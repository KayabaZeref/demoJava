/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WS45;

/**
 *
 * @author Admin
 */
public class Employee {

    String empCode, empName;
    double salary;

    public Employee(String empCode, String empName, double salary) {
        this.empCode = empCode;
        this.empName = empName;
        this.salary = salary;
    }

    public String getEmpCode() {
        return empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public double getSalary() {
        return salary;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return empCode + "-" + empName;

    }
}
