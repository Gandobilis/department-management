package com.example.demo.windows;

import com.example.demo.Main;
import com.example.demo.models.Department;
import com.example.demo.models.Employee;
import com.example.demo.tables.TDepartments;
import com.example.demo.tables.TEmployees;
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

public class Departments implements Table {
    private final TEmployees emps = TEmployees.getInstance();
    private final TDepartments depts = TDepartments.getInstance();
    private final TableView<Department> table = new TableView<>();
    private ObservableList<Department> data = depts.findAll();

    @Override
    public Scene getScene() {
        StackPane layout = new StackPane();
        Scene scene = new Scene(layout, 1280, 720);

        Button home = createHomeButton();
        Label label = new Label("Departments");

        table.setEditable(true);

        TableColumn<Department, String> name = createTableColumn("Name", "name", 100);
        TableColumn<Department, String> parName = createTableColumn("Parent Name", null, 250);

        parName.setCellValueFactory(cellData -> {
            Department dept = cellData.getValue();
            Department parDept = dept.getParentDepartment();
            return new SimpleStringProperty(parDept != null ? parDept.getName() : "No Parent");
        });

        TableColumn<Department, Void> del = createDeleteColumn();

        table.setItems(data);
        table.getColumns().clear();
        table.getColumns().addAll(name, parName, del);

        VBox forms = new VBox();

        HBox deptForm = new HBox();
        TextField deptName = createTextField("Name", name.getPrefWidth());
        ChoiceBox<Department> parentDeptName = new ChoiceBox<>(depts.findAll());
        parentDeptName.getSelectionModel().selectFirst();
        parentDeptName.setTooltip(new Tooltip("Select the department"));
        Button addDept = createDepartmentAddButton(deptName, parentDeptName);

        deptForm.getChildren().addAll(deptName, parentDeptName, addDept);
        deptForm.setSpacing(3);

        HBox emplForm = new HBox();
        TextField fName = createTextField("First Name", name.getPrefWidth());
        TextField lName = createTextField("Last Name", name.getPrefWidth());
        ChoiceBox<Department> emplDeptName = new ChoiceBox<>(depts.findAll());
        emplDeptName.getSelectionModel().selectFirst();
        emplDeptName.setTooltip(new Tooltip("Select the department"));
        Button addEmpl = createEmployeeAddButton(fName, lName, emplDeptName);

        emplForm.getChildren().addAll(fName, lName, emplDeptName, addEmpl);
        emplForm.setSpacing(3);

        forms.getChildren().addAll(deptForm, emplForm);
        forms.setSpacing(5);

        VBox container = new VBox(home, label, table, forms);
        container.setAlignment(Pos.CENTER);
        container.setSpacing(5);
        container.setPadding(new Insets(10, 0, 0, 10));

        layout.getChildren().add(container);

        return scene;
    }

    private Button createHomeButton() {
        Button home = new Button("Home");
        home.setOnAction(e -> Main.setDefaultScene());
        return home;
    }

    private TableColumn<Department, String> createTableColumn(String text, String property, double width) {
        Callback<TableColumn<Department, String>, TableCell<Department, String>> cellFactory = p -> new EditingCell();

        TableColumn<Department, String> column = new TableColumn<>(text);
        column.setMinWidth(width);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(cellFactory);
        column.setOnEditCommit(t -> {
            Department dept = t.getTableView().getItems().get(t.getTablePosition().getRow());
            dept.setName(t.getNewValue());
            depts.update(dept);
            loadData();
            refreshTable();
        });
        return column;
    }

    private TableColumn<Department, Void> createDeleteColumn() {
        TableColumn<Department, Void> del = new TableColumn<>("Delete");
        del.setMinWidth(50);

        Callback<TableColumn<Department, Void>, TableCell<Department, Void>> cellFactory = param -> new TableCell<>() {
            final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Department dept = getTableView().getItems().get(getIndex());
                    depts.delete(dept.getId());
                    loadData();
                    refreshTable();
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
        del.setCellFactory(cellFactory);

        return del;
    }

    private TextField createTextField(String prompt, double width) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setMaxWidth(width);
        return textField;
    }

    private Button createEmployeeAddButton(TextField fName, TextField lName, ChoiceBox<Department> emplDeptName) {
        Button add = new Button("Add Employee");
        add.setOnAction(e -> {
            Employee employee = new Employee(fName.getText(), lName.getText(), emplDeptName.getSelectionModel().getSelectedItem());
            emps.create(employee);

            fName.clear();
            lName.clear();
        });
        return add;
    }

    private Button createDepartmentAddButton(TextField nameField, ChoiceBox<Department> parentDeptName) {
        Button addDept = new Button("Add Department");
        addDept.setOnAction(e -> {
            Department dept = new Department(nameField.getText(), parentDeptName.getSelectionModel().getSelectedItem());
            depts.create(dept);
            loadData();
            refreshTable();

            nameField.clear();
        });
        return addDept;
    }

    static class EditingCell extends TableCell<Department, String> {

        private TextField textField;

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
                    if (textField != null) textField.setText(getString());
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
            textField.focusedProperty().addListener((arg0, arg1, arg2) -> {
                if (!arg2) commitEdit(textField.getText());
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }

    }

    @Override
    public void loadData() {
        data = depts.findAll();
    }

    private void refreshTable() {
        table.setItems(data);
        table.refresh();
    }
}