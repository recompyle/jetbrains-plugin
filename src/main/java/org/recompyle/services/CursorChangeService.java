package org.recompyle.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.recompyle.services.storage.ProjectStorage;

import java.util.Map;

import static org.recompyle.services.Api.*;
import static org.recompyle.services.DebugService.Logger;

public class CursorChangeService {


    static final ProjectStorage pStorage = ProjectStorage.getInstance();

    static boolean isCaretListener = false;

    static public void init() {
//        System.out.println("Cursor change service init");
//        initEditorListener();
    }

    static CaretListener listener = new CaretListener() {

        @Override
        public void caretPositionChanged(@NotNull CaretEvent e) {
            Project project = e.getEditor().getProject();
            if (pStorage.getConfigForProject(project).config.followCursor && pStorage.getConfigForProject(project).config.enabled) {
                Logger("caretPositionChanged");
                Map<String, String> form = initForm("cursor-change", e.getEditor());
                addLinesAndColumn(form, e);
                sendReq(project,form);
            }
        }

    };



    static void initEditorListener() {
        if (!isCaretListener) {
            EditorFactory.getInstance().getEventMulticaster().addCaretListener(listener, ApplicationManager.getApplication());
        }
        isCaretListener = true;

    }

    static void removeListener() {
        if (isCaretListener) {
            EditorFactory.getInstance().getEventMulticaster().removeCaretListener(listener);
        }
        isCaretListener = false;

    }

}
