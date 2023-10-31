package org.recompyle.services.openedFiles;


import com.intellij.openapi.project.Project;
import org.recompyle.services.ProjectService;
import org.recompyle.services.storage.ProjectStorage;

import java.util.HashMap;
import java.util.Map;

import static org.recompyle.services.DebugService.Logger;

public class OpenedFilesManagerService {

    static final ProjectStorage pStorage = ProjectStorage.getInstance();

    static Map<String, OpenedFiles> openedFilesMapMap = new HashMap<>();

    public static void initOpenedFiles(Project[] openedProjects) {
        Logger("initOpenedFiles");

        clearOpenedFiles();

        for (int i = 0; i < openedProjects.length; i++) {
            ProjectStorage.AppConfig appConfig = pStorage.getConfigForProject(openedProjects[i]);
            if (appConfig.config.openedFilesListener) {
                Logger("add opened file listener");
                openedFilesMapMap.put(appConfig.ideRoot,
                        new OpenedFiles(ProjectService.getProjectFromRootPath(appConfig.ideRoot)));
            }
        }


    }

    static void clearOpenedFiles() {
        openedFilesMapMap.forEach((s, openedFiles) -> {
            if (openedFiles.currentMessageBus != null) {
                openedFiles.currentMessageBus.disconnect();
            }
        });
        openedFilesMapMap = new HashMap<>();
    }

    public static void getAllOpenedFiles() {
        openedFilesMapMap.forEach((s, openedFiles) -> {
            openedFiles.getOpenedFiles();
        });
    }

    public static OpenedFiles getWithIdeRoot(String ideRoot) {
        return openedFilesMapMap.getOrDefault(ideRoot, null);
    }

}
