package com.employee.management.demo;
import com.employee.management.demo.Employee;

import java.util.List;

public interface EmployeeService {
    void createEmployee(Employee employee);
    void updateEmployee(Employee updatedEmployee);
    List<Employee> getAllEmployees();

    void deleteEmployee(int employeeId);

    Employee getEmployeeById(int employeeId);
}
