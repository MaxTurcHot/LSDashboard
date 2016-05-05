package batchtool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Dash {
	
	public static void save(String filepath, ObservableList<Line> lines) {
		try {
			File file = new File(filepath);
			// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			// Split the line with ','
	    	// field[0] = Process Label
	    	// field[1] = Is active
	    	// field[2] = comment
	    	// field[3] = Thread ID
	    	// field[4] = Process Executable Path
	    	// field[5] = Delay
	    	// field[6] = Argument01
	    	// field[7] = Argument02
	    	// field[8] = Argument03
			bw.write("# Process Label, Is Active, Comment, Thread ID, shell script path, Delay (sec) ArgLabel01, ArgType01, ArgLabel02, ArgType02, ..." + "\n");
			for (Line l:lines) {
				String content = l.getExLabel() + ",";
				content += l.isIsActive() + ",";
				content += l.getComments() + ",";
				content += l.getThreadID() + ",";
				content += l.getExPath() + ",";
				content += l.getDelay() + ",";
				for (int i = 0; i < l.getListofArgument().size(); i++) {
					content += l.getListofArgument().get(i).getData() + ",";
				}
				System.out.println(content);
				bw.write(content + "\n");
			}
			
			bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static ObservableList<Line> load(String filepath, ObservableList<Line> availablelist ){
		
		ObservableList<Line> alist = FXCollections.observableArrayList();
		// import allowable csv file here
		Path file = Paths.get(filepath);
		try (InputStream in = Files.newInputStream(file);
		    BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	// Specify the comment caractere #
		    	if (line.startsWith("#")){continue;}
		    	Line newLine = new Line();
		    	// Split the line with ','
		    	// field[0] = Process Label
		    	// field[1] = Is active
		    	// field[2] = comment
		    	// field[3] = Thread ID
		    	// field[4] = Process Executable Path
		    	// field[5] = Delay
		    	// field[6] = Argument01
		    	// field[7] = Argument02
		    	// field[8] = Argument03
		    	String[] field = line.split(",");
		    	Boolean exist = false;
		    	Boolean argmatch = false;
		    	int argumentno = field.length - 6;
		    	//System.out.println(argumentno);
		    	// Determine is Process exits in available processes
		    	for (int i = 0; i < availablelist.size(); i++) {
		    		if (field[0].equals(availablelist.get(i).getExLabel())) {
		    			exist = true;
		    			// Determine is number of argument == number of argument in Available Processes
		    			if (argumentno == availablelist.get(i).getListofArgument().size()) {
		    				argmatch = true;
		    				// Need to clone the line object in order to have the good argument object
		    				newLine = availablelist.get(i).clone();
		    			}
		    			break;
		    		}
		    	}
		    	if (exist && argmatch) {
		    		//System.out.println("exist and argument match");
		    		//  Process found, we can import the line in the dashboard
		    		newLine.setComments(field[2]);
		    		newLine.setDelay(Integer.parseInt(field[5]));
		    		newLine.setExLabel(field[0]);
		    		newLine.setExPath(field[4]);
		    		newLine.setIsActive(Boolean.parseBoolean(field[1]));
		    		newLine.setThreadID(field[3]);
		    		// Go thru all arguments and populate argument object
		    		for (int i = 6; i < field.length; i++) {
		    			newLine.getListofArgument().get(i - 6).setData(field[i]);
			    	}
		    		//newLine.print();
		    		alist.add(newLine);
		    	}
		    	else if (exist && !argmatch) {
		    		System.out.println("exist but argument doesn't match");
		    	}
		    	else {
		    		System.out.println("doesn't exist");
		    	}
		    }
		} catch (IOException x) {
		    System.err.println(x);
		}
		return alist;
	}

}
