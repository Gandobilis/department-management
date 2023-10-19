package com.example.demo.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Employee {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private Department department;

    public Employee(String fName, String lName) {
        this(-1, fName, lName);
    }

    public Employee(Integer id, String fName, String lName) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(fName);
        this.lastName = new SimpleStringProperty(lName);
    }

    public

    public Integer getId() {
        assert id != null;
        return id.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String fName) {
        firstName.set(fName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String fName) {
        lastName.set(fName);
    }
}
