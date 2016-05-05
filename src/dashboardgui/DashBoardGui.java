/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboardgui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
//import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import batchtool.*;
import java.io.File;
//import java.util.ArrayList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
//import javafx.geometry.Rectangle2D;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
//import javafx.stage.FileChooser.ExtensionFilter;
//import javafx.stage.Screen;
import javafx.util.Callback;

/**
 *
 * @author max
 */
public class DashBoardGui extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        // Initialize the Main Dash
        ObservableList<Line> MainList = FXCollections.observableArrayList();
        MainDash maindash = new MainDash(MainList);
        String AvailableProcessesFile = "/home/max/NetBeansProjects/DashBoardGui/AvailableProcesses.txt";
        maindash.setAvailableprocesses(AvailableProcess.load(AvailableProcessesFile));
        TableView<Line> dasboardtable;
        
// Name Column
        TableColumn<Line, Boolean> IsActiveLabel = new TableColumn("Is Active");
        IsActiveLabel.setMinWidth(100);
        IsActiveLabel.setCellValueFactory(new PropertyValueFactory<>("IsActive"));
// Name Column
        TableColumn<Line, String> Comments = new TableColumn("Comments");
        Comments.setMinWidth(200);
        Comments.setCellValueFactory(new PropertyValueFactory<>("Comments"));
        //ThreadID
        TableColumn<Line, String> ThreadID = new TableColumn("Thread ID");
        ThreadID.setMinWidth(100);
        ThreadID.setCellValueFactory(new PropertyValueFactory<>("ThreadID"));
        // Name Column
        TableColumn<Line, Double> DelayLabel = new TableColumn("Delay (sec)");
        DelayLabel.setMinWidth(100);
        DelayLabel.setCellValueFactory(new PropertyValueFactory<>("Delay"));
        // Name Column
        TableColumn<Line, String> ProcessLabel = new TableColumn("Process");
        ProcessLabel.setMinWidth(200);
        ProcessLabel.setCellValueFactory(new PropertyValueFactory<>("ExLabel"));
        
        // Set Argument Column
        TableColumn col_action_set = new TableColumn<>("Set Arguments");
        col_action_set.setMinWidth(200);
        col_action_set.setSortable(false);
        
        // Run Column
        TableColumn col_action_run = new TableColumn<>("Run");
        col_action_run.setMinWidth(100);
        col_action_run.setSortable(false);
        
        // Run Column
        TableColumn col_action_del = new TableColumn<>("Delete");
        col_action_del.setMinWidth(100);
        col_action_del.setSortable(false);
        
        
        col_action_set.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Line, Boolean>, ObservableValue<Boolean>>() {
 
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Line, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });
        col_action_set.setCellFactory(
                new Callback<TableColumn<Line, Boolean>, TableCell<Line, Boolean>>() {
 
            @Override
            public TableCell<Line, Boolean> call(TableColumn<Line, Boolean> p) {
                return new ButtonCell();
            }
         
        });
        
        dasboardtable = new TableView<>();
        dasboardtable.setItems(maindash.getDashprocesses());
        dasboardtable.setEditable(true);
        dasboardtable.getColumns().addAll(IsActiveLabel, Comments, ThreadID, DelayLabel, ProcessLabel, col_action_set, col_action_run, col_action_del);
        // All button here
        Button loadprojectbutton = new Button();
        loadprojectbutton.setText("Load Project Dash");
        Button addlinebutton = new Button();
        addlinebutton.setText("Add Process Line");
        Button saveprojectbutton = new Button();
        saveprojectbutton.setText("Save Project Dash");
        
        
        loadprojectbutton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Project Dash");
            File file = fileChooser.showOpenDialog(new Stage());
            ObservableList<Line> arraylist = Dash.load(file.getAbsolutePath(), maindash.getAvailableprocesses());
            maindash.setDashprocesses(arraylist);
            dasboardtable.setItems(maindash.getDashprocesses());
            //refresh_table(dasboardtable);
        });
        
        
        GridPane mainlayout = new GridPane();
        mainlayout.setPadding(new Insets(10, 10, 10, 10));
        mainlayout.setMinSize(300, 300);
        mainlayout.setVgap(5);
        mainlayout.setHgap(5);
        mainlayout.add(addlinebutton, 0, 0);
        mainlayout.add(loadprojectbutton, 1, 0);
        mainlayout.add(saveprojectbutton, 2, 0);
        mainlayout.add(dasboardtable,0 ,1, 10, 12);
        Scene mainwindow = new Scene(mainlayout, 1200, 400);
        
        // Maximize window size here
        //Screen screen = Screen.getPrimary();
        //Rectangle2D bounds = screen.getVisualBounds();
        //primaryStage.setX(bounds.getMinX());
        //primaryStage.setY(bounds.getMinY());
        //primaryStage.setWidth(bounds.getWidth());
        //primaryStage.setHeight(bounds.getHeight());
        
        primaryStage.setTitle("Linux Structural Dashboard");
        primaryStage.setScene(mainwindow);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        launch(args);
    }
    
    public static void refresh_table(TableView table){
        for (int i = 0; i < table.getColumns().size(); i++) {
    ((TableColumn)(table.getColumns().get(i))).setVisible(false);
    ((TableColumn)(table.getColumns().get(i))).setVisible(true);
    }
}
    private class ButtonCell extends TableCell<Line, Boolean> {
        final Button cellButton = new Button("Action");
         
        ButtonCell(){
             
            cellButton.setOnAction(new EventHandler<ActionEvent>(){
 
                @Override
                public void handle(ActionEvent t) {
                    // do something when button clicked
                    System.out.println("asx");
                }
            });
        }
}
}
