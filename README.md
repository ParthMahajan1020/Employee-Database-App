# Employee Database Management System (Java + MySQL + Swing)

A desktop-based Employee Management System built using **Java Swing** and **MySQL** for handling employee records through an intuitive GUI.  
This project allows users to **Add, Update, Delete, View, and Manage** employee details in a MySQL database.

---

## 🚀 Features

- **Add New Employee** – Add new employee details with validation checks.  
- **Update Existing Records** – Modify employee data directly through the GUI.  
- **Delete Employee** – Safely delete records with confirmation.  
- **View All Employees** – Display all employees in a clean table view.  
- **Refresh Data** – Instantly reload the employee table.  
- **Auto Table Creation** – The database table is automatically created if not present.  
- **Error & Success Handling** – User-friendly popups for all actions.

---

## 🧠 Tech Stack

- **Language:** Java  
- **GUI Framework:** Swing  
- **Database:** MySQL  
- **JDBC:** For database connectivity

---

## ⚙️ Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/ParthMahajan1020/Employee-Database-App.git
```

### 2. Create MySQL Database
Open your MySQL console and run:
```
CREATE DATABASE employee_db;
USE employee_db;
```

### 3. Update Database Credentials
Inside the EmployeeDatabaseGUI.java file:
```
private static final String DB_URL = "jdbc:mysql://localhost:3306/employee_db";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_password";
```
Replace your_password with your actual MySQL password.

### 4. Add MySQL JDBC Driver
Download the MySQL Connector/J (JAR file) and add it to your project classpath:
```
mysql-connector-j-8.x.x.jar
```

### 5. Run the Project
Compile and run the program:
```
javac EmployeeDatabaseGUI.java
java EmployeeDatabaseGUI
```

## 🧩 Database Schema
The system automatically creates a table named employees if it doesn’t exist:
```
CREATE TABLE employees (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  department VARCHAR(50),
  salary DECIMAL(10,2)
);
```

## 📸 GUI Overview

Main Features:

- Text fields for entering employee details (Name, Email, Department, Salary)
- Action buttons (Add, Update, Delete, Clear, Refresh)
- JTable for displaying all employee records
- Automatic selection handling to edit or delete records

## 🧭 Future Enhancements

- Add Search / Filter feature for employees
- Export employee list to Excel or CSV
- Add Login Authentication for Admin access
- Implement Pagination for large datasets
- Connect with a Cloud Database for remote access

## Author

*Parth Mahajan*  
- BTech Student | Java & Software Development Enthusiast  
- GitHub: https://github.com/ParthMahajan1020  
- Email: parth.mahajan1020@example.com  
- Passionate about building console applications, learning new programming languages, and exploring software projects.
