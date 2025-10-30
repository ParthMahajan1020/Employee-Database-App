import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class EmployeeDatabaseGUI extends JFrame {
    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employee_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "your_password";
    
    // GUI Components
    private JTextField txtId, txtName, txtEmail, txtDepartment, txtSalary;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnRefresh;
    
    public EmployeeDatabaseGUI() {
        setTitle("Employee Database Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create table on startup
        createTable();
        
        // Initialize GUI
        initComponents();
        loadEmployeeData();
    }
    
    private void initComponents() {
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("EMPLOYEE DATABASE MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Employee Information"));
        formPanel.setBackground(new Color(236, 240, 241));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(15);
        txtId.setEditable(false);
        formPanel.add(txtId, gbc);
        
        // Name
        gbc.gridx = 2; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 3;
        txtName = new JTextField(15);
        formPanel.add(txtName, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(15);
        formPanel.add(txtEmail, gbc);
        
        // Department
        gbc.gridx = 2; gbc.gridy = 1;
        formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 3;
        txtDepartment = new JTextField(15);
        formPanel.add(txtDepartment, gbc);
        
        // Salary
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Salary:"), gbc);
        gbc.gridx = 1;
        txtSalary = new JTextField(15);
        formPanel.add(txtSalary, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(236, 240, 241));
        
        btnAdd = createStyledButton("Add", new Color(46, 204, 113));
        btnUpdate = createStyledButton("Update", new Color(52, 152, 219));
        btnDelete = createStyledButton("Delete", new Color(231, 76, 60));
        btnClear = createStyledButton("Clear", new Color(149, 165, 166));
        btnRefresh = createStyledButton("Refresh", new Color(241, 196, 15));
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnRefresh);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 4;
        formPanel.add(buttonPanel, gbc);
        
        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Employee Records"));
        
        String[] columnNames = {"ID", "Name", "Email", "Department", "Salary"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add listeners
        btnAdd.addActionListener(e -> addEmployee());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());
        btnClear.addActionListener(e -> clearFields());
        btnRefresh.addActionListener(e -> loadEmployeeData());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                loadSelectedEmployee();
            }
        });
        
        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(100, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    private void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS employees (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "department VARCHAR(50), " +
                "salary DECIMAL(10, 2))";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            showError("Error creating table: " + e.getMessage());
        }
    }
    
    private void loadEmployeeData() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM employees";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("department"),
                    String.format("$%.2f", rs.getDouble("salary"))
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            showError("Error loading data: " + e.getMessage());
        }
    }
    
    private void addEmployee() {
        if (!validateFields()) return;
        
        String sql = "INSERT INTO employees (name, email, department, salary) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, txtName.getText().trim());
            pstmt.setString(2, txtEmail.getText().trim());
            pstmt.setString(3, txtDepartment.getText().trim());
            pstmt.setDouble(4, Double.parseDouble(txtSalary.getText().trim()));
            
            pstmt.executeUpdate();
            showSuccess("Employee added successfully!");
            clearFields();
            loadEmployeeData();
        } catch (SQLException e) {
            showError("Error adding employee: " + e.getMessage());
        }
    }
    
    private void updateEmployee() {
        if (txtId.getText().isEmpty()) {
            showError("Please select an employee to update!");
            return;
        }
        
        if (!validateFields()) return;
        
        String sql = "UPDATE employees SET name = ?, email = ?, department = ?, salary = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, txtName.getText().trim());
            pstmt.setString(2, txtEmail.getText().trim());
            pstmt.setString(3, txtDepartment.getText().trim());
            pstmt.setDouble(4, Double.parseDouble(txtSalary.getText().trim()));
            pstmt.setInt(5, Integer.parseInt(txtId.getText()));
            
            pstmt.executeUpdate();
            showSuccess("Employee updated successfully!");
            clearFields();
            loadEmployeeData();
        } catch (SQLException e) {
            showError("Error updating employee: " + e.getMessage());
        }
    }
    
    private void deleteEmployee() {
        if (txtId.getText().isEmpty()) {
            showError("Please select an employee to delete!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this employee?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) return;
        
        String sql = "DELETE FROM employees WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(txtId.getText()));
            pstmt.executeUpdate();
            showSuccess("Employee deleted successfully!");
            clearFields();
            loadEmployeeData();
        } catch (SQLException e) {
            showError("Error deleting employee: " + e.getMessage());
        }
    }
    
    private void loadSelectedEmployee() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            txtId.setText(table.getValueAt(selectedRow, 0).toString());
            txtName.setText(table.getValueAt(selectedRow, 1).toString());
            txtEmail.setText(table.getValueAt(selectedRow, 2).toString());
            txtDepartment.setText(table.getValueAt(selectedRow, 3).toString());
            String salary = table.getValueAt(selectedRow, 4).toString().replace("$", "");
            txtSalary.setText(salary);
        }
    }
    
    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtDepartment.setText("");
        txtSalary.setText("");
        table.clearSelection();
    }
    
    private boolean validateFields() {
        if (txtName.getText().trim().isEmpty()) {
            showError("Please enter employee name!");
            return false;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            showError("Please enter email!");
            return false;
        }
        if (txtDepartment.getText().trim().isEmpty()) {
            showError("Please enter department!");
            return false;
        }
        try {
            double salary = Double.parseDouble(txtSalary.getText().trim());
            if (salary < 0) {
                showError("Salary must be positive!");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid salary!");
            return false;
        }
        return true;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            EmployeeDatabaseGUI frame = new EmployeeDatabaseGUI();
            frame.setVisible(true);
        });
    }
}