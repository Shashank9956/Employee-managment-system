import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

// Employee class (Model)
class Employee implements Serializable {
    private int id;
    private String name;
    private double salary;
    private String gender;
    private String contact;
    private String emailId;
    private String performance;

    public Employee(int id, String name, double salary, String gender, String contact, String emailId, String performance) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.gender = gender;
        this.contact = contact;
        this.emailId = emailId;
        this.performance = performance;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }
    public String getPerformance() { return performance; }
    public void setPerformance(String performance) { this.performance = performance; }

    @Override
    public String toString() {
        return "Employee [ID=" + id + ", Name=" + name + ", Salary=" + salary +
                ", Gender=" + gender + ", Contact=" + contact +
                ", EmailID=" + emailId + ", Performance=" + performance + "]";
    }
}

// Utility class for File Operations
class FileUtil {
    private static final String FILE_NAME = "employees.dat";

    public static void createFileIfNotExists() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
                writeEmployees(new ArrayList<>());
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        }
    }

    public static List<Employee> readEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            employees = (List<Employee>) ois.readObject();
        } catch (EOFException e) {
            // File is empty, return empty list
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return employees;
    }

    public static void writeEmployees(List<Employee> employees) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(employees);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}

// Service class for Employee Operations
class EmployeeService {
    private List<Employee> employees;

    public EmployeeService() {
        FileUtil.createFileIfNotExists();
        employees = FileUtil.readEmployees();
    }

    public void addEmployee(Scanner sc) {
        System.out.print("Enter Employee ID: ");
        int id = getIntInput(sc);
        if (findEmployeeById(id) != null) {
            System.out.println("Employee ID already exists!");
            return;
        }
        System.out.print("Enter Name: ");
        String name = sc.next();
        double salary = getValidSalary(sc);

        System.out.print("Enter Gender (Male/Female/Other): ");
        String gender = sc.next();

        System.out.print("Enter Contact Number: ");
        String contact = sc.next();

        String emailId = getValidEmail(sc);

        System.out.print("Enter Performance (Excellent/Good/Average/Poor): ");
        String performance = sc.next();

        Employee emp = new Employee(id, name, salary, gender, contact, emailId, performance);
        employees.add(emp);
        FileUtil.writeEmployees(employees);
        System.out.println("Employee added successfully.");
    }

    public void viewEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            for (Employee emp : employees) {
                System.out.println(emp);
            }
        }
    }

    public void searchEmployee(Scanner sc) {
        System.out.print("Enter Employee ID to search: ");
        int id = getIntInput(sc);
        Employee emp = findEmployeeById(id);
        if (emp != null) {
            System.out.println(emp);
        } else {
            System.out.println("Employee not found.");
        }
    }

    public void updateEmployee(Scanner sc) {
        System.out.print("Enter Employee ID to update: ");
        int id = getIntInput(sc);
        Employee emp = findEmployeeById(id);
        if (emp != null) {
            System.out.print("Enter new Name: ");
            String name = sc.next();
            double salary = getValidSalary(sc);

            System.out.print("Enter new Gender (Male/Female/Other): ");
            String gender = sc.next();

            System.out.print("Enter new Contact Number: ");
            String contact = sc.next();

            String emailId = getValidEmail(sc);

            System.out.print("Enter new Performance (Excellent/Good/Average/Poor): ");
            String performance = sc.next();

            emp.setName(name);
            emp.setSalary(salary);
            emp.setGender(gender);
            emp.setContact(contact);
            emp.setEmailId(emailId);
            emp.setPerformance(performance);

            FileUtil.writeEmployees(employees);
            System.out.println("Employee updated successfully.");
        } else {
            System.out.println("Employee not found.");
        }
    }

    public void deleteEmployee(Scanner sc) {
        System.out.print("Enter Employee ID to delete: ");
        int id = getIntInput(sc);
        Employee emp = findEmployeeById(id);
        if (emp != null) {
            employees.remove(emp);
            FileUtil.writeEmployees(employees);
            System.out.println("Employee deleted successfully.");
        } else {
            System.out.println("Employee not found.");
        }
    }

    private Employee findEmployeeById(int id) {
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }

    public int getIntInput(Scanner sc) {
        while (true) {
            String input = sc.next();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a valid integer: ");
            }
        }
    }

    private double getValidSalary(Scanner sc) {
        while (true) {
            System.out.print("Enter Salary: ");
            String input = sc.next();
            try {
                double salary = Double.parseDouble(input);
                if (salary < 0) {
                    System.out.println("Salary must be positive!");
                } else {
                    return salary;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private String getValidEmail(Scanner sc) {
        while (true) {
            System.out.print("Enter Email ID: ");
            String email = sc.next();
            if (isValidEmail(email)) {
                return email;
            } else {
                System.out.println("Invalid email format. Please enter a valid email.");
            }
        }
    }

    private boolean isValidEmail(String email) {
        // Simple regex for email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }
}

// Main class (UI Layer)
public class EmployeeManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EmployeeService service = new EmployeeService();
        while (true) {
            System.out.println("\n--- Employee Management System ---");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = service.getIntInput(sc);
            switch (choice) {
                case 1: service.addEmployee(sc); break;
                case 2: service.viewEmployees(); break;
                case 3: service.searchEmployee(sc); break;
                case 4: service.updateEmployee(sc); break;
                case 5: service.deleteEmployee(sc); break;
                case 6: System.out.println("Exiting..."); sc.close(); return;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }
}