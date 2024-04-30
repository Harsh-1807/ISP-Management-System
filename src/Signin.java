import java.sql.*;
import javax.swing.JOptionPane;

public class Signin {

    // Database connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/isp3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Harsh_123";

    public static boolean addUser(String username, String password, String security) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            
            if (usernameExists(username, conn)) {
                System.out.println("Username already exists.");
                return false;
            }

            // Insert the user into the database
            String sql = "INSERT INTO User (username, password, usertype, security) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, "Customer"); // Set usertype to Customer
            statement.setString(4, security); // Set security question
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static boolean usernameExists(String username, Connection conn) {
        try {
            String sql = "SELECT COUNT(*) AS count FROM User WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count") > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean allFieldsFilled(String username, String password, String security) {
        return !username.isEmpty() && !password.isEmpty() && !security.isEmpty();
    }

    public static void main(String[] args) {
        // Input username, password, and security question from the user
        String username = JOptionPane.showInputDialog("Enter username:");
        String password = JOptionPane.showInputDialog("Enter password:");
        String security = JOptionPane.showInputDialog("Security Question: What is your favorite game?");

        if (allFieldsFilled(username, password, security)) {
            if (addUser(username, password, security)) {
                JOptionPane.showMessageDialog(null, "User added successfully.");
                // Redirect to another Java part (Login.java)
                Login.main(args);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add user.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
        }
    }
}
