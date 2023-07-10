package com.github.ivanlarios.cleanarchitectureplugin.models;

public class ModuleModel {

    private final String name;
    private final boolean hasPersistence;
    private final boolean hasApi;

    public ModuleModel(String name, boolean hasPersistence, boolean hasApi) {
        this.name = name;
        this.hasPersistence = hasPersistence;
        this.hasApi = hasApi;
    }

    public boolean hasApi() {
        return hasApi;
    }

    public boolean hasPersistence() {
        return hasPersistence;
    }

    public String getName() {
        return name;
    }
}
