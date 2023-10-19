package com.example.demo.windows;

import com.example.demo.models.Department;
import com.example.demo.models.Employee;
import com.example.demo.tables.Employees;
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

public class EmployeesWindow {
    private final Employees employees = new Employees();
    private final TableView<Employee> table = new TableView<>();
    private ObservableList<Employee> data = employees.findAll();
    final HBox hb = new HBox();

    public Scene getScene() {
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane, 500, 500);

        final Label label = new Label("Employees");

        TableColumn<Employee, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));

        TableColumn<Employee, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));

        TableColumn<Employee, String> departmentNameCol = new TableColumn<>("Department Name");
        departmentNameCol.setMinWidth(100);

        departmentNameCol.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue();
            Department department = employee.getDepartment();
            if (department != null) {
                return new SimpleStringProperty(department.getName());
            } else {
                return new SimpleStringProperty("No Department");
            }
        });

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

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, departmentNameCol, deleteCol);

        final TextField addFirstName = new TextField();
        addFirstName.setPromptText("First Name");
        addFirstName.setMaxWidth(firstNameCol.getPrefWidth());
        final TextField addLastName = new TextField();
        addLastName.setMaxWidth(lastNameCol.getPrefWidth());
        addLastName.setPromptText("Last Name");
        final TextField addDepartmentName = new TextField();
        addDepartmentName.setMaxWidth(addDepartmentName.getPrefWidth());
        addDepartmentName.setPromptText("Department Name");

        final Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            Employee employee = new Employee(
                    addFirstName.getText(),
                    addLastName.getText(),
                    new Department(addDepartmentName.getText())
            );
            employees.create(employee);
            data = employees.findAll();
            table.setItems(data);
            table.refresh();

            addFirstName.clear();
            addLastName.clear();
        });

        hb.getChildren().addAll(addFirstName, addLastName, addDepartmentName, addButton);
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
