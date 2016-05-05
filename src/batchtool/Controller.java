package batchtool;

import java.util.ArrayList;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Controller {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Construct available processes
		ObservableList<Line> availableprocesses = FXCollections.observableArrayList();
		// Construct Dash processes
		ObservableList<Line> dashprocesses = FXCollections.observableArrayList();
		
		// Reading available processes from txt file
		String allowablefile = "/home/max/Documents/JavaSoft/BatchController/AvailableProcesses.txt";
		availableprocesses = importprocess(allowablefile);
		
		
		//System.out.println("---------- Following Lines Available ----------");
		//printProcesses(availableprocesses);
		
		// Load Processes from text file
		String dashfile = "/home/max/Documents/JavaSoft/BatchController/MyDash.csv";
		dashprocesses = Dash.load(dashfile, availableprocesses);
		
		// Adding Process here
		String newProcessType = "WebBrowser";
		addLineFromType(newProcessType, availableprocesses, dashprocesses);

		System.out.println("---------- Following Lines in Dash ----------");
		printProcesses(dashprocesses);
		String dashfilesaved = "/home/max/Documents/JavaSoft/BatchController/MySavedDash.csv";
		
		
		// Save Processes to text file
		Dash.save(dashfilesaved, dashprocesses);
		
		//dashprocesses.get(0).execute();
		
		//System.out.println("---------- Following Lines Available ----------");
		//printProcesses(availableprocesses);
		
	}
	private static ObservableList<Line> importprocess(String allowablefile) {
		ObservableList<Line> alist = FXCollections.observableArrayList();
		// import allowable csv file here
		Path file = Paths.get(allowablefile);
		try (InputStream in = Files.newInputStream(file);
		    BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	// Specify the comment caractere #
		    	if (line.startsWith("#")){continue;}
		    	// Split the line with ','
		    	// field[0] = Process Label
		    	// field[1] = shell script path
		    	// field[2] = ArgLabel01
		    	// field[3] = ArgType01
		    	// field[4] = ArgLabel02
		    	// field[5] = ArgType02
		    	// field[6] = ArgLabelxx
		    	// field[7] = ArgTypexx
		    	String[] field = line.split(",");
		        //System.out.println(line);
		    	Line newLine = new Line();
		    	newLine.setComments("Comments");
		    	newLine.setDelay(0);
		    	newLine.setExLabel(field[0]);
		    	newLine.setExPath(field[1]);
		    	newLine.setIsActive(true);
		    	newLine.setThreadID("0");
		    	//int argumentNo = (field.length - 2) / 2;
		    	ObservableList<Argument> ListofArgument = FXCollections.observableArrayList();
		    	newLine.setListofArgument(ListofArgument);
		    	for (int i = 2; i < field.length;) {
		    		//System.out.println(field[i]);
		    		switch (field[i+1]) {
		    		case "string":
		    			Argument sArgument = new ArgString();
		    			sArgument.setData("Default value");
		    			sArgument.setDatalabel(field[i]);
		    			ListofArgument.add(sArgument);
		    			break;
		    		case "file":
		    			Argument fArgument = new ArgFile();
		    			fArgument.setData("Default value");
		    			fArgument.setDatalabel(field[i]);
		    			ListofArgument.add(fArgument);
		    			break;
		    		case "path":
		    			Argument pArgument = new ArgPath();
		    			pArgument.setData("Default value");
		    			pArgument.setDatalabel(field[i]);
		    			ListofArgument.add(pArgument);
		    			break;
		    		case "bool":
		    			Argument bArgument = new ArgBool();
		    			bArgument.setData("Default value");
		    			bArgument.setDatalabel(field[i]);
		    			ListofArgument.add(bArgument);
		    			break;
		    		}
		    		i += 2;
		    	}
		    	alist.add(newLine);
		    }
		} catch (IOException x) {
		    System.err.println(x);
		}
		return alist;
	}

	private static void printProcesses(ObservableList<Line> Lines) {
		for (Line l:Lines){
			l.print();
		}
	}
	
	private static void addLineFromType(String ExType, ObservableList<Line> From, ObservableList<Line> To) {
		if (searchLine(ExType, From) != null ) {
			To.add(searchLine(ExType, From));
		}
		else {
			System.out.println("---------- Process " + ExType + " Not Found ----------");
		}
	}
	
	private static Line searchLine(String ExType, ObservableList<Line> Lines) {
		for (Line l:Lines) {
			if (ExType.equals(l.getExLabel())) {
				return l.clone();
			}
		}
		return null;
	}
}
