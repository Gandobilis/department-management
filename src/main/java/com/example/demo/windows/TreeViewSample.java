package com.example.demo.windows;

import com.example.demo.models.Department;
import com.example.demo.models.Employee;
import com.example.demo.tables.TDepartments;
import com.example.demo.tables.TEmployees;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.stream.Collectors;

public class TreeViewSample extends Application {
    ObservableList<Department> departments = TDepartments.getInstance().findAll();
    ObservableList<Employee> employees = TEmployees.getInstance().findAll();
    TreeItem<String> rootNode =
            new TreeItem<>("Departments");

    public static void main(String[] args) {
        Application.launch(args);
    }

    private TreeItem<String> findDepartmentNode(TreeItem<String> parent, String departmentName) {
        for (TreeItem<String> departmentNode : parent.getChildren()) {
            if (departmentNode.getValue().equals(departmentName)) {
                return departmentNode;
            }

            TreeItem<String> foundInChild = findDepartmentNode(departmentNode, departmentName);
            if (foundInChild != null) {
                return foundInChild;
            }
        }
        return null;
    }

    @Override
    public void start(Stage stage) {
        rootNode.setExpanded(true);
        TreeItem<String> rootNode = new TreeItem<>("Root Department");
        for (Department department : departments) {
            TreeItem<String> departmentNode = findDepartmentNode(rootNode, department.getParentDepartment().getName());

            if (departmentNode == null) {
                departmentNode = new TreeItem<>(department.getParentDepartment().getName());
                rootNode.getChildren().add(departmentNode);
            }

            TreeItem<String> empLeaf = new TreeItem<>(department.getName());

            // Add employees as child nodes of the department
            departmentNode.getChildren().add(empLeaf);
        }

        for (Employee employee : employees) {
            TreeItem<String> departmentNode = findDepartmentNode(rootNode, employee.getDepartment().getName());

            TreeItem<String> empLeaf = new TreeItem<>(employee.getFirstName() + " " + employee.getLastName());

            // Add employees as child nodes of the department
            assert departmentNode != null;
            departmentNode.getChildren().add(empLeaf);
        }

        stage.setTitle("Tree View Sample");
        VBox box = new VBox();
        final Scene scene = new Scene(box, 400, 300);
        scene.setFill(Color.LIGHTGRAY);

        TreeView<String> treeView = new TreeView<>(rootNode);
        treeView.setEditable(true);
        treeView.setCellFactory(p -> new TextFieldTreeCellImpl());

        box.getChildren().add(treeView);
        stage.setScene(scene);
        stage.show();
    }

    private final class TextFieldTreeCellImpl extends TreeCell<String> {

        private TextField textField;
        private ContextMenu addMenu = new ContextMenu();

        public TextFieldTreeCellImpl() {
            MenuItem addMenuItem = new MenuItem("Add Employee");
            addMenu.getItems().add(addMenuItem);
            addMenuItem.setOnAction((EventHandler) t -> {
                TreeItem newEmployee =
                        new TreeItem<>("New Employee");
                getTreeItem().getChildren().add(newEmployee);
            });
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(getItem());
            setGraphic(getTreeItem().getGraphic());
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
                    setGraphic(getTreeItem().getGraphic());
                    if (
                            !getTreeItem().isLeaf() && getTreeItem().getParent() != null
                    ) {
                        setContextMenu(addMenu);
                    }
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnKeyReleased(t -> {
                if (t.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            });

        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}
