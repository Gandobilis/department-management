package com.example.demo.models;

import javafx.collections.ObservableList;

public interface Model<T> {
    T findById(Integer id);

    ObservableList<T> findAll();

    void create(T model);

    void update(T model);

    void delete(Integer id);
}