package com.example.demo.tables;

import lombok.Getter;

@Getter
public class Singleton<T> {
    private static Singleton<?> instance;

    private T value;

    private Singleton() {
    }

    @SuppressWarnings("unchecked")
    public static <T> Singleton<T> getInstance() {
        if (instance == null) {
            instance = new Singleton<>();
        }
        return (Singleton<T>) instance;
    }
}