package org.recompyle.services;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.recompyle.services.socket.SocketIoInstance;
import org.recompyle.services.socket.SocketIoManagerService;

import java.util.HashMap;
import java.util.Map;

import static org.recompyle.services.DebugService.Logger;

public class Api {

    private static final SocketIoManagerService socketService = SocketIoManagerService.getInstance();

    public static void addLinesAndColumn(Map<String, String> form, @NotNull CaretEvent e) {
//        Caret caret = e.getCaret();
        LogicalPosition pos = e.getNewPosition();
        Integer line = pos.line + 1;
        form.put("line", line.toString());
        form.put("column", String.valueOf(pos.column));
        Logger("line " + line.toString());
    }

    public static Map<String, String> initForm(String eventName, Editor editor) {
        Map<String, String> form = new HashMap<String, String>();


        // duplicated
        Document document = editor.getDocument();
        VirtualFile file = FileDocumentManager.getInstance().getFile(document);

        if (file != null && file.exists() && editor.getProject() != null) {
            form.put("ideRoot", ProjectService.getIdeRootPath(editor.getProject()));
            form.put("filePath", file.getPath());
            form.put("event", eventName);
        }

        return form;
    }

    public static void sendReq(Project project, Map<String, String> form) {
        String IdeRoot = ProjectService.getIdeRootPath(project);
        SocketIoInstance socketInst = socketService.getSocketInstanceWithIdeRoot(IdeRoot);
        if (socketInst != null && socketInst.socket != null) {
            Logger("sendReq");
            socketInst.socket.emit("ev", form);
        }
    }

//    public static void sendReqHttp(Map<String, String> form) {
//        try {
//            if(!Objects.equals(form.get("file"), "/Dummy.txt")){
//                postJSON(form);
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        }

//
//    public static CompletableFuture<Void> postJSON(Map<String, String> map)
//            throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String requestBody = objectMapper
//                .writerWithDefaultPrettyPrinter()
//                .writeValueAsString(map);
//
//        String port = appStorage.state.port;
//        URI myUri = URI.create("http://localhost:" + port + "/api/ide-plugin/event");
//        HttpRequest request = HttpRequest.newBuilder(myUri)
//                .header("Content-Type", "application/json")
//                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                .build();
//
//        return HttpClient.newHttpClient()
//                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                .thenApply(HttpResponse::statusCode)
//                .thenAccept(System.out::println);
//    }


}
