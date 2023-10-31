package org.recompyle.actions;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.recompyle.services.Api.initForm;
import static org.recompyle.services.Api.sendReq;
import static org.recompyle.services.DebugService.Logger;


public class SelectFile extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {

    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Map<String, String> form = initForm("select-file", e.getData(CommonDataKeys.EDITOR));
        Logger("select file ");
        sendReq(e.getProject(), form);

//        BreakpointsList.getBps();
    }


}

