package org.recompyle.services;

import org.recompyle.toolWindow.DebugToolWindow;

public class DebugService {

    static Boolean enableLog = false;
   public static void Logger(String text){
        if(enableLog){
            System.out.println(text);
            DebugToolWindow.addLog(text);
        }
    }

   public static void Logger(Object object){
        if(enableLog){
            System.out.println(object);
            if(object!=null){
                DebugToolWindow.addLog(object.toString());
            }
        }
    }
}
