package dashboardgui;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ArgFile extends Argument {

    private String style = "file";

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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("LSD - File Selector");
        File file = fileChooser.showOpenDialog(new Stage());
        this.setData(file.getAbsolutePath());
    }

    @Override
    public void selector(String basepath) {
        File basepathfile = new File(basepath);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("LSD - File Selector");
        if (basepathfile.exists())
        {
            fileChooser.setInitialDirectory(basepathfile);
        }
        else
        {
            MessageBox.show("Warning", "IO warning","Base path doesn't exist");
        }
        File file = fileChooser.showOpenDialog(new Stage());
        String relativepath = PathManipulator.relative(basepathfile, file);
        this.setData(relativepath);
    }

    @Override
    public void selector(String basepath, Boolean donotexist) {
        File basepathfile = new File(basepath);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("LSD - New File Selector");
        fileChooser.setInitialDirectory(basepathfile);
        File file = fileChooser.showSaveDialog(new Stage());
        String relativepath = PathManipulator.relative(basepathfile, file);
        this.setData(relativepath);

    }
    
    @Override
    public boolean valid(String basepath) {
        File filepath = new File(basepath + File.separator + this.getData());
        return filepath.exists();
    }
}
