package com.targetindia.ui;

import com.targetindia.model.Customer;
import com.targetindia.service.CustomerService;
import com.targetindia.service.ServiceException;
import com.targetindia.utils.KeyboardUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) {
        new Main().start();
    }

    private final CustomerService service;

    public Main() {
        service = new CustomerService();
    }

    public void start() {
        int choice;
        while ((choice = menu()) != 0) {
            switch (choice) {
                case 1:
                    addNewCustomer();
                    break;
                case 2:
                    listAll();
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                    break;
                case 7:
                    updateCustomerData();
                    break;
                case 8:
                    deleteCustomer();
                    break;
                default:
                    System.out.println("Invalid choice. Please retry.");
            }
        }
    }

    private void updateCustomerData() {
        var email = KeyboardUtil.getString("Enter customer email to delete: ");
        try {
            Customer customer = service.getCustomerByEmail(email);
            if (customer == null) {
                System.out.println("No customer data found for this email!");
                return;
            }

            System.out.println("Customer data found. Please enter update information: ");
            String input;
            input = KeyboardUtil.getString("Name          : (" + customer.getName() + ") ");
            if (!input.isBlank()) {
                customer.setName(input);
            }

            input = KeyboardUtil.getString("Email         : (" + customer.getEmail() + ") ");
            if (!input.isBlank()) {
                customer.setEmail(input);
            }

            input = KeyboardUtil.getString("Phone         : (" + customer.getPhone() + ") ");
            if (!input.isBlank()) {
                customer.setPhone(input);
            }

            input = KeyboardUtil.getString("City          : (" + customer.getCity() + ") ");
            if (!input.isBlank()) {
                customer.setCity(input);
            }

            service.updateCustomer(customer);
            System.out.println("Customer data updated successfully!");

        } catch (ServiceException e) {
            log.warn("error while updating customer", e);
            System.out.println("Couldn't update the customer. " + e.getMessage());
        }
    }

    private void deleteCustomer() {
        var email = KeyboardUtil.getString("Enter customer email to delete: ");
        try {
            service.deleteCustomer(email);
        } catch (ServiceException e) {
            log.warn("error while deleting customer", e);
            System.out.println("Couldn't delete the customer. " + e.getMessage());
        }
    }

    private void addNewCustomer() {
        // TODO: accept customer data and call the service method to add
        System.out.println("Enter new customer details: ");
        String name = KeyboardUtil.getString("Name          : ");
        Date birthDate = null;
        try {
            birthDate = KeyboardUtil.getDate("Date of birth : ");
        } catch (Exception e) {
            log.warn("user supplied invalid birth date", e);
        }

        String email = KeyboardUtil.getString("Email         : ");
        String phone = KeyboardUtil.getString("Phone         : ");
        String city = KeyboardUtil.getString("City          : ");
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setCity(city);
        customer.setBirthDate(birthDate);

        try {
            service.addNewCustomer(customer);
            System.out.println("New customer data added successfully!");
            log.trace("New customer data added successfully with id {}", customer.getId());
        } catch (ServiceException e) {
            System.out.println("Something went wrong. Check the logs for more details.");
            log.warn("error while adding customer in the Main", e);
        }


    }

    private void listAll() {
        // TODO: get all customers from service and print in a tabular format
        var list = service.getAllCustomers();
        // printCustomerListAsTable(list);
        for (var c : list) {
            System.out.println(c);
        }
    }

    void printCustomerListAsTable(List<Customer> customers) {
        // TODO: display the customer data in a tabular format as in MYSQL CLI output
    }

    int menu() {
        System.out.println("*** Main Menu ***");
        System.out.println("0. Exit");
        System.out.println("1. Add a new customer data");
        System.out.println("2. List all");
        System.out.println("3. Search by id");
        System.out.println("4. Search by email");
        System.out.println("5. Search by phone");
        System.out.println("6. Search by city");
        System.out.println("7. Update customer data");
        System.out.println("8. Delete customer data");
        try {
            return KeyboardUtil.getInt("Enter your choice: ");
        } catch (Exception e) {
            log.warn("error in choice by user", e);
            return -1;
        }
    }
}
