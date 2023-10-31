package org.recompyle.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import org.jetbrains.annotations.NotNull;
import org.recompyle.services.ProjectManagerService;
import org.recompyle.services.storage.ProjectStorage;

import static org.recompyle.services.DebugService.Logger;

public class BreakpointListener extends ToggleAction {

    private final ProjectStorage pStorage = ProjectStorage.getInstance();

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        Logger("breakpointListener.isSelected " + pStorage.getConfigForActionEvent(e).config.breakpointListener.toString());
        return pStorage.getConfigForActionEvent(e).config.breakpointListener;
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        pStorage.getConfigForActionEvent(e).config.breakpointListener = state;
        Logger("breakpointListener " + pStorage.getConfigForActionEvent(e).config.breakpointListener.toString());
        ProjectManagerService.init();
    }
}
