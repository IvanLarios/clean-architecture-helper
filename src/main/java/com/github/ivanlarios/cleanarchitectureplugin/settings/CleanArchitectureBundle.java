package com.github.ivanlarios.cleanarchitectureplugin.settings;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public final class CleanArchitectureBundle extends AbstractBundle {
    @NonNls
    public static final String BUNDLE_NAME = "messages.CleanArchitecture";
    private static final CleanArchitectureBundle INSTANCE = new CleanArchitectureBundle();

    private CleanArchitectureBundle() {
        super(BUNDLE_NAME);
    }

    public static @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE_NAME) String key,
                                      Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }

}
