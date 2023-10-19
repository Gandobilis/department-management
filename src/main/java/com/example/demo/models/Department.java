package com.example.demo.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Department {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;

    public Department(String name) {
        this(-1, name);
    }

    public Department(Integer id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    public Integer getId() {
        assert id != null;
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
