package com.github.ivanlarios.cleanarchitectureplugin.module_generator.ui;

import com.github.ivanlarios.cleanarchitectureplugin.module_generator.backend.ModuleCreator;
import com.github.ivanlarios.cleanarchitectureplugin.module_generator.models.ModuleModel;
import com.github.ivanlarios.cleanarchitectureplugin.settings.CleanArchitectureBundle;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AddModuleDialog extends DialogWrapper {
    private JPanel contentPane;
    private JTextField moduleNameInput;
    private JLabel moduleNameLabel;
    private JCheckBox persistenceCheckBox;
    private JCheckBox apiCheckBox;
    private final AnActionEvent event;

    public AddModuleDialog(AnActionEvent event) {
        super(true);
        setTitle(CleanArchitectureBundle.message("cleanarchitecture.action.dialog.title"));
        init();
        this.event = event;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return contentPane;
    }

    @Override
    protected void doOKAction() {
        ModuleModel moduleModel = new ModuleModel(moduleNameInput.getText(), persistenceCheckBox.isSelected(), apiCheckBox.isSelected());
        ModuleCreator.createModuleFolderStructure(moduleModel, event.getProject(), ModuleCreator.getPath(event));
        dispose();
    }

    @Override
    public void doCancelAction() {
        dispose();
    }

    public static void initDialog(AnActionEvent e) {
        AddModuleDialog dialog = new AddModuleDialog(e);
        dialog.show();
    }

}
