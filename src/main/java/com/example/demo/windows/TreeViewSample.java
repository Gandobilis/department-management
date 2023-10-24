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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Optional;

public class TreeViewSample extends Application {
    private final TableView<Employee> table = new TableView<>();
    private ObservableList<Department> departments = TDepartments.getInstance().findAll();
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

        ContextMenu employeeContextMenu = createEmployeeContextMenu();
        ContextMenu departmentContextMenu = createDepartmentContextMenu();

        // Set context menus for tree view nodes
        treeView.setContextMenu(null); // Disable default context menu
        treeView.setOnContextMenuRequested(event -> {
            TreeItem<DepartmentOrEmployee> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (selectedItem.getValue().isEmployee()) {
                    treeView.setContextMenu(employeeContextMenu);
                } else {
                    treeView.setContextMenu(departmentContextMenu);
                }
            }
        });

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
                    Employee employee = getTableView().getItems().get(getIndex());
                    openEditEmployeeDialog(employee);
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

    private void openEditEmployeeDialog(Employee employee) {
        // Create a new dialog
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Edit Employee");

        // Set the button types (OK and Cancel)
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create a form for editing employee details
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField firstNameField = new TextField();
        firstNameField.setText(employee.getFirstName());
        TextField lastNameField = new TextField();
        lastNameField.setText(employee.getLastName());

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to an Employee object when the Save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                employee.setFirstName(firstNameField.getText());
                employee.setLastName(lastNameField.getText());
                // Update the employee in your data source here (e.g., TEmployees.getInstance().update(employee))
                // Update the table view
                table.refresh();
                // You may want to save the changes to your data source here
                // For example, you can call TEmployees.getInstance().update(employee);
                return employee;
            }
            return null;
        });

        Optional<Employee> result = dialog.showAndWait();
    }

    private ContextMenu createEmployeeContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addEmployeeItem = new MenuItem("Add Employee");
        addEmployeeItem.setOnAction(event -> openAddEmployeeDialog());
        contextMenu.getItems().add(addEmployeeItem);
        return contextMenu;
    }

    // Helper method to create context menu for adding Department
    private ContextMenu createDepartmentContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addDepartmentItem = new MenuItem("Add Department");
        addDepartmentItem.setOnAction(event -> openAddDepartmentDialog());
        contextMenu.getItems().add(addDepartmentItem);
        return contextMenu;
    }

    // Create and show dialog for adding a new Employee
    private void openAddEmployeeDialog() {
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Add Employee");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        ComboBox<Department> departmentComboBox = new ComboBox<>(departments);

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Department:"), 0, 2);
        grid.add(departmentComboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                Department selectedDepartment = departmentComboBox.getValue();

                if (firstName.isEmpty() || lastName.isEmpty() || selectedDepartment == null) {
                    // You can show an error message here if any field is empty
                    return null;
                }

                Employee newEmployee = new Employee(firstName, lastName, selectedDepartment);
                // Save the new employee to your data source (e.g., TEmployees.getInstance().add(newEmployee))
                employees = TEmployees.getInstance().findAll();
                table.setItems(employees);
                return newEmployee;
            }
            return null;
        });

        dialog.showAndWait();
    }

    // Create and show dialog for adding a new Department
    private void openAddDepartmentDialog() {
        Dialog<Department> dialog = new Dialog<>();
        dialog.setTitle("Add Department");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField departmentNameField = new TextField();
        ComboBox<Department> parentDepartmentComboBox = new ComboBox<>(departments);

        grid.add(new Label("Department Name:"), 0, 0);
        grid.add(departmentNameField, 1, 0);
        grid.add(new Label("Parent Department:"), 0, 1);
        grid.add(parentDepartmentComboBox, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                String departmentName = departmentNameField.getText();
                Department parentDepartment = parentDepartmentComboBox.getValue();

                if (departmentName.isEmpty()) {
                    // You can show an error message here if the department name is empty
                    return null;
                }

                Department newDepartment = new Department(departmentName, parentDepartment);
                // Save the new department to your data source (e.g., TDepartments.getInstance().add(newDepartment))
                departments = TDepartments.getInstance().findAll();
                createDepartmentNodes(); // Update the tree view
                return newDepartment;
            }
            return null;
        });

        dialog.showAndWait();
    }
}
