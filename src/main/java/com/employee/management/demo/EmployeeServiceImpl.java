package com.employee.management.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final DataSource dataSource;

    @Autowired
    public EmployeeServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createEmployee(Employee employee) {
        String sql = "INSERT INTO employee (name, designation, status) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertEmployeeStmt = connection.prepareStatement(sql)) {

            insertEmployeeStmt.setString(1, employee.getName());
            insertEmployeeStmt.setString(2, employee.getDesignation());
            insertEmployeeStmt.setBoolean(3, employee.isStatus());

            // Execute the query
            int rowsAffected = insertEmployeeStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Employee created successfully!");
            } else {
                System.out.println("Failed to create employee.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException according to your application's error-handling strategy.
        }
    }
    @Override
    public void updateEmployee(Employee updatedEmployee) {
        try {
            int employeeId = updatedEmployee.getId();

            String sql = "UPDATE employee SET name = ?, designation = ?, status = ? WHERE id = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement updateEmployeeStmt = connection.prepareStatement(sql)) {

                updateEmployeeStmt.setString(1, updatedEmployee.getName());
                updateEmployeeStmt.setString(2, updatedEmployee.getDesignation());
                updateEmployeeStmt.setBoolean(3, updatedEmployee.isStatus());
                updateEmployeeStmt.setInt(4, employeeId);

                // Execute the query
                int rowsAffected = updateEmployeeStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Employee updated successfully!");
                } else {
                    System.out.println("No employee found with id: " + employeeId);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the SQLException according to your application's error-handling strategy.
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Handle other exceptions, if any
        }
    }@Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE deletestatus = false";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectAllEmployeesStmt = connection.prepareStatement(sql);
             ResultSet resultSet = selectAllEmployeesStmt.executeQuery()) {

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getInt("id"));
                employee.setName(resultSet.getString("name"));
                employee.setDesignation(resultSet.getString("designation"));
                employee.setStatus(resultSet.getBoolean("status"));
                employee.setDeletestatus(resultSet.getBoolean("deletestatus"));
                // Set other properties here...
                employees.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException according to your application's error-handling strategy.
        }

        return employees;
    }
    @Override
    public void deleteEmployee(int employeeId) {
        String sql = "UPDATE employee SET deletestatus = true WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement deleteEmployeeStmt = connection.prepareStatement(sql)) {

            deleteEmployeeStmt.setLong(1, employeeId);

            int rowsAffected = deleteEmployeeStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Employee deleted successfully!");
            } else {
                System.out.println("Failed to delete employee.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException according to your application's error-handling strategy.
        }
    }

    @Override
    public Employee getEmployeeById(int employeeId) {
        String sql = "SELECT * FROM employee WHERE id = ? AND deletestatus = false";
        Employee employee = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectEmployeeStmt = connection.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            selectEmployeeStmt.setLong(1, employeeId);

            try (ResultSet resultSet = selectEmployeeStmt.executeQuery()) {
                if (resultSet.next()) {
                    employee = new Employee();
                    // Set properties of the Employee object based on the columns in your table
                    employee.setId(resultSet.getInt("id"));
                    employee.setName(resultSet.getString("name"));
                    employee.setDesignation(resultSet.getString("designation"));
                    employee.setStatus(resultSet.getBoolean("status"));
                    employee.setDeletestatus(resultSet.getBoolean("deletestatus"));
                    // Set other properties here...
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException according to your application's error-handling strategy.
        }

        return employee;
    }
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Override
//    public List<Employee> getAllEmployees() {
//        return employeeRepository.findAll();
//    }

}
