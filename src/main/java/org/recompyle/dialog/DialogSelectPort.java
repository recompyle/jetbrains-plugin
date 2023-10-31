package org.recompyle.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import org.recompyle.actions.SelectPort;
import org.recompyle.services.ProjectManagerService;
import org.recompyle.services.socket.SocketIoManagerService;
import org.recompyle.services.storage.ProjectStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static org.recompyle.services.DebugService.Logger;

public class DialogSelectPort extends DialogWrapper {

    JPanel dialogPanel;
    JTextField textFieldPort;
    private final ProjectStorage pStorage = ProjectStorage.getInstance();

    public DialogSelectPort() {
        super(true); // use current window as parent
        setTitle("Port");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setSize(500, 500);

        textFieldPort = new JTextField(pStorage.getConfigForProject(SelectPort.activeProject).config.port);
        textFieldPort.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // if it's not a number, ignore the event
                }
            }
        });

        dialogPanel.add(textFieldPort);

        this.setOKButtonText("Save");

        return dialogPanel;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();

        pStorage.getConfigForProject(SelectPort.activeProject).config.port = textFieldPort.getText();
        Logger("save socket port");
        Logger(pStorage.getConfigForProject(SelectPort.activeProject).config.port);
        ProjectManagerService.init();
    }

}
