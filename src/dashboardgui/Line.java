package dashboardgui;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Line {

    // Private variables
    private String ExPath;
    private Double Delay = 0.0;
    private String ExLabel;
    private String ThreadID;
    private ObservableList<Argument> ListofArgument;
    private String Comments;
    private int ArgumentNo;
    private boolean relativeMode = false;

    private int status = 0;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public boolean isRelativeMode() {
        return relativeMode;
    }

    public void setRelativeMode(boolean relativeMode) {
        this.relativeMode = relativeMode;
    }

    public int getArgumentNo() {

        return this.ListofArgument.size();
    }

    public void setArgumentNo(int ArgumentNo) {
        this.ArgumentNo = ArgumentNo;
    }

    // Constructor
    public Line() {
        // TODO Auto-generated constructor stub
        //ArrayList<Argument> ListofArgument = new ArrayList<Argument>();
    }

    // Public variables
    public String getExPath() {
        return ExPath;
    }

    public void setExPath(String exPath) {
        ExPath = exPath;
    }

    public Double getDelay() {
        return Delay;
    }

    public void setDelay(Double delay) {
        Delay = delay;
    }

    public String getExLabel() {
        return ExLabel;
    }

    public void setExLabel(String exLabel) {
        ExLabel = exLabel;
    }

    public String getThreadID() {
        return ThreadID;
    }

    public void setThreadID(String threadID) {
        ThreadID = threadID;
    }

    public ObservableList<Argument> getListofArgument() {
        return ListofArgument;
    }

    public void setListofArgument(ObservableList<Argument> listofArgument) {
        ListofArgument = listofArgument;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public void write(String basepath, WindowsLinux winlin, Boolean batch) {
        String filename;
        String pathstring;
        if (winlin.isLinux()) {
            pathstring = "bashfiles";
        } else {
            //pathstring = winlin.getWindir() + File.separator + "bashfiles";
            pathstring = winlin.getLsdpath() + File.separator + "bashfiles";
        }
        if (batch) {
            filename = pathstring + File.separator + ThreadID + "_lsd.bash";
        } else {
            filename = pathstring + File.separator + "_" + ThreadID + "_lsd.bash";
        }

        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            for (String line : generatecommandlines(basepath, winlin)) {
                writer.print(line + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String dryrun(String basepath, WindowsLinux winlin) {
        //TerminalLauncher.ex(generatecommandlines());
        String commandlines = new String();
        for (String s : generatecommandlines(basepath, winlin)) {
            commandlines += s + "\n";
        }
        return commandlines;
    }

    public void print() {
        System.out.println("Comment: " + Comments);
        System.out.println("Thread ID: " + ThreadID);
        System.out.println("Delay: " + Delay + " sec");
        System.out.println("Executable type: " + ExLabel);
        System.out.println("Executable path: " + ExPath);
        int i = 1;
        for (Argument arg : ListofArgument) {
            System.out.println("Argument " + i + ": " + arg.getData() + " (Type: " + arg.getStyle() + ")");
            i++;
        }
        System.out.println("---------");
    }

    public Line clone() {

        Line ClonedLine = new Line();
        ClonedLine.setComments(this.Comments);
        ClonedLine.setDelay(this.Delay);
        ClonedLine.setExLabel(this.ExLabel);
        ClonedLine.setExPath(this.ExPath);
        ClonedLine.setThreadID(this.ThreadID);
        ClonedLine.setRelativeMode(this.isRelativeMode());
        ObservableList<Argument> ListofArgument = FXCollections.observableArrayList();
        for (Argument arg : this.ListofArgument) {
            switch (arg.getStyle()) {
                case "string":
                    Argument sArgument = new ArgString();
                    sArgument.setData(arg.getData());
                    sArgument.setStyle(arg.getStyle());
                    sArgument.setDatalabel(arg.getDatalabel());
                    ListofArgument.add(sArgument);
                    break;
                case "file":
                    Argument fArgument = new ArgFile();
                    fArgument.setData(arg.getData());
                    fArgument.setStyle(arg.getStyle());
                    fArgument.setDatalabel(arg.getDatalabel());
                    ListofArgument.add(fArgument);
                    break;
                case "path":
                    Argument pArgument = new ArgPath();
                    pArgument.setData(arg.getData());
                    pArgument.setStyle(arg.getStyle());
                    pArgument.setDatalabel(arg.getDatalabel());
                    ListofArgument.add(pArgument);
                    break;
                case "bool":
                    Argument bArgument = new ArgBool();
                    bArgument.setData(arg.getData());
                    bArgument.setStyle(arg.getStyle());
                    bArgument.setDatalabel(arg.getDatalabel());
                    ListofArgument.add(bArgument);
                    break;
            }

        }
        ClonedLine.setListofArgument(ListofArgument);

        return ClonedLine;
    }

    public String valid(String basepath, WindowsLinux winlin) {
        File basefile = new File(basepath);

        String resultstring = basefile.exists() + " -> Base path: " + basefile.getAbsoluteFile() + "\n";
        /*if (winlin.isLinux()) {
            File executablefile = new File(this.getExPath());
            resultstring += executablefile.exists() + " -> Bash Script: " + executablefile.getAbsoluteFile() + "\n";
        }
        else {
            File executablefile = new File(WindowsLinux.converttowin(winlin, this.getExPath()));
            resultstring += executablefile.exists() + " -> Bash Script: " + executablefile.getAbsoluteFile() + "\n";
        }*/
        resultstring += !this.getThreadID().contains("_") + "-> ThreadID: " + this.getThreadID() + "\n";
        for (Argument arg : this.ListofArgument) {
            resultstring += arg.valid(basepath) + " -> " + arg.getDatalabel() + " (" + arg.getStyle() + "): " + arg.getData() + "\n";
        }
        if (relativeMode) {
            resultstring += "\n------Bash Shell script-------Relative path mode (relative to 1st argument)-------\n";
        }
        else {
            resultstring += "\n------Bash Shell script-------Full path mode-------\n";
        }
        resultstring += this.dryrun(basepath, winlin);
        if (!winlin.isLinux()) {
            resultstring += "\n------Windows to Linux Mapping equivalence------\nWindows: ";
            resultstring += winlin.getWindir() + "\nLinux: ";
            resultstring += winlin.getLindir();
            resultstring += "\n------Linux LSDPATH variable should point at the following directory------\n";
            resultstring += winlin.getLsdpath();
        }
        // Window to display result of validation
        TextArea valresults = new TextArea();
        valresults.setPrefRowCount(15);
        valresults.setPrefColumnCount(100);
        valresults.setWrapText(true);
        valresults.setPrefWidth(890);
        valresults.setText(resultstring);
        GridPane sublayout = new GridPane();
        sublayout.setVgap(5);
        sublayout.setHgap(5);
        sublayout.add(valresults, 1, 1);
        Scene subscene = new Scene(sublayout, 900, 300);
        Stage subwindow = new Stage();
        subwindow.setScene(subscene);
        subwindow.setTitle("LSD - " + this.ExLabel + ": " + this.getComments());
        subwindow.initModality(Modality.APPLICATION_MODAL);
        subwindow.showAndWait();
        return null;
    }

    private ArrayList<String> generatecommandlines(String basepath, WindowsLinux winlin) {
        ArrayList<String> ExecutionLines = new ArrayList<String>();
        //ExecutionLines.add("#!/bin/sh");
        String commandstring = "";
        if (Delay != 0) {
            commandstring += "sleep " + Delay.intValue() + ";";
        }
        String Args = "";
        int index = 0;
        String linuxbasepath = "";
        for (Argument arg : ListofArgument) {
            String argstring = " \"" + arg.getAbsData(basepath) + "\"";
            if (!winlin.isLinux()) {
                argstring = WindowsLinux.converttolin(winlin, argstring);
            }
            // If want, we can everything relative to the run path
            if (this.isRelativeMode()) {
                if (index == 0) {
                    argstring = " " + cleanpath(argstring);
                    linuxbasepath = argstring;
                }
                else if (!"string".equals(arg.getStyle()) && !"bool".equals(arg.getStyle())) {
                    // clean path remove all .. from the path
                    argstring = cleanpath(argstring);
                    // getrelativepath returns the relative path of argstring wrt the select directory (which is argument[0])
                    argstring = getrelativepath(argstring, linuxbasepath);
                }
                index++;
            }
            Args += argstring;
        }
        commandstring += ExPath + Args;
        ExecutionLines.add(commandstring);
        return ExecutionLines;
    }

    private static String getrelativepath(String fullpath, String basepath) {
        String relativepath = new String();
        relativepath = " \"";
        fullpath = fullpath.replace("\"", "");
        basepath = basepath.replace("\"", "");
        // For both path, we need to remove the "

        String[] fparray = fullpath.split("/");
        List<String> fplist = Arrays.asList(fparray);

        String[] bparray = basepath.split("/");
        List<String> bplist = Arrays.asList(bparray);
        //List<Integer> indexlist = new ArrayList<>();
        // 0 = means
        // 1 = it corresponds
        //
        int count = 0;
        int n = Math.min(bplist.size(), fplist.size());
        for (int i = 0; i < n; i++) {
            // Look if the path corresponds
            if (bplist.get(i).equals(fplist.get(i))) {
                // If it corresponds, we count how many sub path to remove
                count++;
            }
        }
        // We now remove all corresponding subpath
        bplist = bplist.subList(count, bplist.size());
        fplist = fplist.subList(count, fplist.size());

        // We need now to connect both path, start with the backs
        for (int i = 0; i < bplist.size(); i++) {
            relativepath += "../";
        }
        // Then we add the remaining of the full path
        for (int i = 0; i < fplist.size(); i++) {
            relativepath += fplist.get(i);
            if (i != fplist.size() - 1) {
                relativepath += "/";
            }
            else
            {
                relativepath += "\"";
            }
        }

        return relativepath;
    }
    
    private static String cleanpath(String path){
        String cleanedpath = "";
        path = path.replace("\"", "");
        String[] parray = path.split("/");
        List<String> plist = Arrays.asList(parray);
        List<String> newlist = new ArrayList<>();
        int i = 0;
        if (!plist.contains("..")) {
            return path;
        }
        for (String s: plist){
            if (s.equals("..")) {
                i--;
                newlist.remove(i);
            }
            else {
                newlist.add(s);
                i++;
            }
        }
        cleanedpath += "\"";
        for (int j = 0; j < newlist.size(); j++) {
            if (!" ".equals(newlist.get(j))) {
                cleanedpath += "/" + newlist.get(j);
            }
        }
        cleanedpath += "\"";
        return cleanedpath;
    }
}
