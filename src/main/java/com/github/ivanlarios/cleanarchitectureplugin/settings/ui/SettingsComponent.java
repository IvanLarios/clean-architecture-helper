package com.github.ivanlarios.cleanarchitectureplugin.settings.ui;

import com.github.ivanlarios.cleanarchitectureplugin.settings.PluginSettingState;
import com.google.common.collect.ImmutableMap;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.*;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SettingsComponent {

    private final String[] restrictionLevelOptions = {"Error", "Warning", "Weak warning", "Informative"};
    private final List<String> exceptions = new ArrayList<>();
    private final static ImmutableMap<String, ProblemHighlightType> restrictionLevelToHighlightType =
            new ImmutableMap.Builder<String, ProblemHighlightType>()
                    .put("Error", ProblemHighlightType.ERROR)
                    .put("Warning", ProblemHighlightType.WARNING)
                    .put("Weak warning", ProblemHighlightType.WEAK_WARNING)
                    .put("Informative", ProblemHighlightType.INFORMATION)
                    .build();

    private final JPanel mainPanel;
    private final JBCheckBox enableLinter = new JBCheckBox("Enable linter");
    private final ComboBox<String> restrictionLevel = new ComboBox<>(restrictionLevelOptions);
    private final JBCheckBox disallowExternalImportsInDomain = new JBCheckBox("Only allow domain imports on domain layer");
    private final JBCheckBox disallowExternalImportsInApplication = new JBCheckBox("Only allow domain and application imports on application layer");
    private final ImportExceptionPanel importExceptionList = new ImportExceptionPanel();

    public SettingsComponent(PluginSettingState state) {
        mainPanel = FormBuilder.createFormBuilder()
                .addComponent(enableLinter)
                .addLabeledComponent(new JBLabel("Select a restriction level for inspection "), restrictionLevel, 1, false)
                .addComponent(disallowExternalImportsInDomain, 0)
                .addComponent(disallowExternalImportsInApplication, 0)
                .addComponent(importExceptionList)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
        enableLinter.addActionListener(e -> updateLinterState(enableLinter.isSelected()));
        setComponentState(state);
    }

    public void setComponentState(PluginSettingState state) {
        setEnableLinter(state.enableLinter);
        String stateRestrictionLevel =
                restrictionLevelToHighlightType.entrySet().stream()
                        .filter(r -> r.getValue().equals(state.restrictionLevel))
                        .map(Map.Entry::getKey)
                        .findFirst().orElse("Informative");
        setRestrictionLevel(stateRestrictionLevel);
        setDisallowExternalImportsInApplication(state.disallowExternalImportsInApplication);
        setDisallowExternalImportsInDomain(state.disallowExternalImportsInDomain);
        setExceptions(state.importExceptions);
        updateLinterState(state.enableLinter);
    }

    private void updateLinterState(boolean isSelected){
        restrictionLevel.setEnabled(isSelected);
        disallowExternalImportsInApplication.setEnabled(isSelected);
        disallowExternalImportsInDomain.setEnabled(isSelected);
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return restrictionLevel;
    }

    public boolean getEnableLinter() {
        return enableLinter.isSelected();
    }

    public void setEnableLinter(boolean isSelected) {
        enableLinter.setSelected(isSelected);
    }

    @NotNull
    public ProblemHighlightType getRestrictionLevel() {
        String selectedRestrictionLevel = String.valueOf(restrictionLevel.getSelectedItem());
        if(selectedRestrictionLevel == null) {
            return ProblemHighlightType.INFORMATION;
        }
        return Objects.requireNonNull(restrictionLevelToHighlightType.get(selectedRestrictionLevel));
    }

    public void setRestrictionLevel(@NotNull String restrictionLevelItem) {
        restrictionLevel.setSelectedItem(restrictionLevelItem);
    }

    public boolean getDisallowExternalImportsInDomain() {
        return disallowExternalImportsInDomain.isSelected();
    }

    public void setDisallowExternalImportsInDomain(boolean newStatus) {
        disallowExternalImportsInDomain.setSelected(newStatus);
    }

    public boolean getDisallowExternalImportsInApplication() {
        return disallowExternalImportsInApplication.isSelected();
    }

    public void setDisallowExternalImportsInApplication(boolean newStatus) {
        disallowExternalImportsInApplication.setSelected(newStatus);
    }

    public List<String> getExceptions(){
        return this.exceptions;
    }

    public void setExceptions(List<String> exceptions){
        this.exceptions.clear();
        this.exceptions.addAll(exceptions);
        this.importExceptionList.refill(this.exceptions);
    }


    class ImportExceptionPanel extends JPanel {
        private final JBList<String> importList = new JBList<>(new DefaultListModel<>());

        ImportExceptionPanel() {
            setLayout(new BorderLayout());
            importList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            importList.setCellRenderer(new ImportExceptionRenderer());
            importList.getEmptyText().setText("No exceptions were added."); //TODO: Add to bundle
            importList.setBorder(JBUI.Borders.empty());

            ToolbarDecorator decorator = ToolbarDecorator.createDecorator(importList)
                    .setScrollPaneBorder(JBUI.Borders.empty())
                    .setPanelBorder(JBUI.Borders.customLine(JBColor.border(),1,1,0,1))
                    .setAddAction(__ -> addImportException())
                    .setEditAction(__ -> editImportException())
                    .setRemoveAction(__ -> removeImportException())
                    .disableUpDownActions();
            add(decorator.createPanel(), BorderLayout.NORTH);
            JScrollPane scrollPane = new JBScrollPane(importList);
            scrollPane.setBorder(JBUI.Borders.customLine(JBColor.border(), 0, 1, 1, 1));
            add(scrollPane, BorderLayout.CENTER);
            //noinspection DialogTitleCapitalization IDEA-254041
            setBorder(IdeBorderFactory.createTitledBorder("Add packages exceptions", false, JBUI.insetsTop(8)).setShowLine(false));
        }

        void clearList() {
            ((DefaultListModel<String>)importList.getModel()).clear();
            importList.clearSelection();
        }

        void select(@NotNull String importException) {
            ScrollingUtil.selectItem(importList, importException);
        }

        String removeSelected() {
            String selectedValue = getSelectedItem();
            if (selectedValue == null) return null;
            exceptions.remove(selectedValue);
            importExceptionList.refill(exceptions);
            return selectedValue;
        }

        String getSelectedItem() {
            return importList.getSelectedValue();
        }

        private void refill(@NotNull java.util.List<? extends String> exceptions) {
            clearList();
            List<String> copy = new ArrayList<>(exceptions);
            copy.sort(Comparator.comparing(String::toString));
            DefaultListModel<String> model = (DefaultListModel<String>)importList.getModel();
            for (String exception : copy) {
                model.addElement(exception);
            }
            ScrollingUtil.ensureSelectionExists(importList);
        }
    }

    private static class ImportExceptionRenderer extends ColoredListCellRenderer<String> {
        @Override
        protected void customizeCellRenderer(@NotNull JList<? extends String> list, String value, int index, boolean selected, boolean hasFocus) {
            append(value);
        }
    }

    private void addImportException() {
        editImportException(null);
    }

    private void editImportException() {
        String item = importExceptionList.getSelectedItem();
        if (item != null) {
            editImportException(item);
        }
    }

    private void editImportException(@Nullable("null means new") String item) {
        String title = item == null ? "Add Exception" : "Edit Exception";
        ImportExceptionDialog dialog = new ImportExceptionDialog(item);
        DialogBuilder builder = new DialogBuilder(importExceptionList.importList);
        builder.setPreferredFocusComponent(dialog.getImportExceptionField());
        builder.setCenterPanel(dialog.getContentPane());
        builder.setTitle(title);
        builder.showModal(true);
        if (builder.getDialogWrapper().isOK()) {
            String exception = dialog.getImportExceptionField().getText();
            if (StringUtil.isEmpty(exception)) return;
            if(item!=null)
                exceptions.remove(item);
            if(!exceptions.contains(exception)) {
                exceptions.add(exception);
            }
            importExceptionList.refill(exceptions);
            importExceptionList.select(exception);
            IdeFocusManager.getGlobalInstance().doWhenFocusSettlesDown(() -> IdeFocusManager.getGlobalInstance().requestFocus(importExceptionList.importList, true));
        }
    }

    private void removeImportException() {
        String removed = importExceptionList.removeSelected();
        if (removed == null) return;
        exceptions.remove(removed);
        importExceptionList.refill(exceptions);
        IdeFocusManager.getGlobalInstance().doWhenFocusSettlesDown(() -> IdeFocusManager.getGlobalInstance().requestFocus(importExceptionList.importList, true));
    }

}