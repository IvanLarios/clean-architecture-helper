package com.github.ivanlarios.cleanarchitectureplugin.dependency_inspector;

import com.github.ivanlarios.cleanarchitectureplugin.settings.PluginSettingState;
import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

public class DependencyInspector extends AbstractBaseJavaLocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {

        PluginSettingState state = PluginSettingState.getInstance(holder.getProject());
        if(!state.isEnableLinter())
            return PsiElementVisitor.EMPTY_VISITOR;
        final String filePath = holder.getFile().getContainingDirectory().getVirtualFile().getPath();

        return new JavaCleanArchitectureVisitor(state, holder, filePath);
    }

}
