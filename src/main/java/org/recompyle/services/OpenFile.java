package org.recompyle.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.recompyle.services.storage.ProjectStorage;

public class OpenFile {
    static final ProjectStorage pStorage = ProjectStorage.getInstance();

    public static void openFile(String filePath, int line, int column) {

        VirtualFile file = LocalFileSystem.getInstance().refreshAndFindFileByPath(filePath);
        if (file != null && file.isValid()) {
//            socket.emit("ev", "file vlid " + projects.length);

            Project pj = ProjectService.getProjectFromFile(file);
            OpenFileDescriptor descriptor = new OpenFileDescriptor(pj, file);
            ApplicationManager.getApplication().invokeLater(new Runnable() {
                @Override
                public void run() {
                    Editor editor = FileEditorManager.getInstance(pj).openTextEditor(descriptor, true);
                    assert editor != null;
                    LogicalPosition pos = new LogicalPosition(line, column);
                    editor.getCaretModel().moveToLogicalPosition(pos);
                    editor.getScrollingModel().scrollTo(pos, ScrollType.CENTER);

                }
            });
        }
//        socket.emit("ev", "opened file");
//        System.out.println("END Open File");

    }
}
