package com.github.ivanlarios.cleanarchitectureplugin.settings;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.SettingsCategory;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "com.github.ivanlarios.cleanarchitectureplugin.settings.PluginSettingState",
        storages = {@Storage("cleanArchitectureSettings.xml")},
        category = SettingsCategory.PLUGINS
)
public class PluginSettingState implements PersistentStateComponent<PluginSettingState> {

    public boolean enableLinter;
    public ProblemHighlightType restrictionLevel;
    public boolean disallowExternalImportsInDomain;
    public boolean disallowExternalImportsInApplication;

    public PluginSettingState(){
        this.enableLinter = false;
        this.restrictionLevel = ProblemHighlightType.WEAK_WARNING;
        this.disallowExternalImportsInDomain = false;
        this.disallowExternalImportsInApplication = false;
    }

    public static PluginSettingState getInstance(@NotNull final Project project) {
        return project.getService(PluginSettingState.class);
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
