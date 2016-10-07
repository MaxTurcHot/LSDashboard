/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboardgui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author max
 */
public class AlertBox {
    
    public static void display(String title, String message, Image dashboardimage) {
        Stage subwindow = new Stage();
        subwindow.initModality(Modality.APPLICATION_MODAL);
        subwindow.setTitle(title);
        subwindow.setMinWidth(250);
        subwindow.getIcons().add(dashboardimage);
        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("ok");
        closeButton.setOnAction(e -> subwindow.close());
        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
        Scene subscene = new Scene(layout);
        subwindow.setScene(subscene);
        subwindow.showAndWait();
    }
    
}
