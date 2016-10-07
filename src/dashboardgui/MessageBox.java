package dashboardgui;

import javafx.scene.control.Alert;

/**
 * Created by maxime on 03/09/16.
 */
public class MessageBox {
    public static void show(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
