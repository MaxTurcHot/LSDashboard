package dashboardgui;

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
import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Dash {

    public static void save(String filepath, ObservableList<Line> lines, String BasePath) {
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
            // field[1] = comment
            // field[2] = Thread ID
            // field[3] = Delay
            // field[4] = Relative Mode
            // field[5] = Argument01
            // field[6] = Argument02
            // field[7] = Argument03

            bw.write("# first line is the absolute base path of the project\n");
            bw.write(BasePath + "\n");
            bw.write("# Process Label, Comment, Thread ID, Delay (sec), Relative mode,ArgLabel01, ArgType01, ArgLabel02, ArgType02, ..." + "\n");
            for (Line l : lines) {
                String content = l.getExLabel() + ",";
                content += l.getComments() + ",";
                content += l.getThreadID() + ",";
                //content += l.getExPath() + ",";
                content += l.getDelay() + ",";
                content += l.isRelativeMode() + ",";
                for (int i = 0; i < l.getListofArgument().size(); i++) {
                    content += l.getListofArgument().get(i).getData() + ",";
                }
                System.out.println(content);
                bw.write(content + "\n");
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load(String filepath, MainDash maindash) {

        ObservableList<Line> availablelist = maindash.getAvailableprocesses();
        ObservableList<Line> alist = FXCollections.observableArrayList();
        // import allowable csv file here
        Path file = Paths.get(filepath);
        try (InputStream in = Files.newInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            int j = 0;
            while ((line = reader.readLine()) != null) {
                // Specify the comment caractere #
                if (line.startsWith("#")) {
                    continue;
                }
                j++;
                if (j == 1) {
                    String[] field = line.split(",");

                    maindash.setBasePath(field[0]);
                }
                Line newLine = new Line();
                // Split the line with ','
                // field[0] = Process Label
                // field[1] = comment
                // field[2] = Thread ID
                // field[3] = Delay
                // field[4] = Relative Mode
                // field[5] = Argument01
                // field[6] = Argument02
                // field[7] = Argument03
                String[] field = line.split(",");
                Boolean exist = false;
                Boolean argmatch = false;
                int argumentno = field.length - 5;
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
                    newLine.setComments(field[1]);
                    newLine.setDelay(Double.parseDouble(field[3]));
                    newLine.setExLabel(field[0]);
                    //newLine.setExPath(field[3]);
                    newLine.setThreadID(field[2]);
                    newLine.setRelativeMode(Boolean.valueOf(field[4]));
                    //newLine.setRelativeMode(field[4]);
                    // Go thru all arguments and populate argument object
                    for (int i = 5; i < field.length; i++) {
                        newLine.getListofArgument().get(i - 5).setData(field[i]);
                    }
                    //newLine.print();
                    alist.add(newLine);
                } else if (exist && !argmatch) {
                    System.out.println("exist but argument doesn't match");
                } else {
                    System.out.println("doesn't exist");
                }
            }
        } catch (IOException x) {
            System.err.println(x);
        }
        maindash.setDashprocesses(alist);
        //return alist;
    }

}
