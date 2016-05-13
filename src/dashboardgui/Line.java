package dashboardgui;

import java.io.File;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Line {

    // Private variables
    private String ExPath;
    private Double Delay = 0.0;
    private String ExLabel;
    private String ThreadID;
    private ObservableList<Argument> ListofArgument;
    private String Comments;
    private String IsActive = "true";
    private int ArgumentNo;

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

    public String isIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public void execute(String basepath, WindowsLinux winlin) {
        TerminalLauncher.ex(generatecommandlines(basepath, winlin));
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
        System.out.println("Is Active: " + IsActive);
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
        ClonedLine.setIsActive(this.IsActive);
        ClonedLine.setThreadID(this.ThreadID);
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
        if (winlin.isLinux()) {
            File executablefile = new File(this.getExPath());
            resultstring += executablefile.exists() + " -> Bash Script: " + executablefile.getAbsoluteFile() + "\n";
        }
        else {
            File executablefile = new File(WindowsLinux.converttowin(winlin, this.getExPath()));
            resultstring += executablefile.exists() + " -> Bash Script: " + executablefile.getAbsoluteFile() + "\n";
        }
        for (Argument arg : this.ListofArgument) {
            resultstring += arg.valid(basepath) + " -> " + arg.getDatalabel() + " (" + arg.getStyle() + "): " + arg.getData() + "\n";
        }
        resultstring += "\n------Bash Shell script-------\n";
        resultstring += this.dryrun(basepath, winlin);
        if (!winlin.isLinux()) {
            resultstring += "\n------Windows to Linux Mapping equivalence------\nWindows: ";
            resultstring += winlin.getWindir() + "\nLinux: ";
            resultstring += winlin.getLindir();
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
        subwindow.showAndWait();
        return null;
    }

    private ArrayList<String> generatecommandlines(String basepath, WindowsLinux winlin) {
        ArrayList<String> ExecutionLines = new ArrayList<String>();
        ExecutionLines.add("#!/bin/sh");
        if (Delay != 0) {
            ExecutionLines.add("sleep " + Delay);
        }
        String Args = "";
        for (Argument arg : ListofArgument) {
            String argstring = " \"" + arg.getAbsData(basepath) + "\"";
            if (!winlin.isLinux()) {
                argstring = WindowsLinux.converttolin(winlin, argstring);
            }
            Args += argstring;
        }
        ExecutionLines.add("bash " + ExPath + Args);
        return ExecutionLines;
    }
}
