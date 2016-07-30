package dashboardgui;

import java.io.*;

public class TerminalLauncher {

    public static void exLinux() {

        try {
            
            @SuppressWarnings("unused")
            Process p = Runtime.getRuntime().exec("bash executionfile.bash");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void exWindows(WindowsLinux winlin) {

        try {
            WindowsLinux.copyxsfile(winlin);
            @SuppressWarnings("unused")
            Process p = Runtime.getRuntime().exec("cmd /c execution.xs");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    public static void directoryWindows(String path) {

        try {
            @SuppressWarnings("unused")
            Process p = Runtime.getRuntime().exec("cmd /c explorer " + path);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    
}
