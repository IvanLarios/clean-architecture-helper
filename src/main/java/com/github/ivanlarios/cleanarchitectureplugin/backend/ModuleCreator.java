package com.github.ivanlarios.cleanarchitectureplugin.backend;

import com.github.ivanlarios.cleanarchitectureplugin.models.ModuleModel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;

public class ModuleCreator {
    public static void createModuleFolderStructure(ModuleModel module, Project project, PsiDirectory baseDirectory) {
        WriteCommandAction.runWriteCommandAction(project, () -> createFilesAndDirectories(module, baseDirectory));
    }
    private static void createFilesAndDirectories(ModuleModel data, PsiDirectory baseDirectory) {
        PsiDirectory baseModuleDirectory = baseDirectory.createSubdirectory(data.getName());
        baseModuleDirectory.createSubdirectory("application");
        baseModuleDirectory.createSubdirectory("domain");
        PsiDirectory infraestructureDirectory = baseModuleDirectory.createSubdirectory("infrastructure");
        if(data.hasApi()) {
            infraestructureDirectory.createSubdirectory("api");
        }
        if(data.hasPersistence()){
            infraestructureDirectory.createSubdirectory("persistence");
        }
    }

    public static PsiDirectory getPath(AnActionEvent event) {
        return (PsiDirectory) event.getData(CommonDataKeys.NAVIGATABLE);
    }

}
