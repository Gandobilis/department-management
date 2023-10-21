package com.example.demo.tables;

import javafx.collections.ObservableList;

public interface Table<T> {
    T findById(Integer id);

    ObservableList<T> findAll();

    void create(T model);

    void update(T model);

    void delete(Integer id);
}