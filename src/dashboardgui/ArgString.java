package dashboardgui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ArgString extends Argument {

    private String style = "string";

    @Override
    public String getStyle() {
        return style;
    }

    @Override
    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public void selector(String basepath) {
        GridPane sublayout = new GridPane();
        sublayout.setVgap(5);
        sublayout.setHgap(5);
        Button acceptselection = new Button();
        acceptselection.setText("ok");
        acceptselection.setMinWidth(355);

        Label label = new Label(this.getDatalabel() + ":");
        label.setMinWidth(150);
        TextField textField = new TextField();
        textField.setMinWidth(200);
        // Set initial value to 
        textField.setText(this.getData());
        
        sublayout.add(label, 1, 1);
        sublayout.add(textField, 2, 1);
        sublayout.add(acceptselection, 1, 2, 3, 2);
        
        
        Scene subscene = new Scene(sublayout, 370, 70);
        Stage subwindow = new Stage();
        subwindow.setScene(subscene);
        subwindow.setTitle("LSD - String Selector");
        acceptselection.setOnAction(f -> {
            this.setData(textField.getText());
            subwindow.close();
            //return textField.getText();
        });

        
        subwindow.showAndWait();
        //return textField.getText(); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean valid(String basepath) {        
            return !this.getData().contains(" ");
        }

}
