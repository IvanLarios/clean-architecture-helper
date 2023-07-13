package com.github.ivanlarios.cleanarchitectureplugin.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PluginSettings implements Configurable {

    private final PluginSettingState state = ApplicationManager.getApplication().getService(PluginSettingState.class);

    private SettingsComponent settingsComponent;

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
        PluginSettingState settings = PluginSettingState.getInstance();
        boolean modified = settings.enableLinter == settingsComponent.getEnableLinter();
        modified = modified || settings.allowExternalImportsInDomain == settingsComponent.getAllowExternalImportsInDomain();
        modified = modified || settings.allowExternalImportsInApplication == settingsComponent.getAllowExternalImportsInApplication();
        modified = modified || settings.restrictionLevel == settingsComponent.getRestrictionLevel();
        return modified;
    }

    @Override
    public void apply() {
        PluginSettingState settings = PluginSettingState.getInstance();
        settings.enableLinter = settingsComponent.getEnableLinter();
        settings.allowExternalImportsInDomain = settingsComponent.getAllowExternalImportsInDomain();
        settings.allowExternalImportsInApplication = settingsComponent.getAllowExternalImportsInApplication();
        settings.restrictionLevel = settingsComponent.getRestrictionLevel();
    }

    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }
}
