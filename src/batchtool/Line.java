package batchtool;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Line {
	
	// Private variables
	private String ExPath;
	private int Delay = 0;
	private String ExLabel;
	private String ThreadID;
	private ObservableList<Argument> ListofArgument;
	private String Comments;
	private boolean IsActive = true;
	
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
	public int getDelay() {
		return Delay;
	}
	public void setDelay(int delay) {
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
	public boolean isIsActive() {
		return IsActive;
	}
	public void setIsActive(boolean isActive) {
		IsActive = isActive;
	}
	void execute() {
		ArrayList<String> ExecutionLines = new ArrayList<String>();
		ExecutionLines.add("#!/bin/sh");
		if (Delay != 0){
			ExecutionLines.add("sleep " + Delay);
		}
		String Args = "";
		for (Argument arg:ListofArgument) {
			Args += " " + arg.getData();
		}
		ExecutionLines.add("bash " + ExPath + Args);
		TerminalLauncher.ex(ExecutionLines);
	}
	
	public void print() {
		System.out.println("Comment: " + Comments);
		System.out.println("Is Active: " + IsActive);
		System.out.println("Thread ID: " + ThreadID);
		System.out.println("Delay: " + Delay + " sec");
		System.out.println("Executable type: " + ExLabel);
		System.out.println("Executable path: " + ExPath);
		int i = 1;
		for (Argument arg:ListofArgument) {
			System.out.println("Argument "+ i + ": "+ arg.getData() + " (Type: " + arg.getStyle() + ")");
			i++;
		}
		System.out.println("---------");
	}

	public Line clone() {
		
		Line ClonedLine = new Line();
		ClonedLine.setComments(this.Comments);
		ClonedLine.setDelay(this.Delay );
		ClonedLine.setExLabel(this.ExLabel );
		ClonedLine.setExPath(this.ExPath);
		ClonedLine.setIsActive(this.IsActive);
		ClonedLine.setThreadID(this.ThreadID);
		ObservableList<Argument> ListofArgument = FXCollections.observableArrayList();
		for (Argument arg: this.ListofArgument) {
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
}
