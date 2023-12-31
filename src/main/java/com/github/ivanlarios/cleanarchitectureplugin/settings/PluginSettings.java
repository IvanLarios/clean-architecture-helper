package com.github.ivanlarios.cleanarchitectureplugin.settings;

import com.github.ivanlarios.cleanarchitectureplugin.settings.ui.SettingsComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PluginSettings implements Configurable {

    private final Project project;
    private final PluginSettingState state;
    private SettingsComponent settingsComponent;

    public PluginSettings(@NotNull final Project project){
        this.project = project;
        state = project.getService(PluginSettingState.class);
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return CleanArchitectureBundle.message("cleanarchitecture.settings.name");
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return settingsComponent.getPreferredFocusedComponent();
    }

    @Override
    public @Nullable JComponent createComponent() {
        settingsComponent = new SettingsComponent(state);
        return settingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        PluginSettingState settings = PluginSettingState.getInstance(project);
        return settings.equals(state);
    }

    @Override
    public void apply() {
        state.setEnableLinter(settingsComponent.getEnableLinter());
        state.setDisallowExternalImportsInDomain(settingsComponent.getDisallowExternalImportsInDomain());
        state.setDisallowExternalImportsInApplication(settingsComponent.getDisallowExternalImportsInApplication());
        state.setRestrictionLevel(settingsComponent.getRestrictionLevel());
        state.setImportExceptions(settingsComponent.getExceptions());
    }

    @Override
    public void reset() {
        settingsComponent.setComponentState(state);
    }

    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }
}
