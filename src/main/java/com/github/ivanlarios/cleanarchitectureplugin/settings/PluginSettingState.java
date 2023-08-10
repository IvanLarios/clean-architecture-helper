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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@State(
        name = "com.github.ivanlarios.cleanarchitectureplugin.settings.PluginSettingState",
        storages = {@Storage("cleanArchitectureSettings.xml")},
        category = SettingsCategory.PLUGINS
)
public class PluginSettingState implements PersistentStateComponent<PluginSettingState> {

    private boolean enableLinter;
    private ProblemHighlightType restrictionLevel;
    private boolean disallowExternalImportsInDomain;
    private boolean disallowExternalImportsInApplication;
    private List<String> importExceptions;

    public PluginSettingState(){
        this.setEnableLinter(false);
        this.setRestrictionLevel(ProblemHighlightType.WEAK_WARNING);
        this.setDisallowExternalImportsInDomain(false);
        this.setDisallowExternalImportsInApplication(false);
        this.setImportExceptions(new ArrayList<>());
    }

    @Override
    public @Nullable PluginSettingState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PluginSettingState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static PluginSettingState getInstance(@NotNull final Project project) {
        return project.getService(PluginSettingState.class);
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof PluginSettingState anotherState)){
            return false;
        }
        if(this == object) {
            return true;
        }
        return this.isEnableLinter() == anotherState.isEnableLinter()
        && this.getRestrictionLevel() == anotherState.getRestrictionLevel()
        && this.isDisallowExternalImportsInDomain() == anotherState.isDisallowExternalImportsInDomain()
        && this.isDisallowExternalImportsInApplication() == anotherState.isDisallowExternalImportsInApplication()
        && this.getImportExceptions().equals(anotherState.getImportExceptions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                enableLinter,
                restrictionLevel,
                disallowExternalImportsInDomain,
                disallowExternalImportsInApplication,
                importExceptions
        );
    }

    public boolean isEnableLinter() {
        return enableLinter;
    }

    public void setEnableLinter(boolean enableLinter) {
        this.enableLinter = enableLinter;
    }

    public ProblemHighlightType getRestrictionLevel() {
        return restrictionLevel;
    }

    public void setRestrictionLevel(ProblemHighlightType restrictionLevel) {
        this.restrictionLevel = restrictionLevel;
    }

    public boolean isDisallowExternalImportsInDomain() {
        return disallowExternalImportsInDomain;
    }

    public void setDisallowExternalImportsInDomain(boolean disallowExternalImportsInDomain) {
        this.disallowExternalImportsInDomain = disallowExternalImportsInDomain;
    }

    public boolean isDisallowExternalImportsInApplication() {
        return disallowExternalImportsInApplication;
    }

    public void setDisallowExternalImportsInApplication(boolean disallowExternalImportsInApplication) {
        this.disallowExternalImportsInApplication = disallowExternalImportsInApplication;
    }

    public List<String> getImportExceptions() {
        return importExceptions;
    }

    public void setImportExceptions(List<String> importExceptions) {
        this.importExceptions = importExceptions;
    }
}
