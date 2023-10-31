package org.recompyle.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import org.jetbrains.annotations.NotNull;
import org.recompyle.services.storage.ProjectStorage;

import static org.recompyle.services.DebugService.Logger;

public class FileSave extends ToggleAction {
    private final ProjectStorage pStorage = ProjectStorage.getInstance();

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        Logger("FileSave.isSelected " + pStorage.getConfigForActionEvent(e).config.fileSave.toString());
        return pStorage.getConfigForActionEvent(e).config.fileSave;
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        pStorage.getConfigForActionEvent(e).config.fileSave = state;
        Logger("FileSave " + pStorage.getConfigForActionEvent(e).config.fileSave.toString());
    }
}
