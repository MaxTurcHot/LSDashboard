/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboardgui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.converter.DoubleStringConverter;

/**
 *
 * @author max
 */
public class DashBoardGui extends Application {

    @Override
    public void start(Stage primaryStage) {

        Stage window;

        Image dashboardimage = new Image("file:Dashboard.png");
        
        // Initialize the Main Dash
        ObservableList<Line> MainList = FXCollections.observableArrayList();
        MainDash maindash = new MainDash(MainList);
        //System.out.println();
        //String AvailableProcessesFile = "/home/max/NetBeansProjects/DashBoardGui/AvailableProcesses.txt";
        String AvailableProcessesFile = "AvailableProcesses.txt";
        maindash.setAvailableprocesses(AvailableProcess.load(AvailableProcessesFile));
        TableView<Line> dasboardtable;

        // Base Path setting //
        //String BasePath= System.getProperty("user.dir");
        Label basepathlabel = new Label("Project Base Path:");
        basepathlabel.setMinWidth(150);
        TextField basepathfield = new TextField();
        basepathfield.setMinWidth(400);
        // Set initial value to PWD
        basepathfield.setText(maindash.getBasePath());

        ////////////////////////////////////////
        /////// COLUMN DEFINITION //////////////
        ////////////////////////////////////////
        // Is Active Column
        TableColumn<Line, String> IsActiveLabel = new TableColumn("Active");
        IsActiveLabel.setMinWidth(60);
        IsActiveLabel.setEditable(true);
        IsActiveLabel.setCellValueFactory(new PropertyValueFactory<>("IsActive"));
        IsActiveLabel.setCellFactory(TextFieldTableCell.forTableColumn());
        IsActiveLabel.setOnEditCommit(new EventHandler<CellEditEvent<Line, String>>() {
            @Override
            public void handle(CellEditEvent<Line, String> t) {
                //
                List<String> istrue = Arrays.asList("TRUE", "True", "true", "1", "on", "ON", "active");
                List<String> isfalse = Arrays.asList("FALSE", "False", "false", "0", "OFF", "off", "inactive");
                boolean containedtrue = istrue.contains(t.getNewValue());
                boolean containedfalse = isfalse.contains(t.getNewValue());
                if (containedtrue) {
                    ((Line) t.getTableView().getItems().get(t.getTablePosition().getRow())).setIsActive("true");
                } else if (containedfalse) {
                    ((Line) t.getTableView().getItems().get(t.getTablePosition().getRow())).setIsActive("false");
                }

                //
            }
        }
        );
        // Comments Column
        TableColumn<Line, String> Comments = new TableColumn("Comments");
        Comments.setMinWidth(210);
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
        TableColumn<Line, String> ThreadID = new TableColumn("Thread");
        ThreadID.setMinWidth(80);
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
        dasboardtable.setPrefHeight(800);
        dasboardtable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        dasboardtable.getColumns().addAll(IsActiveLabel, Comments, ThreadID, DelayLabel, ProcessLabel, ArgNum);
        //dasboardtable.getColumns().addAll(IsActiveLabel, Comments, ThreadID, ProcessLabel, ArgNum);

        ////////////////////////////////////////
        /////// Button DEFINITION //////////////
        ////////////////////////////////////////
        Button loadprojectbutton = new Button();
        loadprojectbutton.setText("Load Project Dash");
        loadprojectbutton.setMinWidth(150);
        Button addlinebutton = new Button();
        addlinebutton.setText("Add Process");
        addlinebutton.setMinWidth(150);
        Button clonelinebutton = new Button();
        clonelinebutton.setText("Clone Process(es)");
        clonelinebutton.setMinWidth(150);
        Button saveprojectbutton = new Button();
        saveprojectbutton.setText("Save Project Dash");
        saveprojectbutton.setMinWidth(150);
        //
        Button setargumentbutton = new Button();
        setargumentbutton.setText("Set Arguments");
        setargumentbutton.setMinWidth(150);
        Button runlinebutton = new Button();
        runlinebutton.setText("Run Process(es)");
        runlinebutton.setMinWidth(150);
        Button validatelinebutton = new Button();
        validatelinebutton.setText("Validate Process(es)");
        Button deletelinebutton = new Button();
        deletelinebutton.setText("Delete Process(es)");
        deletelinebutton.setMinWidth(150);
        Button batchbutton = new Button();
        batchbutton.setText("Batch run");
        batchbutton.setMinWidth(150);
        Button basepathselection = new Button();
        basepathselection.setText("Select Base Path");
        basepathselection.setMinWidth(150);

        ////////////////////////////////////////
        /////// Action On Test field ///////////
        ////////////////////////////////////////
        basepathfield.setOnAction(e -> {
            maindash.setBasePath(basepathfield.getText());
        });
        ////////////////////////////////////////
        /////// Action On Button  //////////////
        ////////////////////////////////////////
        deletelinebutton.setOnAction(e -> {
            // Get index of lines to remove
            List<Integer> indexlist = new ArrayList<Integer>();
            for (Line l : dasboardtable.getSelectionModel().getSelectedItems()) {
                indexlist.add(maindash.getDashprocesses().indexOf(l));
            }
            // remove the lines at reverse (reverse is useless finnaly)
            for (int i = indexlist.size() - 1; i > -1; i--) {
                maindash.removeline(maindash.getDashprocesses().get(indexlist.get(i)));
            }
        });

        validatelinebutton.setOnAction(e -> {
            for (Line l : dasboardtable.getSelectionModel().getSelectedItems()) {
                l.valid(maindash.getBasePath());
            }
            //System.out.println("ici rdio");
        });
        
        addlinebutton.setOnAction(e -> {
            Line line = SelectProcess.window(maindash.getAvailableprocesses(), dashboardimage);
            maindash.addline(line);
            //System.out.println("ici rdio");
        });

        clonelinebutton.setOnAction(e -> {
            for (Line l : dasboardtable.getSelectionModel().getSelectedItems()) {
                maindash.addline(l.clone());
            }
        });

        loadprojectbutton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("LSD - Load Project Dash");
            File file = fileChooser.showOpenDialog(new Stage());
            Dash.load(file.getAbsolutePath(), maindash);
            dasboardtable.setItems(maindash.getDashprocesses());
            // Need to set Base Path
            basepathfield.setText(maindash.getBasePath());
        });

        saveprojectbutton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("LSD - Save Project Dash");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("csv file (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(new Stage());
            Dash.save(file.getAbsolutePath(), maindash.getDashprocesses(), maindash.getBasePath());
        });

        setargumentbutton.setOnAction(e -> {
            // If selection is unique
            if (dasboardtable.getSelectionModel().getSelectedItems().size() == 1) {
                Line lineitem = dasboardtable.getSelectionModel().getSelectedItem();
                SetArgument.window(lineitem, dashboardimage, maindash.getBasePath());
            }
        });

        runlinebutton.setOnAction(e -> {
            for (Line l : dasboardtable.getSelectionModel().getSelectedItems()) {
                l.execute(maindash.getBasePath());
            }
        });

        basepathselection.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("LSD - Directory Selector");
            File file = directoryChooser.showDialog(new Stage());
            // Change the maindash basepath
            maindash.setBasePath(file.getAbsolutePath());
            // change field display
            basepathfield.setText(file.getAbsolutePath());
        });

        GridPane mainlayout = new GridPane();
        mainlayout.setPadding(new Insets(10, 10, 10, 10));
        mainlayout.setMinSize(300, 300);
        mainlayout.setVgap(5);
        mainlayout.setHgap(5);
        mainlayout.add(basepathlabel, 0, 0);
        mainlayout.add(basepathfield, 1, 0, 3, 1);
        //basepathselection
        mainlayout.add(basepathselection, 4, 0);
        mainlayout.add(addlinebutton, 0, 1);
        mainlayout.add(clonelinebutton, 0, 2);
        mainlayout.add(loadprojectbutton, 1, 1);
        mainlayout.add(saveprojectbutton, 1, 2);
        mainlayout.add(setargumentbutton, 2, 2);
        mainlayout.add(validatelinebutton, 3, 1);
        mainlayout.add(deletelinebutton, 3, 2);
        mainlayout.add(runlinebutton, 4, 2);
        mainlayout.add(batchbutton, 4, 1);
        mainlayout.add(dasboardtable, 0, 3, 5, 1);
        Scene mainwindow = new Scene(mainlayout, 790, 600);

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
        window.getIcons().add(dashboardimage);
        window.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(args);
    }

    public static void refresh_table(TableView table) {
        for (int i = 0; i < table.getColumns().size(); i++) {
            ((TableColumn) (table.getColumns().get(i))).setVisible(false);
            ((TableColumn) (table.getColumns().get(i))).setVisible(true);
        }
    }

}
