package com.github.ivanlarios.cleanarchitectureplugin.settings.ui;

import com.github.ivanlarios.cleanarchitectureplugin.settings.CleanArchitectureBundle;
import com.github.ivanlarios.cleanarchitectureplugin.settings.PluginSettingState;
import com.google.common.collect.ImmutableMap;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.*;
import java.util.List;

public class SettingsComponent {

    private final String[] restrictionLevelOptions = {
            CleanArchitectureBundle.message("cleanarchitecture.settings.linter.level.error"),
            CleanArchitectureBundle.message("cleanarchitecture.settings.linter.level.warning"),
            CleanArchitectureBundle.message("cleanarchitecture.settings.linter.level.weakwarn"),
            CleanArchitectureBundle.message("cleanarchitecture.settings.linter.level.info")
    };

    private static final ImmutableMap<String, ProblemHighlightType> restrictionLevelToHighlightType =
            new ImmutableMap.Builder<String, ProblemHighlightType>()
                    .put(CleanArchitectureBundle.message("cleanarchitecture.settings.linter.level.error"), ProblemHighlightType.ERROR)
                    .put(CleanArchitectureBundle.message("cleanarchitecture.settings.linter.level.warning"), ProblemHighlightType.WARNING)
                    .put(CleanArchitectureBundle.message("cleanarchitecture.settings.linter.level.weakwarn"), ProblemHighlightType.WEAK_WARNING)
                    .put(CleanArchitectureBundle.message("cleanarchitecture.settings.linter.level.info"), ProblemHighlightType.INFORMATION)
                    .build();

    private final JPanel mainPanel;
    private final JBCheckBox enableLinter = new JBCheckBox(CleanArchitectureBundle.message("cleanarchitecture.settings.linter.enable.checkbox"));
    private final ComboBox<String> restrictionLevel = new ComboBox<>(restrictionLevelOptions);
    private final JBCheckBox disallowExternalImportsInDomain = new JBCheckBox(CleanArchitectureBundle.message("cleanarchitecture.settings.imports.domain.allow.checkbox"));
    private final JBCheckBox disallowExternalImportsInApplication = new JBCheckBox(CleanArchitectureBundle.message("cleanarchitecture.settings.imports.application.allow.checkbox"));
    private final ImportExceptionsPanel importExceptionList = new ImportExceptionsPanel();



    public SettingsComponent(PluginSettingState state) {
        mainPanel = FormBuilder.createFormBuilder()
                .addComponent(enableLinter)
                .addLabeledComponent(new JBLabel(CleanArchitectureBundle.message("cleanarchitecture.settings.linter.level.label")), restrictionLevel, 1, false)
                .addComponent(disallowExternalImportsInDomain, 0)
                .addComponent(disallowExternalImportsInApplication, 0)
                .addComponent(importExceptionList)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
        enableLinter.addActionListener(e -> updateLinterState(enableLinter.isSelected()));
        setComponentState(state);
    }

    public void setComponentState(PluginSettingState state) {
        setEnableLinter(state.isEnableLinter());
        String stateRestrictionLevel =
                restrictionLevelToHighlightType.entrySet().stream()
                        .filter(r -> r.getValue().equals(state.getRestrictionLevel()))
                        .map(Map.Entry::getKey)
                        .findFirst().orElse(CleanArchitectureBundle.message("cleanarchitecture.settings.linter.level.info"));
        setRestrictionLevel(stateRestrictionLevel);
        setDisallowExternalImportsInApplication(state.isDisallowExternalImportsInApplication());
        setDisallowExternalImportsInDomain(state.isDisallowExternalImportsInDomain());
        setExceptions(state.getImportExceptions());
        updateLinterState(state.isEnableLinter());
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
        return this.importExceptionList.getExceptionList();
    }

    public void setExceptions(List<String> exceptions){
        this.importExceptionList.setExceptionList(exceptions);
    }

}