package dashboardgui;

import java.io.*;
//import java.util.*;
import java.util.ArrayList;

public class TerminalLauncher {

    public static void exLinux(ArrayList<String> command) {

        try {
            PrintWriter writer = new PrintWriter("executionfile.sh", "UTF-8");
            for (String line : command) {
                writer.println(line);
                //System.out.println(line);
            }
            writer.close();
            @SuppressWarnings("unused")
            Process p = Runtime.getRuntime().exec("bash executionfile.sh");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void exWindows(ArrayList<String> command, WindowsLinux winlin) {

        try {
            
            PrintWriter writer = new PrintWriter(winlin.getWindir() +  File.separator +"executionfile.sh", "UTF-8");
            for (String line : command) {
                writer.println(line + "\n");
            }
            writer.close();
            @SuppressWarnings("unused")
            Process p = Runtime.getRuntime().exec(winlin.getWinexe());
            //
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }   
}
