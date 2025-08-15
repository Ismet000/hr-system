package com.ismet.domain;


import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

public class Employee extends AbstractEntity {

    @SerializedName("employee_name")
    private String employeeName;

    @SerializedName("employee_last_name")
    private String employeeLastName;

    @SerializedName("employee_email")
    private String employeeEmail;

    public Employee() {
    }

    public Employee(String employeeName, String employeeLastName, String employeeEmail) {
        this.employeeName = employeeName;
        this.employeeLastName = employeeLastName;
        this.employeeEmail = employeeEmail;
    }

    @Override
    public String validate() {
        if (employeeName == null || employeeName.isEmpty()) {
            return "Employee name is mandatory";
        }
        if (employeeLastName == null || employeeLastName.isEmpty()) {
            return "Employee last name is mandatory";
        }
        if (employeeEmail == null || employeeEmail.isEmpty()) {
            return "Employee email is mandatory";
        }
        return null;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }
}
