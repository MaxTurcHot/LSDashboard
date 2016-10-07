/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboardgui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author max
 */
public class SelectProcess {
    
    public static void window(MainDash Dash, Image dashboardimage) {

        ObservableList<String> availablechoices = FXCollections.observableArrayList();
        for (Line l : Dash.getAvailableprocesses()) {
            availablechoices.add(l.getExLabel());
        }

        GridPane sublayout = new GridPane();
        sublayout.setVgap(5);
        sublayout.setHgap(5);
        Button acceptselection = new Button();
        acceptselection.setText("ok");
        acceptselection.setMinWidth(200);
        acceptselection.setPrefWidth(800);

        ChoiceBox cb = new ChoiceBox();
        cb.setItems(availablechoices);
        cb.getSelectionModel().selectFirst();
        cb.setPrefWidth(800);
        cb.setMinWidth(200);
        sublayout.add(cb, 0, 0);
        sublayout.add(acceptselection, 0, 1);
        Scene subscene = new Scene(sublayout, 200, 60);
        Stage subwindow = new Stage();
        subwindow.setScene(subscene);
        subwindow.setMinWidth(200);
        subwindow.setHeight(100);
        subwindow.setMaxHeight(100);
        subwindow.getIcons().add(dashboardimage);
        subwindow.setTitle("LSD - Select Process");
        subwindow.initModality(Modality.APPLICATION_MODAL);

        acceptselection.setOnAction(f -> {
            String selection = cb.getSelectionModel().getSelectedItem().toString();
            for (Line l : Dash.getAvailableprocesses()) {
                if (l.getExLabel().equals(selection)) {
                    //System.out.println("found");
                    Dash.addline(l.clone());
                    break;
                }
            }
            subwindow.close();
            //refresh_table(dasboardtable);
        });
        subwindow.showAndWait();
    }
}
