package org.recompyle.listeners;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.recompyle.services.ProjectService;
import org.recompyle.services.storage.ProjectStorage;

import java.util.Map;

import static org.recompyle.services.Api.initForm;
import static org.recompyle.services.Api.sendReq;


public class FileSaveListener implements FileDocumentManagerListener {

    private final ProjectStorage pStorage = ProjectStorage.getInstance();

    @Override
    public void beforeDocumentSaving(@NotNull Document document) {
//         Logger("beforeDocumentSaving");

        VirtualFile file = FileDocumentManager.getInstance().getFile(document);
        Project pj = ProjectService.getProjectFromFile(file);
        if (pj != null) {
            ProjectStorage.ProjectConfig config = pStorage.getConfigForProject(pj).config;
            if (config.fileSave) {
                Editor editor = ProjectService.getEditor(pj);
                if (editor != null) {
                    Map<String, String> form = initForm("file-saved", editor);
                    sendReq(pj, form);
                }

            }
        }

    }

}