package com.example.demo.windows;

import com.example.demo.models.Department;
import com.example.demo.models.DepartmentOrEmployee;
import com.example.demo.models.Employee;
import com.example.demo.tables.TDepartments;
import com.example.demo.tables.TEmployees;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TreeViewSample extends Application {
    private final TableView<Employee> table = new TableView<>();
    private final ObservableList<Department> departments = TDepartments.getInstance().findAll();
    private ObservableList<Employee> employees = TEmployees.getInstance().findAll();
    private final TreeItem<DepartmentOrEmployee> rootNode = new TreeItem<>(new DepartmentOrEmployee(new Department("Departments")));

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Create tree nodes for departments and employees
        createDepartmentNodes();
        createEmployeeNodes();

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
        TableColumn<Employee, Void> edit = createEditColumn();

        table.setItems(employees);
        table.getColumns().addAll(fName, lName, deptName, edit, del);

        TreeView<DepartmentOrEmployee> treeView = new TreeView<>(rootNode);

        // Set up the JavaFX scene
        HBox box = new HBox();
        box.getChildren().addAll(treeView, table);
        Scene scene = new Scene(box, 1280, 720);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    // Helper method to create department nodes and add them to the tree
    private void createDepartmentNodes() {
        for (Department department : departments) {
            TreeItem<DepartmentOrEmployee> departmentNode = findDepartmentNode(rootNode, department.getParentDepartment());

            if (departmentNode == null) {
                departmentNode = new TreeItem<>(new DepartmentOrEmployee(department));
                rootNode.getChildren().add(departmentNode);
            } else {
                TreeItem<DepartmentOrEmployee> departmentLeaf = new TreeItem<>(new DepartmentOrEmployee(department));
                departmentNode.getChildren().add(departmentLeaf);
            }
        }
    }

    // Helper method to create employee nodes and add them to the tree
    private void createEmployeeNodes() {
        for (Employee employee : employees) {
            TreeItem<DepartmentOrEmployee> departmentNode = findDepartmentNode(rootNode, employee.getDepartment());

            if (departmentNode != null) {
                TreeItem<DepartmentOrEmployee> employeeLeaf = new TreeItem<>(new DepartmentOrEmployee(employee));
                departmentNode.getChildren().add(employeeLeaf);
            }
        }
    }

    // Helper method to find a department node in the tree
    private TreeItem<DepartmentOrEmployee> findDepartmentNode(TreeItem<DepartmentOrEmployee> parent, Department department) {
        for (TreeItem<DepartmentOrEmployee> departmentNode : parent.getChildren()) {
            if (!departmentNode.getValue().isEmployee() && departmentNode.getValue().getDepartment().getName().equals(department.getName())) {
                return departmentNode;
            }

            TreeItem<DepartmentOrEmployee> foundInChild = findDepartmentNode(departmentNode, department);
            if (foundInChild != null) {
                return foundInChild;
            }
        }
        return null;
    }

    private TableColumn<Employee, String> createTableColumn(String text, String property, Integer width) {
        TableColumn<Employee, String> column = new TableColumn<>(text);
        column.setMinWidth(width);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    private TableColumn<Employee, Void> createDeleteColumn() {
        TableColumn<Employee, Void> del = new TableColumn<>("Delete");
        del.setMinWidth(50);

        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory = p -> new TableCell<>() {
            private final Button editButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    TEmployees.getInstance().delete(employee.getId());
                    employees = TEmployees.getInstance().findAll();
                    table.setItems(employees);
                    table.refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        };
        del.setCellFactory(cellFactory);

        return del;
    }

    private TableColumn<Employee, Void> createEditColumn() {
        TableColumn<Employee, Void> edit = new TableColumn<>("Edit");
        edit.setMinWidth(50);

        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory = p -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
//                    Employee employee = getTableView().getItems().get(getIndex());
//                    TEmployees.getInstance().delete(employee.getId());
//                    employees = TEmployees.getInstance().findAll();
//                    table.setItems(employees);
//                    table.refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        };
        edit.setCellFactory(cellFactory);

        return edit;
    }
}
