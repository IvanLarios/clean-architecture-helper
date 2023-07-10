package com.github.ivanlarios.cleanarchitectureplugin.ui;

import com.github.ivanlarios.cleanarchitectureplugin.backend.ModuleCreator;
import com.github.ivanlarios.cleanarchitectureplugin.models.ModuleModel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;
import java.awt.event.*;

public class AddModuleDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField moduleNameInput;
    private JLabel moduleNameLabel;
    private JCheckBox persistenceCheckBox;
    private JCheckBox apiCheckBox;
    private final AnActionEvent event;
    public AddModuleDialog(AnActionEvent event) {
        this.event = event;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        ModuleModel moduleModel = new ModuleModel(moduleNameInput.getText(), persistenceCheckBox.isSelected(), apiCheckBox.isSelected());
        ModuleCreator.createModuleFolderStructure(moduleModel, event.getProject(), ModuleCreator.getPath(event));
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(AnActionEvent e) {
        AddModuleDialog dialog = new AddModuleDialog(e);
        dialog.setUndecorated(true);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
