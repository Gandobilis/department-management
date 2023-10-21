package com.example.demo.windows;

import com.example.demo.Main;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class Employees implements Table {
    private final TEmployees employees = TEmployees.getInstance();
    private final TableView<Employee> table = new TableView<>();
    private ObservableList<Employee> data = employees.findAll();

    @Override
    public Scene getScene() {
        StackPane layout = new StackPane();
        Scene scene = new Scene(layout, 1280, 720);

        final Button home = createHomeButton();

        final Label label = new Label("Employees");

        table.setEditable(true);

        TableColumn<Employee, String> fName = createTableColumn("First Name", "firstName", 100);
        TableColumn<Employee, String> lName = createTableColumn("Last Name", "lastName", 100);
        TableColumn<Employee, String> deptName = createTableColumn("Department Name", null, 200);

        deptName.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue();
            Department department = employee.getDepartment();
            return new SimpleStringProperty(department != null ? department.getName() : "No Department");
        });

        TableColumn<Employee, Void> del = createDeleteColumn();

        table.setItems(data);
        table.getColumns().clear();
        table.getColumns().addAll(fName, lName, deptName, del);

        VBox vbox = new VBox(home, label, table);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));

        layout.getChildren().add(vbox);
        return scene;
    }

    private TableColumn<Employee, String> createTableColumn(String text, String property, Integer width) {
        Callback<TableColumn<Employee, String>, TableCell<Employee, String>> cellFactory = p -> new EditingCell();

        TableColumn<Employee, String> column = new TableColumn<>(text);
        column.setMinWidth(width);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(cellFactory);
        column.setOnEditCommit(t -> {
            Employee empl = t.getTableView().getItems().get(t.getTablePosition().getRow());
            if (t.getTableColumn().getText().equals("First Name")) {
                empl.setFirstName(t.getNewValue());
            }
            if (t.getTableColumn().getText().equals("Last Name")) {
                empl.setLastName(t.getNewValue());
            }
            employees.update(empl);
            loadData();
            refreshTable();
        });
        return column;
    }

    private TableColumn<Employee, Void> createDeleteColumn() {
        TableColumn<Employee, Void> del = new TableColumn<>("Delete");
        del.setMinWidth(50);

        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory = param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    employees.delete(employee.getId());
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

    private Button createHomeButton() {
        Button home = new Button("Home");
        home.setOnAction(e -> Main.setDefaultScene());
        return home;
    }

    static class EditingCell extends TableCell<Employee, String> {

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
        data = employees.findAll();
    }

    private void refreshTable() {
        table.setItems(data);
        table.refresh();
    }
}
