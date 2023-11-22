package org.recompyle.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.recompyle.dialog.DialogSelectPort;
import org.recompyle.services.storage.ProjectStorage;

import static org.recompyle.services.DebugService.Logger;

public class SelectPort extends AnAction {

    private final ProjectStorage pStorage = ProjectStorage.getInstance();

    public static Project activeProject = null;

    @Override
    public void actionPerformed(AnActionEvent e) {
        new DialogSelectPort().showAndGet();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        String textPort = getText(e.getProject());
        Logger("SelectPort.update ");
        e.getPresentation().setText(textPort);
    }
    

    public String getText(Project project) {
        activeProject = project;
        return "Port : " + pStorage.getConfigForProject(project).config.port;
    }

}
