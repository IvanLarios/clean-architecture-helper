package com.github.ivanlarios.cleanarchitectureplugin.dependency_inspector;

import com.github.ivanlarios.cleanarchitectureplugin.settings.CleanArchitectureBundle;
import com.github.ivanlarios.cleanarchitectureplugin.settings.PluginSettingState;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class JavaCleanArchitectureVisitor extends JavaElementVisitor {

    private final PluginSettingState state;
    private final ProblemsHolder holder;
    private final String filePath;

    private static final String DOMAIN_CONTEXT_PATH = ".domain";
    private static final String APPLICATION_CONTEXT_PATH = ".application";


    public JavaCleanArchitectureVisitor(PluginSettingState state, ProblemsHolder holder, String filePath) {
        this.state = state;
        this.holder = holder;
        this.filePath = filePath;
    }

    @Override
    public void visitImportList(@NotNull PsiImportList importList){
        super.visitImportList(importList);
        final PsiElement parent = importList.getParent();
        if (!(parent instanceof PsiJavaFile javaFile)) {
            return;
        }
        final String packageName = javaFile.getPackageName();
        if ((!packageName.contains(DOMAIN_CONTEXT_PATH) && !packageName.contains(APPLICATION_CONTEXT_PATH)) || filePath.contains("/test/")) {
            return;
        }
        for(PsiImportStatementBase importStatement : importList.getAllImportStatements()) {
            if(packageName.contains(DOMAIN_CONTEXT_PATH)){
                verifyDomainFile(state, holder, packageName, importStatement);
                continue;
            }
            if(packageName.contains(APPLICATION_CONTEXT_PATH)){
                verifyApplicationFile(state, holder,packageName, importStatement);
            }
        }
    }

    private void verifyDomainFile(PluginSettingState state, ProblemsHolder holder, String packageName, PsiImportStatementBase importStatement){
        if(!state.isDisallowExternalImportsInDomain()){
            return;
        }
        String domainPackagePath = packageName.substring(0, packageName.indexOf(DOMAIN_CONTEXT_PATH)+DOMAIN_CONTEXT_PATH.length());
        String importModulePath = Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName();
        if(!importModulePath.startsWith(domainPackagePath)
                && !importModulePath.contains("shared.domain")
                && !isOnExceptionList(state, importStatement)
                && !isFromJavaSDK(importStatement)
        ){
            holder.registerProblem(importStatement,
                    CleanArchitectureBundle.message("cleanarchitecture.import.references.problems.domain.descriptor"),
                    state.getRestrictionLevel());
        }

    }

    private void verifyApplicationFile(PluginSettingState state, ProblemsHolder holder, String packageName, PsiImportStatementBase importStatement){
        if(!state.isDisallowExternalImportsInApplication()){
            return;
        }
        String applicationPackagePath = packageName.substring(0, packageName.indexOf(APPLICATION_CONTEXT_PATH)+APPLICATION_CONTEXT_PATH.length());
        String importModulePath = Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName();
        if(!importModulePath.startsWith(applicationPackagePath)
                && !importModulePath.startsWith(applicationPackagePath.replace(APPLICATION_CONTEXT_PATH, DOMAIN_CONTEXT_PATH))
                && !importModulePath.contains("shared.domain")
                && !importModulePath.contains("shared.application")
                && !isOnExceptionList(state, importStatement)
                && !isFromJavaSDK(importStatement)
        ){
            holder.registerProblem(importStatement,
                    CleanArchitectureBundle.message("cleanarchitecture.import.references.problems.application.descriptor"),
                    state.getRestrictionLevel());
        }

    }

    private boolean isOnExceptionList(PluginSettingState state, PsiImportStatementBase importStatement){
        return state.getImportExceptions().stream()
                .anyMatch(exception ->
                        Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName().startsWith(exception)
                );
    }
    private boolean isFromJavaSDK(PsiImportStatementBase importStatement) {
        return Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName().startsWith("java");
    }
}
