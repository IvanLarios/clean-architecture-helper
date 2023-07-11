package com.github.ivanlarios.cleanarchitectureplugin.dependency_inspector;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public final class InspectionsBundle extends AbstractBundle {
    @NonNls
    public static final String BUNDLE_NAME = "messages.DependencyInspector";
    private static final InspectionsBundle INSTANCE = new InspectionsBundle();

    private InspectionsBundle() {
        super(BUNDLE_NAME);
    }

    public static @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE_NAME) String key,
                                      Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }

}
