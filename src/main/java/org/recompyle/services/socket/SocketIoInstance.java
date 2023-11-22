package org.recompyle.services.socket;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;
import org.recompyle.services.breakpoint.BreakpointManager;
import org.recompyle.services.openedFiles.OpenedFilesManagerService;
import org.recompyle.services.storage.ProjectStorage;

import java.net.URI;

import static org.recompyle.services.DebugService.Logger;
import static org.recompyle.services.OpenFile.openFile;

public class SocketIoInstance {

    static final ProjectStorage pStorage = ProjectStorage.getInstance();

    public Socket socket;
    public String IdeRoot;

    public SocketIoInstance(String IdeRoot) {
        this.IdeRoot = IdeRoot;
        this.initSocket();
    }

    public void doCheck() {
        if (socket.connected()) {
            socket.emit("ping", 1);
        } else {
//                    Logger("connect socket");
            socket.disconnect().connect();
        }
    }

    public void initSocket() {
        Logger("Init socket Service ");

        IO.Options options = IO.Options.builder()
                .setTransports(new String[]{WebSocket.NAME})
                .setForceNew(false)
                .setReconnection(true)
                .setReconnectionDelay(900)
                .setReconnectionDelayMax(1000)
                .setTimeout(2000)
                .build();

        String port = pStorage.getConfigForIdeRoot(IdeRoot).config.port;
        Logger("PORT SOCKET " + port);

        this.socket = IO.socket(URI.create("http://localhost:" + port + "/ide"), options);

        this.events();
        this.listen();

        this.socket.connect();

        this.socket.emit("ev", "first-connected");

        Logger("END Init socket Service ");

    }

    public void events() {
        this.socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger("socket connected "); // true
            }
        });

        this.socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger("socket diconnected ");
            }
        });

        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
//                Logger("socket error ");
            }
        });

    }

    private void listen() {
        this.socket.on("target-project", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger("On target-project ");
                Logger(args[0]);
                if (OpenedFilesManagerService.getWithIdeRoot(IdeRoot) != null) {
                    OpenedFilesManagerService.getWithIdeRoot(IdeRoot).getOpenedFiles();
                }
                if (BreakpointManager.getWithIdeRoot(IdeRoot) != null) {
                    BreakpointManager.getWithIdeRoot(IdeRoot).updateBreakpoints();
                }
            }
        });

        this.socket.on("open-file", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger("On Open File");
                Logger(args[0]);
                Logger(args[1]);
                Logger(args[2]);
                openFile((String) args[0], (int) args[1], (int) args[2]);
            }
        });

//        this.socket.on("do-disconnect", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Logger("Do Disconnect");
//
//            }
//        });
//
//        this.socket.on("get-opened-files", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Logger("Get opened files");
//                OpenedFilesManagerService.getAllOpenedFiles();
//            }
//        });
    }
}
