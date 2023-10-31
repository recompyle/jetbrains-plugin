package org.recompyle.listeners;


import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.project.ProjectManager;
import org.recompyle.services.CursorChangeService;
import org.recompyle.services.ProjectManagerService;
import org.recompyle.services.openedFiles.OpenedFilesManagerService;
import org.recompyle.services.socket.SocketIoManagerService;
import org.recompyle.services.storage.ProjectStorage;

public class AppLifecycle implements AppLifecycleListener {

    private final ProjectStorage pStorage = ProjectStorage.getInstance();

    @Override
    public void appStarted() {
        AppLifecycleListener.super.appStarted();
        System.out.println("appStarted");
        ProjectManagerService.init();
    }
}
