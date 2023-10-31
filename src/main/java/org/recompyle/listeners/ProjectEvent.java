package org.recompyle.listeners;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;
import org.recompyle.services.ProjectManagerService;

import static org.recompyle.services.DebugService.Logger;

public class ProjectEvent implements ProjectManagerListener {
    @Override
    public void projectOpened(@NotNull Project project) {
        ProjectManagerListener.super.projectOpened(project);
        Logger("projectOpened");
        ProjectManagerService.updateProjects();
    }

    @Override
    public void projectClosed(@NotNull Project project) {
        ProjectManagerListener.super.projectClosed(project);
        Logger("projectClosed");
        ProjectManagerService.updateProjects();
    }
}
