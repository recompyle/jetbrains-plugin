package org.recompyle.services.breakpoint;

import com.google.gson.Gson;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.breakpoints.XBreakpoint;
import com.intellij.xdebugger.breakpoints.XBreakpointListener;
import com.intellij.xdebugger.breakpoints.XBreakpointManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.recompyle.services.socket.SocketIoManagerService;

import java.util.ArrayList;
import java.util.List;

import static org.recompyle.services.DebugService.Logger;

public class BreakpointsList {
    static final SocketIoManagerService socketService = SocketIoManagerService.getInstance();
    @NotNull XBreakpointManager bpMgr;
    Project project;

    BreakpointsList(Project mProject) {
        project = mProject;
        bpMgr = XDebuggerManager.getInstance(project).getBreakpointManager();
        updateBreakpoints();
    }



    public void updateBreakpoints() {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                updateBreakpointsInvokeLater();
            }
        });
    }

    public void updateBreakpointsInvokeLater() {
        XBreakpoint<?> @NotNull [] bps = bpMgr.getAllBreakpoints();

        List<BreakPointPosition> list = new ArrayList<>();

        for (int i = 0; i < bps.length; i++) {
            @Nullable XSourcePosition pos = bps[i].getSourcePosition();
            if (pos != null && pos.getFile() != null) {
                list.add(getBreakpointPosition(project, pos.getFile(), pos.getOffset()));
            }
        }

        Logger("Send update breakpoints " + bps.length + " " + project.getBasePath());
        Gson gson = new Gson();
        String json = gson.toJson(list);
        socketService.getSocketInstanceWithProject(project)
                .socket.emit("breakpoints", project.getBasePath(), json);
    }

    public BreakPointPosition getBreakpointPosition(Project project, VirtualFile virtualFile, Integer offset) {
        Document document = FileDocumentManager.getInstance().getDocument(virtualFile);

        if (document != null) {
            // Convert offset to line number
            int lineNumber = document.getLineNumber(offset);
            int columnNumber = offset - document.getLineStartOffset(lineNumber);

            return new BreakPointPosition(virtualFile.getPath(), lineNumber + 1, columnNumber + 1);
        }

        return null;
    }

}


