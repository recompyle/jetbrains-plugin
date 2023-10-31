package org.recompyle.services;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Objects;

public class ProjectService {

    public static @org.jetbrains.annotations.Nullable Editor getEditor(Project project) {
        return FileEditorManager.getInstance(project).getSelectedTextEditor();
    }

    public static Project getProjectFromRootPath(String rootPath) {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        Project project = null;
        for (Project project1 : projects) {
            if (Objects.equals(project1.getBasePath(), rootPath)) {
                project = project1;
            }
        }
        return project;
    }

    public static Project getProjectFromFile(VirtualFile file) {

        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        Project project = null;

        for (int i = 0; i < projects.length; i++) {
            if (file.getPath().startsWith(Objects.requireNonNull(getIdeRootPath(projects[i])))) {
                project = projects[i];
            }
        }
        return project;
    }

    public static String getIdeRootPath(Project project) {
        if (project != null) {
            Module[] mm = ModuleManager.getInstance(project).getModules();
            if (mm.length > 1 && mm[0].isDisposed()) {
                ModuleRootManager mrm = ModuleRootManager.getInstance(mm[0]);
                if (mrm != null) {
                    VirtualFile IdeRoot = mrm.getContentRoots()[0];
                    return IdeRoot.getPath();
                }
            }
            return project.getBasePath();
        }
        return null;
    }

}

