package org.recompyle.listeners;

import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.wm.IdeFrame;
import org.jetbrains.annotations.NotNull;

public class ApplicationActivation implements ApplicationActivationListener {

    @Override
    public void applicationActivated(@NotNull IdeFrame ideFrame) {
        ApplicationActivationListener.super.applicationActivated(ideFrame);
//        Logger("applicationActivated");
    }

//    ProjectManager.TOPIC
}
