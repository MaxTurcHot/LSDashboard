/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboardgui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.scene.paint.Color;

/**
 *
 * @author max
 */
public class DashBoardGui extends Application {

    @Override
    public void start(Stage primaryStage) {

        Image dashboardimage = new Image("file:Dashboard.png");
        // Look if it's linux running or windows
        WindowsLinux winlin = new WindowsLinux("", "", "Linux".equals(System.getProperty("os.name")), "", "");
        if (!winlin.isLinux()) {
            File linuxwindowsetupfile = new File("WindowsPatch.txt");
            if (linuxwindowsetupfile.exists()) {
                Path path2file = Paths.get(linuxwindowsetupfile.getAbsolutePath());
                String[] paths = WindowsLinux.load(path2file);
                winlin.setWindir(paths[0]);
                winlin.setLindir(paths[1]);
                winlin.setWinexe(paths[2]);
                winlin.setLsdpath(paths[3]);
            } else {
                AlertBox.display("File not found", "WindowsPatch.txt must be in the application directory", dashboardimage);
                System.exit(1);
            }
        }

        Stage window;

        // Initialize the Main Dash
        ObservableList<Line> MainList = FXCollections.observableArrayList();
        MainDash maindash = new MainDash(MainList);

        String AvailableProcessesFile = "AvailableProcesses.txt";
        maindash.setAvailableprocesses(AvailableProcess.load(AvailableProcessesFile));
        TableView<Line> dasboardtable;
        File def = new File("DefaultValues.txt");
        if (def.exists()) {
            AvailableProcess.loaddefault(maindash, "DefaultValues.txt");
        }

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
        // Comments Column
        TableColumn<Line, String> Comments = new TableColumn("Comments");
        Comments.setMinWidth(280);
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
        dasboardtable.setPrefHeight(1800);
        //dasboardtable.setMaxWidth(400);
        dasboardtable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        dasboardtable.getColumns().addAll(Comments, ThreadID, DelayLabel, ProcessLabel, ArgNum);

        ContextMenu cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Add Process");
        cm.getItems().add(mi1);
        mi1.setOnAction(e -> {
            SelectProcess.window(maindash, dashboardimage);
        });

        MenuItem mi2 = new MenuItem("Load Project Dash");
        mi2.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("LSD - Load Project Dash");
            File file = fileChooser.showOpenDialog(new Stage());
            Dash.load(file.getAbsolutePath(), maindash);
            dasboardtable.setItems(maindash.getDashprocesses());
            // Need to set Base Path
            basepathfield.setText(maindash.getBasePath());
        });
        cm.getItems().add(mi2);

        dasboardtable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                // Detect where I clicked
                //TablePosition focusedCell = dasboardtable.getFocusModel().

                //int row = focusedCell.getRow();
                boolean count = dasboardtable.getSelectionModel().isEmpty();
                if (t.getButton() == MouseButton.SECONDARY && count) {
                    //dasboardtable.getSelectionModel().clearSelection();
                    cm.show(dasboardtable, t.getScreenX(), t.getScreenY());
                } else {
                    cm.hide();
                }
            }
        });

        //dasboardtable.getColumns().addAll(IsActiveLabel, Comments, ThreadID, ProcessLabel, ArgNum);
        // ------------------------------------- ACTION ON ROW ------------------------------------- 
        dasboardtable.setRowFactory(
                new Callback<TableView<Line>, TableRow<Line>>() {
            @Override
            public TableRow<Line> call(TableView<Line> tableView) {
                final TableRow<Line> row = new TableRow<>();
                final ContextMenu rowMenu = new ContextMenu();
                MenuItem setArgument = new MenuItem("Set Arguments");
                setArgument.setOnAction(e -> {
                    // If selection is unique
                    if (dasboardtable.getSelectionModel().getSelectedItems().size() == 1) {
                        Line lineitem = dasboardtable.getSelectionModel().getSelectedItem();
                        SetArgument.window(lineitem, dashboardimage, maindash.getBasePath());
                    }
                });

                MenuItem openpath = new MenuItem("Open Run Path");
                openpath.setOnAction(e -> {
                    // If selection is unique
                    if (dasboardtable.getSelectionModel().getSelectedItems().size() == 1) {
                        Line lineitem = dasboardtable.getSelectionModel().getSelectedItem();
                        if (!winlin.isLinux()) {
                            TerminalLauncher.directoryWindows(lineitem.getListofArgument().get(0).getAbsData(maindash.getBasePath()));
                        }
                    }
                });

                MenuItem cloneme = new MenuItem("Clone Process(es)");
                cloneme.setOnAction(e -> {
                    for (Line l : dasboardtable.getSelectionModel().getSelectedItems()) {
                        maindash.addline(l.clone());
                    }
                });

                MenuItem dryrunme = new MenuItem("Dry run");
                dryrunme.setOnAction(e -> {
                    // If selection is unique
                    if (dasboardtable.getSelectionModel().getSelectedItems().size() == 1) {
                        Line lineitem = dasboardtable.getSelectionModel().getSelectedItem();
                        lineitem.valid(maindash.getBasePath(), winlin);
                    }
                });

                MenuItem addItem = new MenuItem("Add Process");
                addItem.setOnAction(e -> {
                    SelectProcess.window(maindash, dashboardimage);
                });

                MenuItem loadsItems = new MenuItem("Load Project Dash");
                loadsItems.setOnAction(e -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("LSD - Load Project Dash");
                    File file = fileChooser.showOpenDialog(new Stage());
                    Dash.load(file.getAbsolutePath(), maindash);
                    dasboardtable.setItems(maindash.getDashprocesses());
                    // Need to set Base Path
                    basepathfield.setText(maindash.getBasePath());
                });

                MenuItem removeItem = new MenuItem("Delete Process(es)");
                removeItem.setOnAction(e -> {
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

                rowMenu.getItems().addAll(setArgument, openpath, cloneme, dryrunme, addItem, loadsItems, removeItem);

// only display context menu for non-null items:
                row.contextMenuProperty().bind(
                        Bindings.when(Bindings.isNotNull(row.itemProperty()))
                        .then(rowMenu)
                        .otherwise((ContextMenu) null));
                return row;
            }
        });

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
        runlinebutton.setText("Parallel");
        runlinebutton.setMinWidth(150);
        Button validatelinebutton = new Button();
        validatelinebutton.setText("Dry run Process(es)");
        validatelinebutton.setMinWidth(150);
        Button deletelinebutton = new Button();
        deletelinebutton.setText("Delete Process(es)");
        deletelinebutton.setMinWidth(150);
        Button batchbutton = new Button();
        batchbutton.setText("Batch");
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
                l.valid(maindash.getBasePath(), winlin);
            }
            //System.out.println("ici rdio");
        });

        addlinebutton.setOnAction(e -> {
            SelectProcess.window(maindash, dashboardimage);
            //maindash.addline(line);
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
            File fini = new File(maindash.getBasePath());
            if (fini.exists())
                {
                    fileChooser.setInitialDirectory(fini);
                }
            else
                {
                    MessageBox.show("Warning", "IO warning","Base path doesn't exist");
                }
            File file = fileChooser.showOpenDialog(new Stage());
            Dash.load(file.getAbsolutePath(), maindash);
            dasboardtable.setItems(maindash.getDashprocesses());
            // Need to set Base Path
            basepathfield.setText(maindash.getBasePath());
        });

        saveprojectbutton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("LSD - Save Project Dash");
            File fini = new File(maindash.getBasePath());
            fileChooser.setInitialDirectory(fini);
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
            if (areThreadOk(dasboardtable.getSelectionModel().getSelectedItems())){

                // Need to clean up directory first
                String pathstring;
                if (winlin.isLinux()) {
                    pathstring = "bashfiles";
                } else {
                    pathstring = winlin.getLsdpath() + File.separator + "bashfiles";
                }
                File f = new File(pathstring);
                if (f.isDirectory()) {
                    File[] files = f.listFiles();
                    for (File file : files) {
                        file.delete();
                    }
                } else {
                    f.mkdir();
                }
                for (Line l : dasboardtable.getSelectionModel().getSelectedItems()) {
                    l.write(maindash.getBasePath(), winlin, false);
                }
                if (winlin.isLinux()) {
                    TerminalLauncher.exLinux();
                } else {
                    TerminalLauncher.exWindows(winlin);
                }
            }
            else {
                MessageBox.show("Error","Operation can't complete", "Selected Thread must be different");
            }

        });
        batchbutton.setOnAction(e -> {
            if (areThreadOk(dasboardtable.getSelectionModel().getSelectedItems())){
                // Need to clean up directory first
                String pathstring;
                if (winlin.isLinux()) {
                    pathstring = "bashfiles";
                } else {
                    pathstring = winlin.getLsdpath() + File.separator + "bashfiles";
                }
                File f = new File(pathstring);
                if (f.isDirectory()) {
                    File[] files = f.listFiles();
                    for (File file : files) {
                        file.delete();
                    }
                } else {
                    f.mkdir();
                }
                for (Line l : dasboardtable.getSelectionModel().getSelectedItems()) {
                    l.write(maindash.getBasePath(), winlin, true);
                }
                if (winlin.isLinux()) {
                    TerminalLauncher.exLinux();
                } else {
                    TerminalLauncher.exWindows(winlin);
                }
            }
            else {
                MessageBox.show("Error","Operation can't complete", "Selected Thread must be different");
            }

        });

        basepathselection.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("LSD - Directory Selector");
            File fini = new File(maindash.getBasePath());
            if (fini.exists())
            {
                directoryChooser.setInitialDirectory(fini);
            }
            else
            {
                MessageBox.show("Warning", "IO warning","Base path doesn't exist");
            }
            File file = directoryChooser.showDialog(new Stage());
            // Change the maindash basepath
            maindash.setBasePath(file.getAbsolutePath());
            // change field display
            basepathfield.setText(file.getAbsolutePath());
        });

        Label mtlabel = new Label("                       Maxime Turcotte 2016");
        final double MAX_FONT_SIZE = 8.0;
        mtlabel.setFont(new Font(MAX_FONT_SIZE));
        
        
        GridPane mainlayout = new GridPane();
        mainlayout.setPadding(new Insets(10, 10, 10, 10));
        mainlayout.setMinSize(300, 300);
        mainlayout.setVgap(5);
        mainlayout.setHgap(5);
        mainlayout.add(basepathlabel, 0, 0);
        mainlayout.add(basepathfield, 1, 0, 3, 1);
        //basepathselection
        mainlayout.add(basepathselection, 4, 0);
        mainlayout.add(addlinebutton, 2, 1);
        //mainlayout.add(clonelinebutton, 0, 2);
        mainlayout.add(loadprojectbutton, 0, 1);
        mainlayout.add(saveprojectbutton, 1, 1);
        //mainlayout.add(setargumentbutton, 2, 2);
        //mainlayout.add(validatelinebutton, 3, 1);
        //mainlayout.add(deletelinebutton, 3, 2);
        mainlayout.add(runlinebutton, 3, 1);
        mainlayout.add(batchbutton, 4, 1);
        mainlayout.add(dasboardtable, 0, 2, 5, 1);
        mainlayout.add(mtlabel, 4, 3);
        Scene mainwindow = new Scene(mainlayout, 790, 600);

        // Maximize window size here
        //Screen screen = Screen.getPrimary();
        //Rectangle2D bounds = screen.getVisualBounds();
        //primaryStage.setX(bounds.getMinX());
        //primaryStage.setY(bounds.getMinY());
        primaryStage.setMaxWidth(800);
        //primaryStage.setHeight(bounds.getHeight());
        window = primaryStage;
        window.setTitle("Linux Structural Dashboard 0.0.8");
        window.setScene(mainwindow);
        window.getIcons().add(dashboardimage);
        window.show();
    }

    private boolean areThreadOk(ObservableList<Line> selectedItems) {
        if (selectedItems.size() == 0) {

            return true;
        }
        else {
            for (int i = 0; i < selectedItems.size(); i++) {
                for (int j = i + 1; j < selectedItems.size(); j++) {

                    if (selectedItems.get(i).getThreadID().equals(selectedItems.get(j).getThreadID())) {
                        return false;
                    }
                }
            }
            return true;
        }
        //return false;
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
