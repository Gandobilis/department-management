package com.example.demo.windows;

import com.example.demo.models.Department;
import com.example.demo.models.DepartmentOrEmployee;
import com.example.demo.models.Employee;
import com.example.demo.tables.TDepartments;
import com.example.demo.tables.TEmployees;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TreeViewSample extends Application {
    ObservableList<Department> departments = TDepartments.getInstance().findAll();
    ObservableList<Employee> employees = TEmployees.getInstance().findAll();
    TreeItem<DepartmentOrEmployee> rootNode = new TreeItem<>(new DepartmentOrEmployee(new Department("Departments")));

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Create tree nodes for departments and employees
        createDepartmentNodes();
//        createEmployeeNodes();

        // Set up the JavaFX scene
        VBox box = new VBox();
        Scene scene = new Scene(box, 400, 300);
        scene.setFill(Color.LIGHTGRAY);

        TreeView<DepartmentOrEmployee> treeView = new TreeView<>(rootNode);

        box.getChildren().add(treeView);
        stage.setScene(scene);
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
            boolean isParentDepartment = departmentNode.getValue().getDepartment().getName().equals(department.getName());
            if (isParentDepartment) {
                return departmentNode;
            }

            TreeItem<DepartmentOrEmployee> foundInChild = findDepartmentNode(departmentNode, department);
            if (foundInChild != null) {
                return foundInChild;
            }
        }
        return null;
    }
}
