package org.recompyle.listeners;

import com.intellij.xdebugger.breakpoints.XBreakpoint;
import com.intellij.xdebugger.breakpoints.XBreakpointListener;
import org.jetbrains.annotations.NotNull;
import org.recompyle.services.breakpoint.BreakpointManager;

public class BreakpointListener implements XBreakpointListener<XBreakpoint<?>> {

    @Override
    public void breakpointAdded(@NotNull XBreakpoint breakpoint) {
        BreakpointManager.sendUpdateWithBreakpoint(breakpoint);
    }

    @Override
    public void breakpointRemoved(@NotNull XBreakpoint breakpoint) {
        BreakpointManager.sendUpdateWithBreakpoint(breakpoint);

    }

    @Override
    public void breakpointChanged(@NotNull XBreakpoint breakpoint) {
        BreakpointManager.sendUpdateWithBreakpoint(breakpoint);
    }

}
