package com.github.ivanlarios.cleanarchitectureplugin.settings.ui;

import com.github.ivanlarios.cleanarchitectureplugin.settings.CleanArchitectureBundle;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;


public class ImportExceptionsPanel extends JPanel {
    private final JBList<String> importList = new JBList<>(new DefaultListModel<>());

    public ImportExceptionsPanel() {
        setLayout(new BorderLayout());
        importList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        importList.setCellRenderer(new ImportExceptionRenderer());
        importList.getEmptyText().setText(CleanArchitectureBundle.message("cleanarchitecture.settings.exceptions.list.empty"));
        importList.setBorder(JBUI.Borders.empty());

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(importList)
                .setScrollPaneBorder(JBUI.Borders.empty())
                .setPanelBorder(JBUI.Borders.customLine(JBColor.border(),1,1,0,1))
                .setAddAction(empty -> addImportException())
                .setEditAction(empty -> editImportException())
                .setRemoveAction(empty -> removeImportException())
                .disableUpDownActions();
        add(decorator.createPanel(), BorderLayout.NORTH);
        JScrollPane scrollPane = new JBScrollPane(importList);
        scrollPane.setBorder(JBUI.Borders.customLine(JBColor.border(), 0, 1, 1, 1));
        add(scrollPane, BorderLayout.CENTER);
        //noinspection DialogTitleCapitalization IDEA-254041
        setBorder(IdeBorderFactory.createTitledBorder(CleanArchitectureBundle.message("cleanarchitecture.settings.exceptions.list.label"), false, JBUI.insetsTop(8)).setShowLine(false));
    }

    public List<String> getExceptionList() {
        return Arrays.stream(getImportListModel().toArray()).map(Object::toString).toList();
    }

    public void setExceptionList(List<String> exceptionList) {
        getImportListModel().clear();
        getImportListModel().addAll(exceptionList);
    }
    private DefaultListModel<String> getImportListModel() {
        return (DefaultListModel<String>) importList.getModel();
    }

    private void select(@NotNull String importException) {
        ScrollingUtil.selectItem(importList, importException);
    }

    private void addImportException() {
        showImportExceptionDialog(null);
    }

    private void editImportException() {
        String selectedValue = importList.getSelectedValue();
        if(selectedValue == null) {
            return;
        }
        showImportExceptionDialog(selectedValue);
    }

    private void removeImportException() {
        String selectedValue = importList.getSelectedValue();
        if(selectedValue == null) {
            return;
        }
        getImportListModel().removeElement(selectedValue);
    }

    private void showImportExceptionDialog(@Nullable("null means new") String item) {
        String title = CleanArchitectureBundle.message(item == null ?
                "cleanarchitecture.settings.exceptions.dialog.add.label" :
                "cleanarchitecture.settings.exceptions.dialog.edit.label"
        );
        ImportExceptionDialog dialog = new ImportExceptionDialog(item);
        DialogBuilder builder = new DialogBuilder(importList);
        builder.setPreferredFocusComponent(dialog.getImportExceptionField());
        builder.setCenterPanel(dialog.getContentPane());
        builder.setTitle(title);
        builder.showModal(true);
        if(builder.getDialogWrapper().isOK()) {
            String exception = dialog.getImportExceptionField().getText();
            if(StringUtil.isEmpty(exception) || getImportListModel().contains(exception)){
                return;
            }
            if(item != null) {
                getImportListModel().removeElement(item);
            }
            getImportListModel().addElement(exception);
            select(exception);
            IdeFocusManager.getGlobalInstance().doWhenFocusSettlesDown(() -> IdeFocusManager.getGlobalInstance().requestFocus(importList, true));
        }
    }

    private static class ImportExceptionRenderer extends ColoredListCellRenderer<String> {
        @Override
        protected void customizeCellRenderer(@NotNull JList<? extends String> list, String value, int index, boolean selected, boolean hasFocus) {
            append(value);
        }
    }
}
