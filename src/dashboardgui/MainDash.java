/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboardgui;

import batchtool.Line;
import java.util.ArrayList;
import javafx.collections.ObservableList;

/**
 *
 * @author max
 */
public class MainDash {

    public MainDash(ObservableList<Line> dashprocesses) {
        this.dashprocesses = dashprocesses;
    }

    private ObservableList<Line> availableprocesses;
    private ObservableList<Line> dashprocesses;

    public ObservableList<Line> getAvailableprocesses() {
        return availableprocesses;
    }
    public void setAvailableprocesses(ObservableList<Line> availableprocesses) {
		this.availableprocesses = availableprocesses;
	}

    public ObservableList<Line> getDashprocesses() {
        return dashprocesses;
    }
    public void setDashprocesses(ObservableList<Line> dashprocesses) {
		this.dashprocesses = dashprocesses;
	}

}
