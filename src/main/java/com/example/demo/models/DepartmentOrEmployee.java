package com.example.demo.models;

import lombok.Getter;

@Getter
public class DepartmentOrEmployee {
    private Department department;
    private Employee employee;

    public DepartmentOrEmployee(Department department) {
        this.department = department;
    }

    public DepartmentOrEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return department == null ? employee.toString() : department.toString();
    }
}
