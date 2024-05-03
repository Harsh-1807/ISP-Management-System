import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Arrays;

public class Login extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    private Connection connection;

    public Login() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/isp3", "root", "Harsh_123");
        } catch (SQLException e) {
            handleDBError(e);
            System.exit(1);
        }
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // Create a panel for holding components
        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200)); 
        panel.setBackground(new Color(173, 216, 230)); 

        // Create a welcome label
        JLabel welcomeLabel = new JLabel("Welcome to ISP Management System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(welcomeLabel);

        // Create username panel
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        panel.add(usernamePanel);

        // Create password panel
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        panel.add(passwordPanel);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.addActionListener(this);
        forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.setFont(new Font("Arial", Font.BOLD, 16));
        forgotPasswordButton.addActionListener(this);
        buttonPanel.add(loginButton);
        buttonPanel.add(forgotPasswordButton);
        panel.add(buttonPanel);

        
        add(panel, BorderLayout.CENTER);

        try {
            Image logo = Toolkit.getDefaultToolkit().getImage("ISP1.jpg");
            setIconImage(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pack(); 
        setLocationRelativeTo(null); 
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            login();
        } else if (e.getSource() == forgotPasswordButton) {
            forgotPassword();
        }
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (PreparedStatement statement = connection.prepareStatement("SELECT usertype FROM User WHERE username=? AND password=?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String userType = resultSet.getString("usertype");
                    showUserHomepage(userType);
                    JOptionPane.showMessageDialog(this, "Login Successful! User Type: " + userType);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Username or Password!");
                }
            }
        } catch (SQLException ex) {
            handleDBError(ex);
        }
    }

    private void forgotPassword() {
        String username = usernameField.getText();
        String securityQuestion = getSecurityQuestion(username);

        if (securityQuestion != null) {

            JPasswordField answerField = new JPasswordField(20);
            Object[] message = {"Security Question:\n", securityQuestion, "\n\nEnter Your Answer:", answerField};
            
            int option = JOptionPane.showConfirmDialog(this, message, "Forgot Password", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
            
                char[] answerChars = answerField.getPassword();
                String answer = new String(answerChars);
                
                if (validateSecurityAnswer(username, answer)) {
                    JOptionPane.showMessageDialog(this, "Security Answer Correct! You can reset your password.");
                   
                    dispose();
                    Signin.main(new String[]{});
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect Security Answer!");
                }
                
                Arrays.fill(answerChars, ' ');
            }
        } else {
            JOptionPane.showMessageDialog(this, "Username not found or no security question set!");
        }
    }

    private String getSecurityQuestion(String username) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT security FROM User WHERE username=?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("security");
                }
            }
        } catch (SQLException ex) {
            handleDBError(ex);
        }
        return null;
    }

    private boolean validateSecurityAnswer(String username, String answer) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS count FROM User WHERE username=? AND security_answer=?")) {
            statement.setString(1, username);
            statement.setString(2, answer);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count") > 0;
                }
            }
        } catch (SQLException ex) {
            handleDBError(ex);
        }
        return false;
    }

    private void showUserHomepage(String userType) {
        if (userType.equals("Admin")) {
            new Homepage().setVisible(true);
        } else {
            new UserHomePage().setVisible(true);
        }
    }

    private void handleDBError(SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database error occurred: " + ex.getMessage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login();
        });
    }
}
