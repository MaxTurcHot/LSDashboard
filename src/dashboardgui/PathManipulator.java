/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboardgui;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author max
 */
public class PathManipulator {
    
    public static String relative(File basepath, File fullpath) {
        String basepath_string = basepath.getPath();
        String fullpath_string = fullpath.getPath();
        Path basepath_path = Paths.get(basepath_string);
        Path fullpath_path = Paths.get(fullpath_string);
        Path relativepath_path = basepath_path.relativize(fullpath_path);
        return  relativepath_path.toString();
    }
}
