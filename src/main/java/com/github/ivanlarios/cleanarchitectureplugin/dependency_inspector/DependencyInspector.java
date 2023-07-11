package com.github.ivanlarios.cleanarchitectureplugin.dependency_inspector;

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiImportList;
import com.intellij.psi.PsiImportStatementBase;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DependencyInspector extends AbstractBaseJavaLocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            @Override
            public void visitImportList(@NotNull PsiImportList importList) {
                super.visitImportList(importList);
                String filePath = importList.getContainingFile().getContainingDirectory().getVirtualFile().getPath();
                if (!filePath.contains("/domain") && !filePath.contains("/application")) {
                    return;
                }
                for(PsiImportStatementBase importStatement : importList.getAllImportStatements()) {
                    if(!hasInfraestructure(importStatement)){
                        continue;
                    }
                    holder.registerProblem(importStatement,
                            InspectionsBundle.message("inspection.cleanarchitecture.import.references.problem.descriptor"));
                }
            }

            private boolean hasInfraestructure(PsiImportStatementBase importStatement) {
                return Objects.requireNonNull(importStatement.getImportReference()).getQualifiedName().contains("infrastructure");
            }
        };
    }

}
