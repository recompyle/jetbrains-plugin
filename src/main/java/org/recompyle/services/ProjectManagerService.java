package org.recompyle.services;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.NotNull;
//import org.recompyle.services.breakpoint.BreakpointManager;
import org.recompyle.services.breakpoint.BreakpointManager;
import org.recompyle.services.openedFiles.OpenedFilesManagerService;
import org.recompyle.services.socket.SocketIoManagerService;
import org.recompyle.services.storage.ProjectStorage;

import static org.recompyle.services.DebugService.Logger;

public class ProjectManagerService {

    static final ProjectStorage pStorage = ProjectStorage.getInstance();
    static boolean initialized = false;

    public static void init() {
        Logger("## ProjectManagerService INIT");

        @NotNull Project[] openedProjects = ProjectManager.getInstance().getOpenProjects();
        for (int i = 0; i < openedProjects.length; i++) {
            Logger("## Project " + openedProjects[i].getBasePath());
            // create the config if does'nt exist
            ProjectStorage.AppConfig config = pStorage.getConfigForProject(openedProjects[i]);
        }

        SocketIoManagerService.getInstance().initSocketInstances(openedProjects);
        OpenedFilesManagerService.initOpenedFiles(openedProjects);
        BreakpointManager.initBreakpoints(openedProjects);

        CursorChangeService.init();

        initialized = true;
    }


}
