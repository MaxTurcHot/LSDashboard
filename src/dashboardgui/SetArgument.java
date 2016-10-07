/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboardgui;

import java.util.HashSet;
import java.util.Set;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author max
 */
public class SetArgument {

    static CheckBox nonexistingFile;
    static CheckBox relativemode;

    public static void window(Line lineitem, Image dashboardimage, String basepath) {
        // Arguments label
        TableColumn<Argument, String> Arg1 = new TableColumn("Arguments");
        Arg1.setMinWidth(300);
        Arg1.setSortable(false);
        //Arg1.setEditable(true);
        Arg1.setCellValueFactory(new PropertyValueFactory<>("datalabel"));

        // Arguments value
        TableColumn<Argument, String> Argvalue = new TableColumn("Value");
        Argvalue.setMinWidth(400);
        Argvalue.setSortable(false);
        Argvalue.setCellValueFactory(new PropertyValueFactory<>("data"));

        //Argvalue.setPrefWidth(1800);
        /*Argvalue.setEditable(true);
        
        Argvalue.setCellFactory(TextFieldTableCell.forTableColumn());
        Argvalue.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Argument, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Argument, String> t) {
                //
                ((Argument) t.getTableView().getItems().get(t.getTablePosition().getRow())).setData(t.getNewValue());
                //
            }
        }
        );*/
        
        
        
        //   Table view setting here
        TableView<Argument> argtable = new TableView<>();
        argtable.setItems(lineitem.getListofArgument());
        argtable.setEditable(true);
        argtable.setMinHeight(100);
        argtable.setPrefWidth(1800);
        argtable.setPrefHeight(1800);
        argtable.getColumns().addAll(Arg1, Argvalue);

        argtable.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
            @Override
            public void onChanged(Change<? extends Integer> change) {
                if (argtable.getSelectionModel().getSelectedItem().getStyle().equals("file")) {
                    nonexistingFile.setDisable(false);
                } else {
                    nonexistingFile.setDisable(true);
                }
            }

        });

        Button ArgSelector = new Button();
        ArgSelector.setText("Select");
        ArgSelector.setMinWidth(400);
        ArgSelector.setOnAction(f -> {
            // Need to call specific argument selector here
            if (argtable.getSelectionModel().getSelectedItems().size() == 1) {
                Argument arg = argtable.getSelectionModel().getSelectedItem();
                // in case file doesn't exist yet, method must change
                if (arg.getStyle().equals("file") && nonexistingFile.isSelected()) {
                    arg.selector(basepath, true);
                } else {
                    arg.selector(basepath);
                }
                argtable.refresh();

            }

        });
        
        
        argtable.setRowFactory(tv -> {
            TableRow<Argument> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    //Argument rowData = row.getItem();
                    //System.out.println(rowData);
                    ArgSelector.fire();
                }
            });
            return row;
        });
        //
        

        nonexistingFile = new CheckBox();
        nonexistingFile.setText("File doesn\'t exist yet");
        nonexistingFile.setDisable(true);

        relativemode = new CheckBox();
        relativemode.setText("Relative mode");
        //relativemode.isSelected();
        relativemode.setOnAction((event) -> {
            // Box was clicked, do something...
            boolean ischecked = relativemode.isSelected();
            lineitem.setRelativeMode(ischecked);
        });
        relativemode.setSelected(lineitem.isRelativeMode());

        
        Label instruction = new Label("(double click to assign value)");
        final double MAX_FONT_SIZE = 10;
        instruction.setFont(new Font(MAX_FONT_SIZE));
        
        
        GridPane sublayout = new GridPane();
        sublayout.setVgap(5);
        sublayout.setHgap(5);
        Scene subscene = new Scene(sublayout, 720, 420);
        Stage subwindow = new Stage();
        subwindow.setScene(subscene);
        subwindow.getIcons().add(dashboardimage);
        subwindow.setTitle("LSD - Argument Setting for " + lineitem.getComments());
        subwindow.setMinWidth(720);
        subwindow.setMaxWidth(720);
        subwindow.initModality(Modality.APPLICATION_MODAL);
        sublayout.add(argtable, 1, 2, 3, 3);
        //sublayout.add(ArgSelector, 1, 1);
        sublayout.add(nonexistingFile, 1, 1);
        sublayout.add(relativemode, 2, 1);
        sublayout.add(instruction ,3 ,1);
        subwindow.showAndWait();
    }
}
