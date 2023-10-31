package org.recompyle.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import org.jetbrains.annotations.NotNull;
import org.recompyle.services.ProjectManagerService;
import org.recompyle.services.openedFiles.OpenedFilesManagerService;
import org.recompyle.services.storage.ProjectStorage;

import static org.recompyle.services.DebugService.Logger;

public class OpenedFilesListener extends ToggleAction {

    private final ProjectStorage pStorage = ProjectStorage.getInstance();

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        Logger("openedFilesListener.isSelected " + pStorage.getConfigForActionEvent(e).config.openedFilesListener.toString());
        return pStorage.getConfigForActionEvent(e).config.openedFilesListener;
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        pStorage.getConfigForActionEvent(e).config.openedFilesListener = state;
        Logger("openedFilesListener " + pStorage.getConfigForActionEvent(e).config.openedFilesListener.toString());
        ProjectManagerService.init();
    }
}
