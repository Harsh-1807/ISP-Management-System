import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class AdminPanel extends JFrame {
    JButton manageCustomersButton;
    JButton seeComplaintsButton;
    JButton viewCurrentPlansButton;

    Connection connection;

    public AdminPanel() {
        try {
            // Establish database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/isp3", "root", "Harsh_123");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
            System.exit(1); // Exit the program if database connection fails
        }

        manageCustomersButton = new JButton("Manage Customers");
        manageCustomersButton.setBounds(50, 50, 200, 30);
        manageCustomersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manageCustomers();
            }
        });
        add(manageCustomersButton);

        seeComplaintsButton = new JButton("See Complaints");
        seeComplaintsButton.setBounds(50, 100, 150, 30);
        seeComplaintsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seeComplaints();
            }
        });
        add(seeComplaintsButton);

        viewCurrentPlansButton = new JButton("View Current Plans");
        viewCurrentPlansButton.setBounds(50, 150, 200, 30);
        viewCurrentPlansButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewCurrentPlans();
            }
        });
        add(viewCurrentPlansButton);

        setSize(300, 250);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // Method to manage customers
    private void manageCustomers() {
        CustomerManagementPanel customerManagementPanel = new CustomerManagementPanel(connection);
        customerManagementPanel.setVisible(true);
    }

    // Method to see complaints
    private void seeComplaints() {
        // Code to open a new window or dialog to display complaints
        // You can reuse the CustomerPanel code here
        CustomerPanel customerPanel = new CustomerPanel();
        customerPanel.setVisible(true);
    }

    // Method to view current plans of all users
    private void viewCurrentPlans() {
        // Code to view current plans
        // For simplicity, just a placeholder
        JOptionPane.showMessageDialog(this, "View Current Plans functionality is not implemented yet.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPanel());
    }
}

class CustomerManagementPanel extends JFrame {
    JButton addButton;
    JButton deleteButton;
    JButton updateButton;

    JTextField nameField;
    JTextField contactNumberField;
    JTextField emailField;

    Connection connection;

    public CustomerManagementPanel(Connection connection) {
        this.connection = connection;

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 50, 100, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(160, 50, 200, 30);
        add(nameField);

        JLabel contactNumberLabel = new JLabel("Contact Number:");
        contactNumberLabel.setBounds(50, 100, 100, 30);
        add(contactNumberLabel);

        contactNumberField = new JTextField();
        contactNumberField.setBounds(160, 100, 200, 30);
        add(contactNumberField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 150, 100, 30);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(160, 150, 200, 30);
        add(emailField);

        addButton = new JButton("Add");
        addButton.setBounds(50, 200, 100, 30);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });
        add(addButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(160, 200, 100, 30);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });
        add(deleteButton);

        updateButton = new JButton("Update");
        updateButton.setBounds(270, 200, 100, 30);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCustomer();
            }
        });
        add(updateButton);

        setSize(400, 300);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void addCustomer() {
        String name = nameField.getText();
        String contactNumber = contactNumberField.getText();
        String email = emailField.getText();

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO User (name, contact_number, email) VALUES (?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, contactNumber);
            statement.setString(3, email);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Customer added successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add customer.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while adding customer.");
        }
    }

    private void deleteCustomer() {
        // Code to delete customer
        // For simplicity, just a placeholder
        JOptionPane.showMessageDialog(this, "Delete Customer functionality is not implemented yet.");
    }

    private void updateCustomer() {
        // Code to update customer
        // For simplicity, just a placeholder
        JOptionPane.showMessageDialog(this, "Update Customer functionality is not implemented yet.");
    }
}