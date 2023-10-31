package org.recompyle.toolWindow;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;

public class DebugToolWindow {

    public static JTextArea area = new JTextArea();
    public static JPanel myToolWindowContent;

    public DebugToolWindow(ToolWindow toolWindow) {
        System.out.println("DebugToolWindow constructor");

        myToolWindowContent = new JPanel();
        myToolWindowContent.setLayout(new BorderLayout(0, 0));

        JBScrollPane scroller = new JBScrollPane();
        scroller.setEnabled(true);
        scroller.setVerticalScrollBarPolicy(20);
        myToolWindowContent.add(scroller, BorderLayout.CENTER);

        area.setEditable(false);
        area.append("Init Debug Tool Window");

        scroller.setViewportView(area);
    }


    public JPanel getContent() {
        return myToolWindowContent;
    }

    public static void addLog(String text) {
//        System.out.println("DEBUG add log");
        area.append("\n " + text);

        if (myToolWindowContent != null) {
            myToolWindowContent.validate();
            myToolWindowContent.repaint();
        }
    }

}
