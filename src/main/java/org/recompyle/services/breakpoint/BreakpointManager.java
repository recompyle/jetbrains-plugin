package org.recompyle.services.breakpoint;

import com.intellij.openapi.project.Project;
import org.recompyle.services.storage.ProjectStorage;

import java.util.HashMap;
import java.util.Map;

public class BreakpointManager {
    static final ProjectStorage pStorage = ProjectStorage.getInstance();
    static Map<String, BreakpointsList> breakpointsListMap = new HashMap<>();

    public static void initBreakpoints(Project[] openedProjects) {
        clearBreakpoints();

        for (int i = 0; i < openedProjects.length; i++) {
            ProjectStorage.AppConfig appConfig = pStorage.getConfigForProject(openedProjects[i]);
            if (appConfig.config.breakpointListener) {
                breakpointsListMap.put(appConfig.ideRoot, new BreakpointsList(openedProjects[i]));
            }
        }
    }

    static void clearBreakpoints() {
        breakpointsListMap.forEach((s, breakpointsList) -> {
            breakpointsList.clearListeners();
        });
        breakpointsListMap = new HashMap<>();
    }

    public static BreakpointsList getWithIdeRoot(String ideRoot) {
        return breakpointsListMap.getOrDefault(ideRoot,null);
    }
}
