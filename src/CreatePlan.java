import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class CreatePlan extends JFrame {
    JTextField planIdField;
    JTextField planNameField;
    JTextField speedField;
    JTextField durationField;
    JTextField priceField;
    JButton createButton, updateButton, deleteButton, returnButton; // Added returnButton

    Connection connection;

    public CreatePlan() {
        try {
            // Establish database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/isp3", "root", "Harsh_123");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
            System.exit(1); // Exit the program if database connection fails
        }

        JLabel planIdLabel = new JLabel("Plan ID:");
        planIdLabel.setBounds(50, 50, 100, 30);
        add(planIdLabel);

        planIdField = new JTextField();
        planIdField.setBounds(160, 50, 200, 30);
        add(planIdField);

        JLabel planNameLabel = new JLabel("Plan Name:");
        planNameLabel.setBounds(50, 100, 100, 30);
        add(planNameLabel);

        planNameField = new JTextField();
        planNameField.setBounds(160, 100, 200, 30);
        add(planNameField);

        JLabel speedLabel = new JLabel("Speed:");
        speedLabel.setBounds(50, 150, 100, 30);
        add(speedLabel);

        speedField = new JTextField();
        speedField.setBounds(160, 150, 200, 30);
        add(speedField);

        JLabel durationLabel = new JLabel("Duration:");
        durationLabel.setBounds(50, 200, 100, 30);
        add(durationLabel);

        durationField = new JTextField();
        durationField.setBounds(160, 200, 200, 30);
        add(durationField);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(50, 250, 100, 30);
        add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(160, 250, 200, 30);
        add(priceField);

        createButton = new JButton("Create");
        createButton.setBounds(50, 300, 100, 30);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertPlan();
            }
        });
        add(createButton);

        updateButton = new JButton("Update");
        updateButton.setBounds(160, 300, 100, 30);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePlan();
            }
        });
        add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(270, 300, 100, 30);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePlan();
            }
        });
        add(deleteButton);

        // Add Return to Homepage button
        returnButton = new JButton("Return to Homepage");
        returnButton.setBounds(160, 350, 200, 30);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current window
                dispose();
                // Open or switch to the homepage window
                Homepage homepage = new Homepage(); // Assuming you have a class named Homepage for the homepage window
                homepage.setVisible(true);
            }
        });
        add(returnButton);

        setSize(400, 400);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void insertPlan() {
        // Retrieve values from text fields
        String planId = planIdField.getText();
        String planName = planNameField.getText();
        String speed = speedField.getText();
        String duration = durationField.getText();
        String price = priceField.getText();

        // Check if any field is empty
        if (planId.isEmpty() || planName.isEmpty() || speed.isEmpty() || duration.isEmpty() || price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            // Prepare SQL statement
            PreparedStatement statement = connection.prepareStatement("INSERT INTO plan (plan_id, plan_name, speed, duration, price) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, planId);
            statement.setString(2, planName);
            statement.setString(3, speed);
            statement.setString(4, duration);
            statement.setString(5, price);

            // Execute SQL statement
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Plan created successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create plan.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred.");
        }
    }

    private void updatePlan() {
        // Retrieve values from text fields
        String planId = planIdField.getText();
        String planName = planNameField.getText();
        String speed = speedField.getText();
        String duration = durationField.getText();
        String price = priceField.getText();

        // Check if any field is empty
        if (planId.isEmpty() || planName.isEmpty() || speed.isEmpty() || duration.isEmpty() || price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            // Prepare SQL statement
            PreparedStatement statement = connection.prepareStatement("UPDATE plan SET plan_name=?, speed=?, duration=?, price=? WHERE plan_id=?");
            statement.setString(1, planName);
            statement.setString(2, speed);
            statement.setString(3, duration);
            statement.setString(4, price);
            statement.setString(5, planId);

            // Execute SQL statement
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Plan updated success
