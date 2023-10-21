package com.example.demo.windows;

import com.example.demo.Main;
import com.example.demo.models.Department;
import com.example.demo.models.Employee;
import com.example.demo.tables.TDepartments;
import com.example.demo.tables.TEmployees;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private final TEmployees employees = TEmployees.getInstance();
    private final TDepartments departments = TDepartments.getInstance();
    private final TableView<Department> departmentsTable = new TableView<>();
    private ObservableList<Department> departmentsData = departments.findAll();

    public Scene getScene() {
        StackPane departmentsLayout = new StackPane();
        Scene scene = new Scene(departmentsLayout, 500, 600);

        Button homeButton = createHomeButton();

        Label departmentsLabel = new Label("Departments");

        departmentsTable.setEditable(true);

        TableColumn<Department, String> departmentNameCol = createTableColumn("Department Name", "name", 100);
        TableColumn<Department, String> parentDepartmentNameCol = createTableColumn("Parent Department Name", null, 250);

        parentDepartmentNameCol.setCellValueFactory(cellData -> {
            Department department = cellData.getValue();
            Department parentDepartment = department.getParentDepartment();
            return new SimpleStringProperty(parentDepartment != null ? parentDepartment.getName() : "No Parent Department");
        });

        TableColumn<Department, Void> deleteDepartmentCol = createDeleteColumn();

        departmentsTable.setItems(departmentsData);
        departmentsTable.getColumns().addAll(departmentNameCol, parentDepartmentNameCol, deleteDepartmentCol);

        VBox vBox = new VBox();

        HBox hBox = new HBox();
        TextField addDepartmentName = createTextField("Name", departmentNameCol.getPrefWidth());
        Button addDepartmentButton = createDepartmentAddButton(addDepartmentName);

        hBox.getChildren().addAll(addDepartmentName, addDepartmentButton);
        hBox.setSpacing(3);

        HBox hBox1 = new HBox();
        TextField addFirstName = createTextField("First Name", departmentNameCol.getPrefWidth());
        TextField addLastName = createTextField("Last Name", departmentNameCol.getPrefWidth());
        TextField addEmployeeDepartmentName = createTextField("Department Name", departmentNameCol.getPrefWidth());
        Button addEmployeeButton = createEmployeeAddButton(addFirstName, addLastName, addEmployeeDepartmentName);

        hBox1.getChildren().addAll(addFirstName, addLastName, addEmployeeDepartmentName, addEmployeeButton);
        hBox1.setSpacing(3);

        vBox.getChildren().addAll(hBox, hBox1);
        vBox.setSpacing(5);

        VBox vbox = new VBox(homeButton, departmentsLabel, departmentsTable, vBox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));

        departmentsLayout.getChildren().add(vbox);

        return scene;
    }

    private Button createHomeButton() {
        Button homeButton = new Button("Home");
        homeButton.setOnAction(e -> Main.setDefaultScene());
        return homeButton;
    }

    private TableColumn<Department, String> createTableColumn(String text, String property, double width) {
        Callback<TableColumn<Department, String>, TableCell<Department, String>> cellFactory = p -> new EditingCell();

        TableColumn<Department, String> column = new TableColumn<>(text);
        column.setMinWidth(width);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(cellFactory);
        column.setOnEditCommit(t -> {
            Department department = t.getTableView().getItems().get(t.getTablePosition().getRow());
            department.setName(t.getNewValue());
            departments.update(department);
            departmentsData = departments.findAll();
            departmentsTable.setItems(departmentsData);
            departmentsTable.refresh();
        });
        return column;
    }

    private TableColumn<Department, Void> createDeleteColumn() {
        TableColumn<Department, Void> deleteDepartmentCol = new TableColumn<>("Delete");
        deleteDepartmentCol.setMinWidth(50);

        Callback<TableColumn<Department, Void>, TableCell<Department, Void>> cellFactory = param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Department department = getTableView().getItems().get(getIndex());
                    departments.delete(department.getId());
                    departmentsData = departments.findAll();
                    departmentsTable.setItems(departmentsData);
                    departmentsTable.refresh();
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
        deleteDepartmentCol.setCellFactory(cellFactory);

        return deleteDepartmentCol;
    }

    private TextField createTextField(String prompt, double width) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setMaxWidth(width);
        return textField;
    }

    private Button createEmployeeAddButton(TextField firstName, TextField lastName, TextField departmentName) {
        Button addButton = new Button("Add Employee");
        addButton.setOnAction(e -> {
            Employee employee = new Employee(firstName.getText(), lastName.getText(), new Department(departmentName.getText()));
            employees.create(employee);
            departmentsData = departments.findAll();
            departmentsTable.setItems(departmentsData);
            departmentsTable.refresh();

            firstName.clear();
            lastName.clear();
            departmentName.clear();
        });
        return addButton;
    }

    private Button createDepartmentAddButton(TextField nameField) {
        Button addDepartmentButton = new Button("Add Department");
        addDepartmentButton.setOnAction(e -> {
            Department department = new Department(nameField.getText());
            departments.create(department);
            departmentsData = departments.findAll();
            departmentsTable.setItems(departmentsData);
            departmentsTable.refresh();
            nameField.clear();
        });
        return addDepartmentButton;
    }

    static class EditingCell extends TableCell<Department, String> {

        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0,
                                    Boolean arg1, Boolean arg2) {
                    if (!arg2) {
                        commitEdit(textField.getText());
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }
}