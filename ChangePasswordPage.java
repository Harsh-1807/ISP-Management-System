import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ChangePasswordPage extends JFrame {
    JTextField usernameTextField, currentPasswordTextField, newPasswordTextField, rewritePasswordTextField;
    JButton submitButton, backToHomepageButton;

    // Database connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/isp3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Harsh_123";

    public ChangePasswordPage() {
        setTitle("Change Password");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 255)); 

        JLabel titleLabel = new JLabel("Change Password");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(110, 20, 200, 30);
        panel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 70, 100, 30);
        panel.add(usernameLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(160, 70, 150, 30);
        panel.add(usernameTextField);

        JLabel currentPasswordLabel = new JLabel("Current Password:");
        currentPasswordLabel.setBounds(50, 110, 120, 30);
        panel.add(currentPasswordLabel);

        currentPasswordTextField = new JPasswordField();
        currentPasswordTextField.setBounds(160, 110, 150, 30);
        panel.add(currentPasswordTextField);

        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setBounds(50, 150, 120, 30);
        panel.add(newPasswordLabel);

        newPasswordTextField = new JPasswordField();
        newPasswordTextField.setBounds(160, 150, 150, 30);
        panel.add(newPasswordTextField);

        JLabel rewritePasswordLabel = new JLabel("Rewrite Password:");
        rewritePasswordLabel.setBounds(50, 190, 120, 30);
        panel.add(rewritePasswordLabel);

        rewritePasswordTextField = new JPasswordField();
        rewritePasswordTextField.setBounds(160, 190, 150, 30);
        panel.add(rewritePasswordTextField);

        submitButton = new JButton("Submit");
        submitButton.setBounds(120, 240, 100, 30);
        submitButton.setBackground(new Color(30, 144, 255)); 
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
        panel.add(submitButton);

        backToHomepageButton = new JButton("Back to Homepage");
        backToHomepageButton.setBounds(100, 290, 200, 30);
        backToHomepageButton.setBackground(new Color(255, 69, 0)); 
        backToHomepageButton.setForeground(Color.WHITE);
        backToHomepageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                UserHomePage homepage = new UserHomePage(); 
                homepage.setVisible(true);
            }
        });
        panel.add(backToHomepageButton);

        try {
            Image logo = Toolkit.getDefaultToolkit().getImage("ISP1.jpg");
            setIconImage(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getContentPane().add(panel);
        setVisible(true);
    }
    private void handleSubmit() {
        String username = usernameTextField.getText();
        String currentPassword = currentPasswordTextField.getText();
        String newPassword = newPasswordTextField.getText();
        String rewritePassword = rewritePasswordTextField.getText();

        if (!newPassword.equals(rewritePassword)) {
            JOptionPane.showMessageDialog(null, "New password and rewrite password do not match.");
            dispose(); 
            Login homepage = new Login(); 
            homepage.setVisible(true);
            return;
        }

        if (!validateUser(username, currentPassword)) {
            JOptionPane.showMessageDialog(null, "Invalid username or password.");
            dispose(); 
            Login homepage4 = new Login(); 
            homepage4.setVisible(true);
            return;
        }

        if (updatePassword(username, newPassword)) {
            JOptionPane.showMessageDialog(null, "Password changed successfully!");
            dispose(); 
        } else {
            JOptionPane.showMessageDialog(null, "Failed to change password. Please try again later.");
        }
    }

    private boolean validateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM User WHERE username = ? AND password = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error occurred.");
            return false;
        }
    }

    private boolean updatePassword(String username, String newPassword) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "UPDATE User SET password = ? WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, newPassword);
            statement.setString(2, username);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0; 
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
