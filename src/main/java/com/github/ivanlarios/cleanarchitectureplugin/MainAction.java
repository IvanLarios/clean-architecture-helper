package com.github.ivanlarios.cleanarchitectureplugin;

import com.github.ivanlarios.cleanarchitectureplugin.ui.AddModuleDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class MainAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        AddModuleDialog.main(e);
    }
}