package com.github.ivanlarios.cleanarchitectureplugin.settings.ui;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ImportExceptionDialog extends JDialog {
    private JPanel contentPane;
    private JTextField importExceptionField;

    public ImportExceptionDialog(@Nullable String oldPackage) {
        importExceptionField.setText(oldPackage);
        setContentPane(contentPane);
        setModal(true);
    }

    public JPanel getContentPane() {
        return contentPane;
    }
    public JTextField getImportExceptionField() {
        return importExceptionField;
    }
}
