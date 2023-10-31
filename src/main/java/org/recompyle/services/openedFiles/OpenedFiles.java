package org.recompyle.services.openedFiles;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;
import org.recompyle.services.socket.SocketIoInstance;
import org.recompyle.services.socket.SocketIoManagerService;
import org.recompyle.services.storage.ProjectStorage;

import static org.recompyle.services.DebugService.Logger;

public class OpenedFiles {
    static final SocketIoManagerService socketService = SocketIoManagerService.getInstance();

    private Project project;
    public MessageBusConnection currentMessageBus;


    public OpenedFiles(Project project) {
        this.project = project;
        if (project != null) {
            this.initFileOpenAndCloseListener();
            this.getOpenedFiles();
        }
    }

    public void initFileOpenAndCloseListener() {
        Logger("initFileOpenAndCloseListener");

        currentMessageBus = project.getMessageBus().connect(project);
        currentMessageBus.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
            @Override
            public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                FileEditorManagerListener.super.fileOpened(source, file);
                Logger("fileOpened");

                getOpenedFiles();
            }

            @Override
            public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                FileEditorManagerListener.super.fileClosed(source, file);
                Logger("fileClosed");

                getOpenedFiles();
            }
        });

    }

    public void getOpenedFiles() {
        FileEditorManager fe = FileEditorManager.getInstance(project);
        FileEditor[] ed = fe.getAllEditors();

        String[] files = new String[ed.length];
        for (int i = 0; i < ed.length; i++) {
            files[i] = ed[i].getFile().getPath();
        }

        SocketIoInstance socketInstance = socketService.getSocketInstanceWithProject(project);
        if (socketInstance != null && socketInstance.socket != null) {
            socketInstance.socket.emit("opened-files", (Object[]) files);
        }
    }
}
