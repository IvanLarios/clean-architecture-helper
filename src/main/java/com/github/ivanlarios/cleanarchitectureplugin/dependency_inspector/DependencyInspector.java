package com.github.ivanlarios.cleanarchitectureplugin.dependency_inspector;

import com.github.ivanlarios.cleanarchitectureplugin.settings.CleanArchitectureBundle;
import com.github.ivanlarios.cleanarchitectureplugin.settings.PluginSettingState;
import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DependencyInspector extends AbstractBaseJavaLocalInspectionTool {
    
    static final String DOMAIN_CONTEXT_PATH = ".domain";
    static final String APPLICATION_CONTEXT_PATH = ".application";

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {

        PluginSettingState state = PluginSettingState.getInstance(holder.getProject());
        if(!state.enableLinter)
            return PsiElementVisitor.EMPTY_VISITOR;

        String packageName = ((PsiJavaFile)holder.getFile()).getPackageName();
        String filePath = holder.getFile().getContainingDirectory().getVirtualFile().getPath();

        return new JavaElementVisitor() {

            @Override
            public void visitImportList(@NotNull PsiImportList importList){
                super.visitImportList(importList);
                if ((!packageName.contains(DOMAIN_CONTEXT_PATH) && !packageName.contains(APPLICATION_CONTEXT_PATH)) || filePath.contains("/test/")) {
                    return;
                }
                for(PsiImportStatementBase importStatement : importList.getAllImportStatements()) {
                    if(packageName.contains(DOMAIN_CONTEXT_PATH)){
                        verifyDomainFile(packageName, importStatement);
                        continue;
                    }
                    if(packageName.contains(APPLICATION_CONTEXT_PATH)){
                        verifyApplicationFile(packageName, importStatement);
                    }
                }
            }

            private void verifyDomainFile(String packageName, PsiImportStatementBase importStatement){
                if(!state.disallowExternalImportsInDomain){
                    return;
                }
                String domainPackagePath = packageName.substring(0, packageName.indexOf(DOMAIN_CONTEXT_PATH)+DOMAIN_CONTEXT_PATH.length());
                String importModulePath = Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName();
                if(!importModulePath.startsWith(domainPackagePath)
                        && !importModulePath.contains("shared.domain")
                        && !isOnExceptionList(importStatement)
                        && !isFromJavaSDK(importStatement)
                ){
                    holder.registerProblem(importStatement,
                            CleanArchitectureBundle.message("cleanarchitecture.import.references.problems.domain.descriptor"),
                            state.restrictionLevel);
                }

            }

            private void verifyApplicationFile(String packageName, PsiImportStatementBase importStatement){
                if(!state.disallowExternalImportsInApplication){
                    return;
                }
                String applicationPackagePath = packageName.substring(0, packageName.indexOf(APPLICATION_CONTEXT_PATH)+APPLICATION_CONTEXT_PATH.length());
                String importModulePath = Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName();
                if(!importModulePath.startsWith(applicationPackagePath)
                        && !importModulePath.startsWith(applicationPackagePath.replace(APPLICATION_CONTEXT_PATH, DOMAIN_CONTEXT_PATH))
                        && !importModulePath.contains("shared.domain")
                        && !importModulePath.contains("shared.application")
                        && !isOnExceptionList(importStatement)
                        && !isFromJavaSDK(importStatement)
                ){
                    holder.registerProblem(importStatement,
                            CleanArchitectureBundle.message("cleanarchitecture.import.references.problems.application.descriptor"),
                            state.restrictionLevel);
                }

            }

            private boolean isOnExceptionList(PsiImportStatementBase importStatement){
                return state.importExceptions.stream()
                        .anyMatch(exception ->
                                Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName().startsWith(exception)
                        );
            }
            private boolean isFromJavaSDK(PsiImportStatementBase importStatement) {
                return Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName().startsWith("java");
            }

        };
    }

}
