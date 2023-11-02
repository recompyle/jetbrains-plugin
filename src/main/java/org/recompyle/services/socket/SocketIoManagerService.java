package org.recompyle.services.socket;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.recompyle.services.ProjectService;
import org.recompyle.services.storage.ProjectStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.recompyle.services.DebugService.Logger;

public class SocketIoManagerService {

    static final ProjectStorage pStorage = ProjectStorage.getInstance();

    public ScheduledExecutorService socketChecker = Executors.newSingleThreadScheduledExecutor();

    Map<String, SocketIoInstance> socketIoInstanceMap = new HashMap<>();


    public SocketIoManagerService() {
        this.checkSocket();
    }

    public void initSocketInstances(Project[] openedProjects) {
        // clear previous instances
        clearInstances();


        for (int i = 0; i < openedProjects.length; i++) {
            ProjectStorage.AppConfig appConfig = pStorage.getConfigForProject(openedProjects[i]);
            if (appConfig.config.enabled) {
                socketIoInstanceMap.put(appConfig.ideRoot, new SocketIoInstance(appConfig.ideRoot));
            }
        }

    }

    public void clearInstances() {
        socketIoInstanceMap.forEach((s, socketIoInstance) -> {
            socketIoInstance.socket.off();
            socketIoInstance.socket.disconnect();
        });
        socketIoInstanceMap = new HashMap<>();
    }


    public void checkSocket() {
        socketChecker.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
//                Logger("checkSocket");
                socketIoInstanceMap.forEach((s, socketIoInstance) -> {
                    socketIoInstance.doCheck();
                });


            }
        }, 2, 1, TimeUnit.SECONDS);
    }

    public SocketIoInstance getSocketInstanceWithIdeRoot(String IdeRoot) {
        return socketIoInstanceMap.get(IdeRoot);
    }

    public SocketIoInstance getSocketInstanceWithProject(Project project) {
        return socketIoInstanceMap.get(ProjectService.getIdeRootPath(project));
    }


    public static SocketIoManagerService getInstance() {
        Logger("Get socket instance");
        return ServiceManager.getService(SocketIoManagerService.class);
    }
}