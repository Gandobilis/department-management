package com.example.demo.windows;

import com.example.demo.Main;
import com.example.demo.models.Department;
import com.example.demo.tables.TDepartments;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class Departments {
    private final TDepartments departments = TDepartments.getInstance();
    private final TableView<Department> table = new TableView<>();
    private ObservableList<Department> data = departments.findAll();
    final HBox hBox = new HBox();

    public Scene getScene() {
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane, 500, 500);

        final Button home = createHomeButton();

        final Label label = new Label("Departments");

        TableColumn<Department, String> nameCol = createTableColumn("Name", "name", 100);
        TableColumn<Department, String> parentDepartmentCol = createTableColumn("Department Name", null, 100);

        parentDepartmentCol.setCellValueFactory(cellData -> {
            Department department = cellData.getValue();
            Department parentDepartment = department.getParentDepartment();
            return new SimpleStringProperty(parentDepartment != null ? parentDepartment.getName() : "No Parent Department");
        });

        TableColumn<Department, Void> deleteCol = createDeleteColumn();

        table.setItems(data);
        table.getColumns().addAll(nameCol, parentDepartmentCol, deleteCol);

        final TextField addName = createTextField("Name", nameCol.getPrefWidth());
        final Button addButton = createAddButton(addName);

        hBox.getChildren().addAll(addName, addButton);
        hBox.setSpacing(3);

        final VBox vbox = new VBox(home, label, table, hBox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));

        stackPane.getChildren().add(vbox);

        return scene;
    }

    private Button createHomeButton() {
        Button home = new Button("Home");
        home.setOnAction(e -> Main.setDefaultScene());
        return home;
    }

    private TableColumn<Department, String> createTableColumn(String text, String property, double width) {
        TableColumn<Department, String> column = new TableColumn<>(text);
        column.setMinWidth(width);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    private TableColumn<Department, Void> createDeleteColumn() {
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

        return deleteCol;
    }

    private TextField createTextField(String prompt, double width) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setMaxWidth(width);
        return textField;
    }

    private Button createAddButton(TextField nameField) {
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            Department department = new Department(nameField.getText());
            departments.create(department);
            data = departments.findAll();
            table.setItems(data);
            table.refresh();
            nameField.clear();
        });
        return addButton;
    }
}

