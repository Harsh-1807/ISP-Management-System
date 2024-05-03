import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.sql.*;

public class CustomerPanel extends JFrame {
    JButton viewComplaintsButton, viewUsersButton, viewPlansButton, backToHomepageButton; // Added backToHomepageButton
    DefaultTableModel model;
    JTable table;

    Connection connection;

    public CustomerPanel() {
        try {
            // Establish database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/isp3", "root", "Harsh_123");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
            System.exit(1); // Exit the program if database connection fails
        }

        // Setting up the JFrame
        setTitle("Customer Panel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(null);

        // Set background color
        getContentPane().setBackground(new Color(240, 240, 240));

        // Buttons for different functionalities
        viewComplaintsButton = new JButton("View Complaints");
        viewComplaintsButton.setBounds(50, 50, 150, 30);
        viewComplaintsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewComplaints();
            }
        });
        add(viewComplaintsButton);

        viewUsersButton = new JButton("View Users");
        viewUsersButton.setBounds(50, 100, 150, 30);
        viewUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewUsers();
            }
        });
        add(viewUsersButton);

        viewPlansButton = new JButton("View Plans");
        viewPlansButton.setBounds(50, 150, 150, 30);
        viewPlansButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPlans();
            }
        });
        add(viewPlansButton);
        try {
            Image logo = Toolkit.getDefaultToolkit().getImage("ISP1.jpg");
            setIconImage(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Homepage Button
        backToHomepageButton = new JButton("Back to Homepage");
        backToHomepageButton.setBounds(50, 200, 150, 30);
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
        table.setBorder(new LineBorder(Color.BLACK)); // Add border to table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(250, 50, 600, 400);
        add(scrollPane);

        setVisible(true);
    }
    // Method to fetch and display complaints
    private void viewComplaints() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM complaints");
            ResultSet resultSet = statement.executeQuery();

            // Clear previous table data
            model.setRowCount(0);

            // Add column names
            model.setColumnIdentifiers(new Object[]{"Complaint ID", "Username", "Complaint Text"});

            // Add complaints to table
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getInt("complaint_id"),
                        resultSet.getString("username"),
                        resultSet.getString("complaint_text")
                };
                model.addRow(rowData);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while fetching complaints.");
        }
    }

    // Method to fetch and display users
    private void viewUsers() {
        try {
            // Clear previous table data
            model.setRowCount(0);

            // Add column names
            model.setColumnIdentifiers(new Object[]{"Plan ID", "Plan Name", "Customer Count"});

            // Fetch users
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Plan_Customer_Count");
            ResultSet resultSet = statement.executeQuery();

            // Add users to table
            while (resultSet.next()) {
                Object[] rowData = {
                    resultSet.getInt("plan_id"),
                    resultSet.getString("plan_name"),
                    resultSet.getInt("customer_count")
                };
                model.addRow(rowData);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while fetching users or counting customers for plans.");
        }
    }

    // Method to fetch and display plans
    private void viewPlans() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Plan");
            ResultSet resultSet = statement.executeQuery();

            // Clear previous table data
            model.setRowCount(0);

            // Add column names
            model.setColumnIdentifiers(new Object[]{"Plan ID", "Plan Name", "Speed", "Duration", "Price"});

            // Add plans to table
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getInt("plan_id"),
                        resultSet.getString("plan_name"),
                        resultSet.getInt("speed"),
                        resultSet.getString("duration"),
                        resultSet.getDouble("price")
                };
                model.addRow(rowData);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while fetching plans.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerPanel::new);
    }
}
