package com.github.ivanlarios.cleanarchitectureplugin.settings;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "com.github.ivanlarios.cleanarchitectureplugin.settings.PluginSettingState",
        storages = @Storage("CleanArchitectureSettings.xml")
)
public class PluginSettingState implements PersistentStateComponent<PluginSettingState> {

    public boolean enableLinter = false;
    public ProblemHighlightType restrictionLevel = ProblemHighlightType.WEAK_WARNING;
    public boolean allowExternalImportsInDomain = false;
    public boolean allowExternalImportsInApplication = false;

    @Override
    public void initializeComponent() {
        this.enableLinter = false;
        this.restrictionLevel = ProblemHighlightType.WEAK_WARNING;
        allowExternalImportsInDomain = false;
        allowExternalImportsInApplication = false;
    }
    public static PluginSettingState getInstance() {
        return ApplicationManager.getApplication().getService(PluginSettingState.class);
    }
    @Override
    public @Nullable PluginSettingState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PluginSettingState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
