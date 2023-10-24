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
        for (Department department : departments) {
            TreeItem<DepartmentOrEmployee> departmentNode = findDepartmentNode(rootNode, department);

            if (departmentNode == null) {
                departmentNode = new TreeItem<>(new DepartmentOrEmployee(department));
                rootNode.getChildren().add(departmentNode);
            }

            TreeItem<DepartmentOrEmployee> departmentLeaf = new TreeItem<>(new DepartmentOrEmployee(department));

            departmentNode.getChildren().add(departmentLeaf);
        }

        for (Employee employee : employees) {
            TreeItem<DepartmentOrEmployee> departmentNode = findDepartmentNode(rootNode, employee.getDepartment());

            TreeItem<DepartmentOrEmployee> employeeLeaf = new TreeItem<>(new DepartmentOrEmployee(employee));

            if (departmentNode != null) {
                departmentNode.getChildren().add(employeeLeaf);
            }
        }

        VBox box = new VBox();
        Scene scene = new Scene(box, 400, 300);
        scene.setFill(Color.LIGHTGRAY);

        TreeView<DepartmentOrEmployee> treeView = new TreeView<>(rootNode);

        box.getChildren().add(treeView);
        stage.setScene(scene);
        stage.show();
    }

    private TreeItem<DepartmentOrEmployee> findDepartmentNode(TreeItem<DepartmentOrEmployee> parent, Department department) {
        for (TreeItem<DepartmentOrEmployee> departmentNode : parent.getChildren()) {
            if (departmentNode.getValue().isDepartment() && departmentNode.getValue().getDepartment().getName().equals(department.getName())) {
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
