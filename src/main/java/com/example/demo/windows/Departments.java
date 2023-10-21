package com.example.demo.windows;

import com.example.demo.models.Department;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class Departments {
    private final com.example.demo.tables.Departments departments = com.example.demo.tables.Departments.getInstance();
    private final TableView<Department> table = new TableView<>();
    private ObservableList<Department> data = departments.findAll();
    final HBox hb = new HBox();

    public Scene getScene() {
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane, 500, 500);

        final Label label = new Label("Departments");

        TableColumn<Department, String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        TableColumn<Department, String> parentDepartmentCol = new TableColumn<>("Department Name");
        parentDepartmentCol.setMinWidth(100);

        parentDepartmentCol.setCellValueFactory(cellData -> {
            Department department = cellData.getValue();
            Department parentDepartment = department.getParentDepartment();
            if (parentDepartment != null) {
                return new SimpleStringProperty(parentDepartment.getName());
            } else {
                return new SimpleStringProperty("No Parent Department");
            }
        });

        TableColumn<Department, Void> deleteCol = new TableColumn<>("Delete");
        deleteCol.setMinWidth(50);

        Callback<TableColumn<Department, Void>, TableCell<Department, Void>> cellFactory = param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Department department = getTableView().getItems().get(getIndex());
                    departments.delete(department.getId());
                    data = departments.findAll();
                    table.setItems(data);
                    table.refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        };
        deleteCol.setCellFactory(cellFactory);

        table.setItems(data);
        table.getColumns().addAll(nameCol, parentDepartmentCol, deleteCol);

        final TextField addName = new TextField();
        addName.setPromptText("First Name");
        addName.setMaxWidth(nameCol.getPrefWidth());

        final Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            Department department = new Department(
                    addName.getText()
            );
            departments.create(department);
            data = departments.findAll();
            table.setItems(data);
            table.refresh();

            addName.clear();
        });

        hb.getChildren().addAll(addName, addButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);

        stackPane.getChildren().add(vbox);

        return scene;
    }
}
