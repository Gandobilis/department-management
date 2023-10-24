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

    public boolean isDepartment() {
        return department != null;
    }

    public boolean isEmployee() {
        return employee != null;
    }

    @Override
    public String toString() {
        return department == null ? employee.toString() : department.toString();
    }
}
