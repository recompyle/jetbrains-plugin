package org.recompyle.services.breakpoint;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.breakpoints.XBreakpoint;
import org.jetbrains.annotations.NotNull;
import org.recompyle.services.ProjectService;
import org.recompyle.services.storage.ProjectStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.recompyle.services.DebugService.Logger;

public class BreakpointManager {
    static final ProjectStorage pStorage = ProjectStorage.getInstance();
    static Map<String, BreakpointsList> breakpointsListMap = new HashMap<>();

    public static void initBreakpoints(Project[] openedProjects) {
        clearBreakpoints();

        for (int i = 0; i < openedProjects.length; i++) {
            ProjectStorage.AppConfig appConfig = pStorage.getConfigForProject(openedProjects[i]);
            if (appConfig.config.breakpointListener && appConfig.config.enabled) {
                breakpointsListMap.put(appConfig.ideRoot, new BreakpointsList(openedProjects[i]));
            }
        }
    }

    static void clearBreakpoints() {
        breakpointsListMap = new HashMap<>();
    }

    public static BreakpointsList getWithIdeRoot(String ideRoot) {
        return breakpointsListMap.getOrDefault(ideRoot, null);
    }

    public static void sendUpdateWithBreakpoint(@NotNull XBreakpoint breakpoint) {
        Logger("sendUpdateWithBreakpoint " + breakpoint.getSourcePosition().toString());
        if (breakpoint.getSourcePosition() != null) {
            VirtualFile file = breakpoint.getSourcePosition().getFile();
            Project project = ProjectService.getProjectFromFile(file);
            if (project != null) {
                BreakpointsList bp = breakpointsListMap.get(project.getBasePath());
                if (bp != null) {
                    bp.updateBreakpoints();
                }
            }
        }

    }
}
