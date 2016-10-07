/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboardgui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author max
 */
public class WindowsLinux {

    private String windir;
    private String lindir;
    private String winexe;
    private boolean linux;
    private String lsdpath;

    public String getLsdpath() {
        return lsdpath;
    }

    public void setLsdpath(String lsdpath) {
        this.lsdpath = lsdpath;
    }

    public WindowsLinux(String windir, String lindir, boolean linux, String winexe, String lsdpath) {
        this.windir = windir;
        this.lindir = lindir;
        this.linux = linux;
        this.winexe = winexe;
        this.lsdpath = lsdpath;
    }

    public String getWinexe() {
        return winexe;
    }

    public void setWinexe(String winexe) {
        this.winexe = winexe;
    }

    public boolean isLinux() {
        return linux;
    }

    public void setLinux(boolean linux) {
        this.linux = linux;
    }

    public String getWindir() {
        return windir;
    }

    public void setWindir(String windir) {
        this.windir = windir;
    }

    public String getLindir() {
        return lindir;
    }

    public void setLindir(String lindir) {
        this.lindir = lindir;
    }

    public static String[] load(Path setupfile) {
        String[] paths = new String[4];
        try (InputStream in = Files.newInputStream(setupfile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            int j = 0;
            while ((line = reader.readLine()) != null) {
                // Specify the comment caractere #
                if (line.startsWith("#")) {
                    continue;
                }
                
                switch (j) {
                    case 0:
                        paths[j] = line;
                        break;
                    case 1:
                        paths[j] = line;
                        break;
                    case 2:
                        paths[j] = line;
                        break;
                    case 3:
                        paths[j] = line;
                        return paths;
                    default:
                        break;
                }
                j++;
            }
        } catch (IOException x) {
            System.err.println(x);
        }
        return null;
    }
    public static String converttolin(WindowsLinux winlin, String in) {
                String out = in.replace(winlin.getWindir(), winlin.getLindir());
                out = out.replace("\\", "/");
        return out;
    }
    public static String converttowin(WindowsLinux winlin, String in) {
                String out = in.replace(winlin.getLindir(), winlin.getWindir());
                out = out.replace("/","\\");
        return out;
    }

    public static void copyxsfile(WindowsLinux winlin) {
        Path readfile = Paths.get(winlin.getWinexe());
        try (InputStream in = Files.newInputStream(readfile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            PrintWriter writer = new PrintWriter("execution.xs", "UTF-8");
            while ((line = reader.readLine()) != null) {
                // Specify the comment caractere #
                if (line.length() > 7 && line.startsWith("Command=")) {
                    writer.print("Command=/usr/bin/xterm -sb -ls -e \"cd ${LSDPATH};executionfile.sh ${LSDPATH}/bashfiles &;wait;\" ; \n");
                } else {
                    writer.print(line + "\n");
                    
                }
            }
            writer.close();
        } catch (IOException x) {
            System.err.println(x);
        }
        
    }
}
