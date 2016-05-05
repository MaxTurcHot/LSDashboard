/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboardgui;

import batchtool.ArgBool;
import batchtool.ArgFile;
import batchtool.ArgPath;
import batchtool.ArgString;
import batchtool.Argument;
import batchtool.Line;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author max
 */
public class AvailableProcess {

    public static ObservableList<Line> load(String allowablefile) {
        System.out.println(allowablefile);
        ObservableList<Line> alist = FXCollections.observableArrayList();
        // import allowable csv file here
        Path file = Paths.get(allowablefile);
        try (InputStream in = Files.newInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                
                // Specify the comment caractere #
                if (line.startsWith("#")) {
                    continue;
                }
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
                    switch (field[i + 1]) {
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
}
