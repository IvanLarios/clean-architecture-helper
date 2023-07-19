package com.github.ivanlarios.cleanarchitectureplugin.dependency_inspector;

import com.github.ivanlarios.cleanarchitectureplugin.settings.CleanArchitectureBundle;
import com.github.ivanlarios.cleanarchitectureplugin.settings.PluginSettingState;
import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DependencyInspector extends AbstractBaseJavaLocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {

        PluginSettingState state = PluginSettingState.getInstance(holder.getProject());
        if(!state.enableLinter)
            return PsiElementVisitor.EMPTY_VISITOR;
        return new JavaElementVisitor() {

            @Override
            public void visitImportList(@NotNull PsiImportList importList) {
                super.visitImportList(importList);
                String filePath = importList.getContainingFile().getContainingDirectory().getVirtualFile().getPath();
                if (!filePath.contains("/domain") && !filePath.contains("/application")) {
                    return;
                }
                for(PsiImportStatementBase importStatement : importList.getAllImportStatements()) {
                    if(filePath.contains("/domain")){
                        verifyDomainFile(filePath, importStatement);
                        continue;
                    }
                    if(filePath.contains("application")){
                        verifyApplicationFile(filePath, importStatement);
                    }
                }
            }

            private void verifyDomainFile(String packagePath, PsiImportStatementBase importStatement){
                if(!state.disallowExternalImportsInDomain){
                    return;
                }
                String modulePath = packagePath.substring(packagePath.indexOf("/main/")+6, packagePath.indexOf("/domain")+7).replace('/', '.');
                String importModulePath = Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName();
                if(!importModulePath.startsWith(modulePath)
                        && !importModulePath.contains("shared.domain")
                        && !isFromJavaSDK(importStatement)
                ){
                    holder.registerProblem(importStatement,
                            CleanArchitectureBundle.message("cleanarchitecture.import.references.problems.domain.descriptor"),
                            state.restrictionLevel);
                }

            }

            private void verifyApplicationFile(String packagePath, PsiImportStatementBase importStatement){
                if(!state.disallowExternalImportsInApplication){
                    return;
                }
                String modulePath = packagePath.substring(packagePath.indexOf("/main/")+6, packagePath.indexOf("/application")+12).replace('/', '.');
                String importModulePath = Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName();
                if(!importModulePath.startsWith(modulePath)
                        && !importModulePath.startsWith(modulePath.replace(".application", ".domain"))
                        && !importModulePath.contains("shared.domain")
                        && !importModulePath.contains("shared.application")
                        && !isFromJavaSDK(importStatement)
                ){
                    holder.registerProblem(importStatement,
                            CleanArchitectureBundle.message("cleanarchitecture.import.references.problems.application.descriptor"),
                            state.restrictionLevel);
                }

            }

            private boolean isFromJavaSDK(PsiImportStatementBase importStatement) {
                return Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName().startsWith("java");
            }

        };
    }

}
