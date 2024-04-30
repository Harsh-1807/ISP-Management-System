import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class ChangePasswordPage extends JFrame {
    JTextField usernameTextField, currentPasswordTextField, newPasswordTextField, rewritePasswordTextField;

    // Database connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/isp3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Harsh_123";

    public ChangePasswordPage() {
        JLabel titleLabel = new JLabel("Change Password");
        titleLabel.setBounds(50, 20, 200, 30);
        add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 60, 120, 30);
        add(usernameLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(180, 60, 120, 30);
        add(usernameTextField);

        JLabel currentPasswordLabel = new JLabel("Current Password:");
        currentPasswordLabel.setBounds(50, 100, 120, 30);
        add(currentPasswordLabel);

        currentPasswordTextField = new JTextField();
        currentPasswordTextField.setBounds(180, 100, 120, 30);
        add(currentPasswordTextField);

        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setBounds(50, 140, 120, 30);
        add(newPasswordLabel);

        newPasswordTextField = new JTextField();
        newPasswordTextField.setBounds(180, 140, 120, 30);
        add(newPasswordTextField);

        JLabel rewritePasswordLabel = new JLabel("Rewrite Password:");
        rewritePasswordLabel.setBounds(50, 180, 120, 30);
        add(rewritePasswordLabel);

        rewritePasswordTextField = new JTextField();
        rewritePasswordTextField.setBounds(180, 180, 120, 30);
        add(rewritePasswordTextField);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(100, 230, 100, 30);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String currentPassword = currentPasswordTextField.getText();
                String newPassword = newPasswordTextField.getText();
                String rewritePassword = rewritePasswordTextField.getText();

                // Validate that new password and rewrite password match
                if (!newPassword.equals(rewritePassword)) {
                    JOptionPane.showMessageDialog(null, "New password and rewrite password do not match.");
                    return;
                }

                // Validate username and current password against the database
                if (!validateUser(username, currentPassword)) {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.");
                    return;
                }

                // Update the password in the database
                if (updatePassword(username, newPassword)) {
                    JOptionPane.showMessageDialog(null, "Password changed successfully!");
                    dispose(); // Close the change password page
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to change password. Please try again later.");
                }
            }
        });
        add(submitButton);

        setSize(850, 800);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Changed to dispose on close, as it's a subpage
    }

    // Method to validate the username and password against the database
    private boolean validateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM User WHERE username = ? AND password = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If the result set has at least one row, the username and password are correct
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error occurred.");
            return false;
        }
    }

    // Method to update the password in the database
    private boolean updatePassword(String username, String newPassword) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "UPDATE User SET password = ? WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, newPassword);
            statement.setString(2, username);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0; // If rowsUpdated > 0, password updated successfully
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error occurred.");
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChangePasswordPage::new);
    }
}