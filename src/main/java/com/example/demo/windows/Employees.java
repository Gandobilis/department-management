package com.example.demo.windows;

import com.example.demo.models.Department;
import com.example.demo.models.Employee;
import com.example.demo.tables.TEmployees;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.util.Callback;

public class Employees {
    private final TEmployees employees = TEmployees.getInstance();
    private final TableView<Employee> table = new TableView<>();
    private ObservableList<Employee> data = employees.findAll();
    final HBox hb = new HBox();

    public Scene getScene() {
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane, 500, 500);

        final Label label = new Label("Employees");

        TableColumn<Employee, String> firstNameCol = createTableColumn("First Name", "firstName");
        TableColumn<Employee, String> lastNameCol = createTableColumn("Last Name", "lastName");
        TableColumn<Employee, String> departmentNameCol = createTableColumn("Department Name", null);

        departmentNameCol.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue();
            Department department = employee.getDepartment();
            return new SimpleStringProperty(department != null ? department.getName() : "No Department");
        });

        TableColumn<Employee, Void> deleteCol = createDeleteColumn();

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, departmentNameCol, deleteCol);

        final TextField addFirstName = createTextField("First Name", firstNameCol.getPrefWidth());
        final TextField addLastName = createTextField("Last Name", lastNameCol.getPrefWidth());
        final TextField addDepartmentName = createTextField("Department Name", departmentNameCol.getPrefWidth());
        final Button addButton = createAddButton(addFirstName, addLastName, addDepartmentName);

        hb.getChildren().addAll(addFirstName, addLastName, addDepartmentName, addButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox(label, table, hb);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));

        stackPane.getChildren().add(vbox);
        return scene;
    }

    private TableColumn<Employee, String> createTableColumn(String text, String property) {
        TableColumn<Employee, String> column = new TableColumn<>(text);
        column.setMinWidth(100);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    private TableColumn<Employee, Void> createDeleteColumn() {
        TableColumn<Employee, Void> deleteCol = new TableColumn<>("Delete");
        deleteCol.setMinWidth(50);

        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory = param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    employees.delete(employee.getId());
                    data = employees.findAll();
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

    private Button createAddButton(TextField firstName, TextField lastName, TextField departmentName) {
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            Employee employee = new Employee(firstName.getText(), lastName.getText(), new Department(departmentName.getText()));
            employees.create(employee);
            data = employees.findAll();
            table.setItems(data);
            table.refresh();

            firstName.clear();
            lastName.clear();
            departmentName.clear();
        });
        return addButton;
    }
}
