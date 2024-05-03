import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.sql.*;

public class Employee extends JFrame {
    JTextField idField;
    JTextField nameField;
    JTextField contactNumberField;
    JTextField joinDateField;
    JTextField leaveDateField;
    JTextField emailField;
    JTextField salaryField;
    JTextField workingHoursField;
    JTextField hourlyPayField;
    JButton createButton, updateButton, deleteButton, searchButton,backToHomepageButton;
    DefaultTableModel model;
    JTable table;

    Connection connection;

    public Employee() {
        try {
            // Establish database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/isp3", "root", "Harsh_123");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
            System.exit(1); // Exit the program if database connection fails
        }

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 50, 100, 30);
        add(nameLabel);
        

        nameField = new JTextField();
        nameField.setBounds(160, 50, 200, 30);
        add(nameField);
        
        idField = new JTextField();
        idField.setBounds(160, 450, 200, 30);
        add(idField);

        JLabel contactNumberLabel = new JLabel("Contact Number:");
        contactNumberLabel.setBounds(50, 90, 100, 30);
        add(contactNumberLabel);

        contactNumberField = new JTextField();
        contactNumberField.setBounds(160, 90, 200, 30);
        add(contactNumberField);

        JLabel joinDateLabel = new JLabel("Join Date:");
        joinDateLabel.setBounds(50, 130, 100, 30);
        add(joinDateLabel);

        joinDateField = new JTextField();
        joinDateField.setBounds(160, 130, 200, 30);
        add(joinDateField);

        JLabel leaveDateLabel = new JLabel("Leave Date:");
        leaveDateLabel.setBounds(50, 170, 100, 30);
        add(leaveDateLabel);

        leaveDateField = new JTextField();
        leaveDateField.setBounds(160, 170, 200, 30);
        add(leaveDateField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 210, 100, 30);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(160, 210, 200, 30);
        add(emailField);

        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setBounds(50, 250, 100, 30);
        add(salaryLabel);

        salaryField = new JTextField();
        salaryField.setBounds(160, 250, 200, 30);
        add(salaryField);

        JLabel workingHoursLabel = new JLabel("Working Hours:");
        workingHoursLabel.setBounds(50, 290, 100, 30);
        add(workingHoursLabel);

        workingHoursField = new JTextField();
        workingHoursField.setBounds(160, 290, 200, 30);
        add(workingHoursField);

        JLabel hourlyPayLabel = new JLabel("Hourly Pay:");
        hourlyPayLabel.setBounds(50, 330, 100, 30);
        add(hourlyPayLabel);

        hourlyPayField = new JTextField();
        hourlyPayField.setBounds(160, 330, 200, 30);
        add(hourlyPayField);

        createButton = new JButton("Create");
        createButton.setBounds(50, 380, 100, 30);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fieldsFilled()) {
                    insertEmployee();
                } else {
                    JOptionPane.showMessageDialog(Employee.this, "Please fill all fields.");
                }
            }
        });
        add(createButton);

        updateButton = new JButton("Update");
        updateButton.setBounds(170, 380, 100, 30);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fieldsFilled()) {
                    updateEmployee();
                } else {
                    JOptionPane.showMessageDialog(Employee.this, "Please fill all fields.");
                }
            }
        });
        add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(290, 380, 100, 30);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(Employee.this, "Please select an employee to delete.");
                } else {
                    deleteEmployee();
                }
            }
        });
        add(deleteButton);
        try {
            Image logo = Toolkit.getDefaultToolkit().getImage("ISP1.jpg");
            setIconImage(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        backToHomepageButton = new JButton("Back to Homepage");
        backToHomepageButton.setBounds(170, 420, 150, 30);
        backToHomepageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current window
                dispose();
                // Open or switch to the homepage window
                Homepage homepage = new Homepage(); // Assuming you have a class named Homepage for the homepage window
                homepage.setVisible(true);
            }
        });
        add(backToHomepageButton);

        // Table and model
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(400, 50, 400, 380);
        add(scrollPane);

        // Table columns
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Contact Number");
        model.addColumn("Join Date");
        model.addColumn("Leave Date");
        model.addColumn("Email");
        model.addColumn("Salary");
        model.addColumn("Working Hours");
        model.addColumn("Hourly Pay");

        // Fetch existing employees and populate the table
        fetchEmployees();

        // Listener for table row selection
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        fillFields(selectedRow);
                    }
                }
            }
        });

        setSize(850, 900);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // Method to fetch employees from the database and populate the table
    private void fetchEmployees() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Employee");
            ResultSet resultSet = statement.executeQuery();

            model.setRowCount(0);

            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getInt("employee_id"),
                        resultSet.getString("name"),
                        resultSet.getString("contact_number"),
                        resultSet.getString("join_date"),
                        resultSet.getString("leave_date"),
                        resultSet.getString("email"),
                        resultSet.getDouble("salary"),
                        resultSet.getInt("working_hours"),
                        resultSet.getDouble("hourly_pay")
                };
                model.addRow(rowData);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred.");
        }
    }

    // Method to fill text fields with data from the selected row in the table
    private void fillFields(int rowIndex) {
        int selectedId = (int) model.getValueAt(rowIndex, 0);
        String name = (String) model.getValueAt(rowIndex, 1);
        String contactNumber = (String) model.getValueAt(rowIndex, 2);
        String joinDate = (String) model.getValueAt(rowIndex, 3);
        String leaveDate = (String) model.getValueAt(rowIndex, 4);
        String email = (String) model.getValueAt(rowIndex, 5);
        double salary = (double) model.getValueAt(rowIndex, 6);
        int workingHours = (int) model.getValueAt(rowIndex, 7);
        double hourlyPay = (double) model.getValueAt(rowIndex, 8);

        idField.setText(String.valueOf(selectedId));
        nameField.setText(name);
        contactNumberField.setText(contactNumber);
        joinDateField.setText(joinDate);
        leaveDateField.setText(leaveDate);
        emailField.setText(email);
        salaryField.setText(String.valueOf(salary));
        workingHoursField.setText(String.valueOf(workingHours));
        hourlyPayField.setText(String.valueOf(hourlyPay));
    }

    // Method to check if all text fields are filled
    private boolean fieldsFilled() {
        return !(
                nameField.getText().isEmpty() ||
                contactNumberField.getText().isEmpty() ||
                joinDateField.getText().isEmpty() ||
                leaveDateField.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                salaryField.getText().isEmpty() ||
                workingHoursField.getText().isEmpty() ||
                hourlyPayField.getText().isEmpty()
        );
    }

    // Method to insert a new employee
    private void insertEmployee() {
        // Retrieve values from text fields
        String name = nameField.getText();
        String contactNumber = contactNumberField.getText();
        String joinDate = joinDateField.getText();
        String leaveDate = leaveDateField.getText();
        String email = emailField.getText();
        double salary = Double.parseDouble(salaryField.getText());
        int workingHours = Integer.parseInt(workingHoursField.getText());
        double hourlyPay = Double.parseDouble(hourlyPayField.getText());

        try {
            // Prepare SQL statement
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Employee (name, contact_number, join_date, leave_date, email, salary, working_hours, hourly_pay) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, contactNumber);
            statement.setString(3, joinDate);
            statement.setString(4, leaveDate);
            statement.setString(5, email);
            statement.setDouble(6, salary);
            statement.setInt(7, workingHours);
            statement.setDouble(8, hourlyPay);

            // Execute SQL statement
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Employee created successfully.");
                fetchEmployees(); // Refresh table after insertion
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create employee.");
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while inserting employee.");
        }
    }

    // Method to update employee information
    private void updateEmployee() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String contactNumber = contactNumberField.getText();
            String joinDate = joinDateField.getText();
            String leaveDate = leaveDateField.getText();
            String email = emailField.getText();
            double salary = Double.parseDouble(salaryField.getText());
            int workingHours = Integer.parseInt(workingHoursField.getText());
            double hourlyPay = Double.parseDouble(hourlyPayField.getText());

            PreparedStatement statement = connection.prepareStatement("UPDATE Employee SET name=?, contact_number=?, join_date=?, leave_date=?, email=?, salary=?, working_hours=?, hourly_pay=? WHERE employee_id=?");
            statement.setString(1, name);
            statement.setString(2, contactNumber);
            statement.setString(3, joinDate);
            statement.setString(4, leaveDate);
            statement.setString(5, email);
            statement.setDouble(6, salary);
            statement.setInt(7, workingHours);
            statement.setDouble(8, hourlyPay);
            statement.setInt(9, id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Employee information updated successfully.");
                fetchEmployees(); // Refresh table after update
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update employee information.");
            }
        } catch (NumberFormatException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while updating employee information.");
        }
    }

    // Method to delete an employee
    private void deleteEmployee() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());

                PreparedStatement statement = connection.prepareStatement("DELETE FROM Employee WHERE employee_id=?");
                statement.setInt(1, id);

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
                    fetchEmployees(); // Refresh table after deletion
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete employee.");
                }
            } catch (NumberFormatException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error occurred while deleting employee.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Employee::new);
    }
}
