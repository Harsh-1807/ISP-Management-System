import javax.swing.*;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.sql.*;

public class CreatePlan extends JFrame {
    private JTextField planIdField, planNameField, speedField, durationField, priceField;
    private JButton createButton, updateButton, deleteButton, returnButton;

    private Connection connection;

    public CreatePlan() {
        try {
            // Establish database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/isp3", "root", "Harsh_123");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
            System.exit(1); // Exit the program if database connection fails
        }

        setTitle("ISP Management System - Create Plan");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Create or Manage ISP Plans");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBounds(80, 20, 350, 30);
        panel.add(titleLabel);
        try {
            Image logo = Toolkit.getDefaultToolkit().getImage("ISP1.jpg");
            setIconImage(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }


        JLabel planIdLabel = new JLabel("Plan ID:");
        planIdLabel.setBounds(50, 70, 100, 20);
        panel.add(planIdLabel);

        planIdField = new JTextField();
        planIdField.setBounds(160, 70, 200, 20);
        panel.add(planIdField);

        JLabel planNameLabel = new JLabel("Plan Name:");
        planNameLabel.setBounds(50, 110, 100, 20);
        panel.add(planNameLabel);

        planNameField = new JTextField();
        planNameField.setBounds(160, 110, 200, 20);
        panel.add(planNameField);

        JLabel speedLabel = new JLabel("Speed:");
        speedLabel.setBounds(50, 150, 100, 20);
        panel.add(speedLabel);

        speedField = new JTextField();
        speedField.setBounds(160, 150, 200, 20);
        panel.add(speedField);

        JLabel durationLabel = new JLabel("Duration:");
        durationLabel.setBounds(50, 190, 100, 20);
        panel.add(durationLabel);

        durationField = new JTextField();
        durationField.setBounds(160, 190, 200, 20);
        panel.add(durationField);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(50, 230, 100, 20);
        panel.add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(160, 230, 200, 20);
        panel.add(priceField);

        createButton = new JButton("Create");
        createButton.setBounds(80, 280, 100, 30);
        createButton.addActionListener(e -> insertPlan());
        panel.add(createButton);

        updateButton = new JButton("Update");
        updateButton.setBounds(200, 280, 100, 30);
        updateButton.addActionListener(e -> updatePlan());
        panel.add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(320, 280, 100, 30);
        deleteButton.addActionListener(e -> deletePlan());
        panel.add(deleteButton);

        returnButton = new JButton("Return to Homepage");
        returnButton.setBounds(150, 340, 200, 30);
        returnButton.addActionListener(e -> {
            dispose();
            Homepage homepage = new Homepage();
            homepage.setVisible(true);
        });
        panel.add(returnButton);
        try {
            Image logo = Toolkit.getDefaultToolkit().getImage("ISP1.jpg");
            setIconImage(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(panel);
        setVisible(true);
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
                JOptionPane.showMessageDialog(this, "Plan updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update plan.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred.");
        }
    }

    private void deletePlan() {
        // Retrieve plan ID
        String planId = planIdField.getText();

        // Check if plan ID is empty
        if (planId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Plan ID to delete.");
            return;
        }

        // Confirm deletion
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this plan?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                // Prepare SQL statement
                PreparedStatement statement = connection.prepareStatement("DELETE FROM plan WHERE plan_id=?");
                statement.setString(1, planId);

                // Execute SQL statement
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Plan deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete plan. Plan ID not found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error occurred.");
            }
        }
    }

    public static void main(String[] args) {
        new CreatePlan();
    }
}
