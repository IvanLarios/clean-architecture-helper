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

        PluginSettingState state = PluginSettingState.getInstance();
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
                    if(filePath.contains("/domain") && state.allowExternalImportsInDomain) {
                        continue;
                    }
                    if(filePath.contains("/application") && !state.allowExternalImportsInDomain) {
                        continue;
                    }
                    if(filePath.contains("/domain")){
                        if(hasInfraestructure(importStatement)){
                            holder.registerProblem(importStatement,
                                    CleanArchitectureBundle.message("cleanarchitecture.import.references.problems.infra.descriptor"),
                                    state.restrictionLevel);
                        }
                        if(!isFromJavaSDK(importStatement) && !hasDomain(importStatement)){
                            holder.registerProblem(importStatement,
                                    CleanArchitectureBundle.message("cleanarchitecture.import.references.problems.app.descriptor"),
                                    state.restrictionLevel);
                        }

                    }
                    if(filePath.contains("application")){
                        if(hasInfraestructure(importStatement)){
                            holder.registerProblem(importStatement,
                                    CleanArchitectureBundle.message("cleanarchitecture.import.references.problems.infra.descriptor"),
                                    state.restrictionLevel);
                        }
                        if(!hasApplication(importStatement) && !hasDomain(importStatement) && !isFromJavaSDK(importStatement)) {
                            holder.registerProblem(importStatement,
                                    CleanArchitectureBundle.message("cleanarchitecture.import.references.problems.app.descriptor"),
                                    state.restrictionLevel);
                        }

                    }
                }
            }

            private boolean isFromJavaSDK(PsiImportStatementBase importStatement) {
                return Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName().startsWith("java");
            }

            private boolean hasDomain(PsiImportStatementBase importStatement) {
                return Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName().contains("domain");
            }
            private boolean hasInfraestructure(PsiImportStatementBase importStatement) {
                return Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName().contains("infrastructure");
            }

            private boolean hasApplication(PsiImportStatementBase importStatement) {
                return Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName().contains("application");
            }
        };
    }

}
