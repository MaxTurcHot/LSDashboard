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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//import java.util.ArrayList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
//import javafx.geometry.Rectangle2D;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
//import javafx.stage.FileChooser.ExtensionFilter;
//import javafx.stage.Screen;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.NumberStringConverter;
import javax.swing.ListSelectionModel;

/**
 *
 * @author max
 */
public class DashBoardGui extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        Stage window;
        
        // Initialize the Main Dash
        ObservableList<Line> MainList = FXCollections.observableArrayList();
        MainDash maindash = new MainDash(MainList);
        //System.out.println();
        //String AvailableProcessesFile = "/home/max/NetBeansProjects/DashBoardGui/AvailableProcesses.txt";
        String AvailableProcessesFile = "AvailableProcesses.txt";
        maindash.setAvailableprocesses(AvailableProcess.load(AvailableProcessesFile));
        TableView<Line> dasboardtable;

        
        ////////////////////////////////////////
        /////// COLUMN DEFINITION //////////////
        ////////////////////////////////////////
        
        
        // Is Active Column
        TableColumn<Line, String> IsActiveLabel = new TableColumn("Is Active");
        IsActiveLabel.setMinWidth(100);
        IsActiveLabel.setEditable(true);
        IsActiveLabel.setCellValueFactory(new PropertyValueFactory<>("IsActive"));
        IsActiveLabel.setCellFactory(TextFieldTableCell.forTableColumn());
        IsActiveLabel.setOnEditCommit(new EventHandler<CellEditEvent<Line, String>>() {
            @Override
            public void handle(CellEditEvent<Line, String> t) {
                //
                List<String> istrue = Arrays.asList("TRUE","True","true","1","on","ON","active");
                List<String> isfalse = Arrays.asList("FALSE","False","false","0","OFF","off","inactive");
                boolean containedtrue = istrue.contains(t.getNewValue());
                boolean containedfalse = isfalse.contains(t.getNewValue());
                if (containedtrue) {
                    ((Line) t.getTableView().getItems().get(t.getTablePosition().getRow())).setIsActive("TRUE");
                }
                else if (containedfalse) {
                    ((Line) t.getTableView().getItems().get(t.getTablePosition().getRow())).setIsActive("FALSE");
                }
                
                //
            }
        }
        );
        // Comments Column
        TableColumn<Line, String> Comments = new TableColumn("Comments");
        Comments.setMinWidth(200);
        Comments.setEditable(true);
        Comments.setCellValueFactory(new PropertyValueFactory<>("Comments"));
        Comments.setCellFactory(TextFieldTableCell.forTableColumn());
        Comments.setOnEditCommit(new EventHandler<CellEditEvent<Line, String>>() {
            @Override
            public void handle(CellEditEvent<Line, String> t) {
                //
                ((Line) t.getTableView().getItems().get(t.getTablePosition().getRow())).setComments(t.getNewValue());
                //
            }
        }
        );
        //ThreadID
        TableColumn<Line, String> ThreadID = new TableColumn("Thread ID");
        ThreadID.setMinWidth(100);
        ThreadID.setEditable(true);
        ThreadID.setCellValueFactory(new PropertyValueFactory<>("ThreadID"));
        ThreadID.setCellFactory(TextFieldTableCell.forTableColumn());
        ThreadID.setOnEditCommit(new EventHandler<CellEditEvent<Line, String>>() {
            @Override
            public void handle(CellEditEvent<Line, String> t) {
                //
                ((Line) t.getTableView().getItems().get(t.getTablePosition().getRow())).setThreadID(t.getNewValue());
                //
            }
        }
        );
        // Delay Column
        TableColumn<Line, Double> DelayLabel = new TableColumn("Delay (sec)");
        DelayLabel.setMinWidth(100);
        DelayLabel.setEditable(true);
        DelayLabel.setCellValueFactory(new PropertyValueFactory<>("Delay"));
        DelayLabel.setCellFactory(TextFieldTableCell.<Line, Double>forTableColumn(new DoubleStringConverter()));
        DelayLabel.setOnEditCommit(new EventHandler<CellEditEvent<Line, Double>>() {
            @Override
            public void handle(CellEditEvent<Line, Double> t) {
                //
                //
                //String var = t.getNewValue();
                ((Line) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDelay(t.getNewValue());
                //
            }
        }
        );
        // Process Column
        TableColumn<Line, String> ProcessLabel = new TableColumn("Process");
        ProcessLabel.setMinWidth(200);
        //ProcessLabel.
        ProcessLabel.setCellValueFactory(new PropertyValueFactory<>("ExLabel"));
        
        // Set Argument Column
        TableColumn ArgNum = new TableColumn<>("Argument #");
        ArgNum.setMinWidth(100);
        ArgNum.setCellValueFactory(new PropertyValueFactory<>("ArgumentNo"));

        //   Table view setting here
        dasboardtable = new TableView<>();
        dasboardtable.setItems(maindash.getDashprocesses());
        dasboardtable.setEditable(true);
        dasboardtable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        dasboardtable.getColumns().addAll(IsActiveLabel, Comments, ThreadID, DelayLabel, ProcessLabel, ArgNum);
        //dasboardtable.getColumns().addAll(IsActiveLabel, Comments, ThreadID, ProcessLabel, ArgNum);
        // All button here
        Button loadprojectbutton = new Button();
        loadprojectbutton.setText("Load Project Dash");
        Button addlinebutton = new Button();
        addlinebutton.setText("Add Process Line");
        Button saveprojectbutton = new Button();
        saveprojectbutton.setText("Save Project Dash");
        //
        Button setargumentbutton = new Button();
        setargumentbutton.setText("Set Arguments");
        Button runlinebutton = new Button();
        runlinebutton.setText("Run Process(es)");
        Button deletelinebutton = new Button();
        deletelinebutton.setText("Delete Process(es)");
        
        addlinebutton.setOnAction(e -> {
            
            ObservableList<String> availablechoices = FXCollections.observableArrayList();
            for (Line l:maindash.getAvailableprocesses()) {
                availablechoices.add(l.getExLabel());
            }
            
            GridPane sublayout = new GridPane();
            sublayout.setVgap(5);
            sublayout.setHgap(5);
            Button acceptselection = new Button();
            acceptselection.setText("ok");
            acceptselection.setMinWidth(200);
            
            
            ChoiceBox cb = new ChoiceBox();
            cb.setItems(availablechoices);
            cb.getSelectionModel().selectFirst();
            sublayout.add(cb, 0 ,0);
            sublayout.add(acceptselection, 0 ,1);
            Scene subscene = new Scene(sublayout, 200, 60);
            Stage subwindow = new Stage();
            subwindow.setScene(subscene);
            subwindow.setTitle("Select Process");
            subwindow.show();
            

            acceptselection.setOnAction(f -> {
                String selection = cb.getSelectionModel().getSelectedItem().toString();
                for (Line l:maindash.getAvailableprocesses()) {
                    if (l.getExLabel().equals(selection)) {
                        System.out.println("found");
                        maindash.addline(l);
                        break;
                    }
                }
                subwindow.close();
            //refresh_table(dasboardtable);
        });
            //refresh_table(dasboardtable);
        });
        
        
        
        
        loadprojectbutton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Project Dash");
            File file = fileChooser.showOpenDialog(new Stage());
            ObservableList<Line> arraylist = Dash.load(file.getAbsolutePath(), maindash.getAvailableprocesses());
            maindash.setDashprocesses(arraylist);
            dasboardtable.setItems(maindash.getDashprocesses());
            //refresh_table(dasboardtable);
        });
        
        setargumentbutton.setOnAction(e -> {
            //dasboardtable.getSelectionModel().getSelectedItems();
            //for (Line l:dasboardtable.getSelectionModel().getSelectedItems()) {
             //   l.print();
            //}
            
            
            // If selection is unique
            if (dasboardtable.getSelectionModel().getSelectedItems().size() == 1) {

                Line lineitem = dasboardtable.getSelectionModel().getSelectedItem();
                // Arguments label
                TableColumn<Argument, String> Arg1 = new TableColumn("Arguments");
                Arg1.setMinWidth(200);
                //Arg1.setEditable(true);
                Arg1.setCellValueFactory(new PropertyValueFactory<>("datalabel"));
                
                // Arguments value
                TableColumn<Argument, String> Argvalue = new TableColumn("Value");
                Argvalue.setMinWidth(500);
                Argvalue.setEditable(true);
                Argvalue.setCellValueFactory(new PropertyValueFactory<>("data"));
                Argvalue.setCellFactory(TextFieldTableCell.forTableColumn());
                Argvalue.setOnEditCommit(new EventHandler<CellEditEvent<Argument, String>>() {
                    @Override
                    public void handle(CellEditEvent<Argument, String> t) {
                        //
                        ((Argument) t.getTableView().getItems().get(t.getTablePosition().getRow())).setData(t.getNewValue());
                        //
                    }
                }
                );

                //   Table view setting here
                TableView<Argument> argtable = new TableView<>();
                argtable.setItems(lineitem.getListofArgument());
                argtable.setEditable(true);
                argtable.setMinHeight(100);
                argtable.getColumns().addAll(Arg1, Argvalue);

                //
                Button ArgSelector = new Button();
                ArgSelector.setText("Select");
                ArgSelector.setOnAction(f -> {
                    // Need to call specific argument selector here
                    if (argtable.getSelectionModel().getSelectedItems().size() == 1) {
                        Argument arg = argtable.getSelectionModel().getSelectedItem();
                        System.out.println("Selector \"" + arg.getData());
                    }
                    
                });

                
                
                GridPane sublayout = new GridPane();
                sublayout.setVgap(5);
                sublayout.setHgap(5);
                Scene subscene = new Scene(sublayout, 720, 420);
                Stage subwindow = new Stage();
                subwindow.setScene(subscene);
                subwindow.setTitle("Argument Setting for " + dasboardtable.getSelectionModel().getSelectedItem().getComments());
                sublayout.add(argtable,0 ,1, 3, 3);
                sublayout.add(ArgSelector, 1 ,0);
                subwindow.showAndWait();
                
                
            }
        });
        
        GridPane mainlayout = new GridPane();
        mainlayout.setPadding(new Insets(10, 10, 10, 10));
        mainlayout.setMinSize(300, 300);
        mainlayout.setVgap(5);
        mainlayout.setHgap(5);
        mainlayout.add(addlinebutton, 0, 0);
        mainlayout.add(loadprojectbutton, 1, 0);
        mainlayout.add(saveprojectbutton, 2, 0);
        mainlayout.add(setargumentbutton, 4, 0);
        mainlayout.add(runlinebutton, 5, 0);
        mainlayout.add(deletelinebutton, 6, 0);
        mainlayout.add(dasboardtable,0 ,1, 10, 12);
        Scene mainwindow = new Scene(mainlayout, 850, 400);
        
        // Maximize window size here
        //Screen screen = Screen.getPrimary();
        //Rectangle2D bounds = screen.getVisualBounds();
        //primaryStage.setX(bounds.getMinX());
        //primaryStage.setY(bounds.getMinY());
        //primaryStage.setWidth(bounds.getWidth());
        //primaryStage.setHeight(bounds.getHeight());
        window = primaryStage;
        window.setTitle("Linux Structural Dashboard");
        window.setScene(mainwindow);
        window.show();
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
    
}
