package dashboardgui;

import java.io.File;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ArgPath extends Argument {

    private String style = "path";

    @Override
    public String getStyle() {
        return style;
    }
    
    @Override
    public String getAbsData(String basepath) {
        File fullpath = new File(basepath + File.separator + this.getData());
		return fullpath.getAbsolutePath();
	}

    @Override
    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public void selector() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("LSD - Directory Selector");
        File file = directoryChooser.showDialog(new Stage());
        this.setData(file.getAbsolutePath());
        //return file.getAbsolutePath(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void selector(String basepath) {
        File basepathfile = new File(basepath);

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("LSD - Directory Selector");
        if (basepathfile.exists())
        {
            directoryChooser.setInitialDirectory(basepathfile);
        }
        else
        {
            MessageBox.show("Warning", "IO warning","Base path doesn't exist");
        }
        File file = directoryChooser.showDialog(new Stage());
        //directoryChooser.setInitialDirectory(basepathfile);
        String relativepath = PathManipulator.relative(basepathfile, file);

        this.setData(relativepath);

        //this.setData(file.getAbsolutePath());
        //return file.getAbsolutePath(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean valid(String basepath) {
        File argpath = new File(basepath + File.separator + this.getData());
        return argpath.exists();
    }

}
