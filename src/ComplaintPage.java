import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class ComplaintPage extends JFrame {
    JTextArea complaintTextArea;

    // Database connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/isp3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Harsh_123";

    public ComplaintPage() {
        JLabel titleLabel = new JLabel("Add Complaint");
        titleLabel.setBounds(50, 20, 200, 30);
        add(titleLabel);

        JLabel complaintLabel = new JLabel("Complaint:");
        complaintLabel.setBounds(50, 60, 80, 30);
        add(complaintLabel);

        complaintTextArea = new JTextArea();
        complaintTextArea.setBounds(50, 100, 250, 150);
        add(complaintTextArea);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(120, 270, 100, 30);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String complaint = complaintTextArea.getText();
                if (complaint.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a complaint.");
                } else {
                    // Call a method to save the complaint to the database
                    if (saveComplaint(complaint)) {
                        JOptionPane.showMessageDialog(null, "Complaint submitted successfully!");
                        dispose(); // Close the complaint page after submission
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to submit complaint. Please try again later.");
                    }
                }
            }
        });
        add(submitButton);

        setSize(350, 350);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Changed to dispose on close, as it's a subpage
    }

    // Method to retrieve the username from the User table
    private String getUsername() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT username FROM User"; // Assuming 'User' is the table name
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("username");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Method to save the complaint to the database
    private boolean saveComplaint(String complaint) {
        String username = getUsername(); // Retrieve username from the User table
        if (username == null) {
            // Handle case where username is not found
            return false;
        }
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
        	String sql = "INSERT INTO complaints (username, complaint_text, complaint_date) VALUES (?, ?, CURRENT_DATE())";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, complaint);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComplaintPage());
    }
}
