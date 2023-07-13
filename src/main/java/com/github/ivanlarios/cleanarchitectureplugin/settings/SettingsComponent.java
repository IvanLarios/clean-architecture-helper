package com.github.ivanlarios.cleanarchitectureplugin.settings;

import com.google.common.collect.ImmutableMap;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Map;
import java.util.Objects;

public class SettingsComponent {

    private final String[] restrictionLevelOptions = {"Error", "Warning", "Informative"};
    private final static ImmutableMap<String, ProblemHighlightType> restrictionLevelToHighlightType =
            new ImmutableMap.Builder<String, ProblemHighlightType>()
                    .put("Error", ProblemHighlightType.ERROR)
                    .put("Warning", ProblemHighlightType.WARNING)
                    .put("Informative", ProblemHighlightType.INFORMATION)
                    .build();

    private final JPanel mainPanel;
    private final JBCheckBox enableLinter = new JBCheckBox("Enable linter");
    private final ComboBox<String> restrictionLevel = new ComboBox<>(restrictionLevelOptions);
    private final JBCheckBox allowExternalImportsInDomain = new JBCheckBox("Only allow domain imports on domain layer");
    private final JBCheckBox allowExternalImportsInApplication = new JBCheckBox("Only allow domain and application imports on application layer");

    public SettingsComponent(PluginSettingState state) {
        mainPanel = FormBuilder.createFormBuilder()
                .addComponent(enableLinter)
                .addLabeledComponent(new JBLabel("Select a restriction level for inspection "), restrictionLevel, 1, false)
                .addComponent(allowExternalImportsInDomain, 0)
                .addComponent(allowExternalImportsInApplication, 0)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
        enableLinter.addActionListener(e -> updateLinterState(enableLinter.isSelected()));
        initComponentState(state);
        updateLinterState(state.enableLinter);
    }

    private void initComponentState(PluginSettingState state) {
        setEnableLinter(state.enableLinter);
        String stateRestrictionLevel =
                restrictionLevelToHighlightType.entrySet().stream()
                        .filter(r -> r.getValue().equals(state.restrictionLevel))
                        .map(Map.Entry::getKey)
                        .findFirst().orElse("Informative");
        setRestrictionLevel(stateRestrictionLevel);
        setAllowExternalImportsInApplication(state.allowExternalImportsInApplication);
        setAllowExternalImportsInDomain(state.allowExternalImportsInDomain);
    }

    private void updateLinterState(boolean isSelected){
        restrictionLevel.setEnabled(isSelected);
        allowExternalImportsInApplication.setEnabled(isSelected);
        allowExternalImportsInDomain.setEnabled(isSelected);
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

    public boolean getAllowExternalImportsInDomain() {
        return allowExternalImportsInDomain.isSelected();
    }

    public void setAllowExternalImportsInDomain(boolean newStatus) {
        allowExternalImportsInDomain.setSelected(newStatus);
    }

    public boolean getAllowExternalImportsInApplication() {
        return allowExternalImportsInApplication.isSelected();
    }

    public void setAllowExternalImportsInApplication(boolean newStatus) {
        allowExternalImportsInApplication.setSelected(newStatus);
    }

}