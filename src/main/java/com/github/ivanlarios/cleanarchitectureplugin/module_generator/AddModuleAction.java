package com.github.ivanlarios.cleanarchitectureplugin.module_generator;

import com.github.ivanlarios.cleanarchitectureplugin.module_generator.ui.AddModuleDialog;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class AddModuleAction extends DumbAwareAction {

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setEnabledAndVisible(isPsiElementDirectory(event));
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        AddModuleDialog.initDialog(event);
    }

    private boolean isPsiElementDirectory(@NotNull AnActionEvent event){
        VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);
        return file != null && file.isDirectory();
    }
}