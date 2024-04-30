import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class myplan extends JFrame {
    // Database connection details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/isp3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Harsh_123";

    private JTextArea resultTextArea;

    public myplan() {
        setTitle("User Plans");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        add(scrollPane, BorderLayout.CENTER);

        fetchUserPlans();
    }

    private void fetchUserPlans() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // 1. Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Establish a connection to the MySQL database
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // 3. Create a Statement to execute the query
            statement = connection.createStatement();

            // 4. Execute the query to fetch user plans
            String query = "call ShowUserPlans()";
            resultSet = statement.executeQuery(query);

            // 5. Process the result set and display the user plans
            StringBuilder result = new StringBuilder();
            result.append("Username\tPlan Name\n");
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String planName = resultSet.getString("plan_name");
                result.append(username).append("\t").append(planName).append("\n");
            }

            // Display the results in the JTextArea
            resultTextArea.setText(result.toString());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            myplan frame = new myplan();
            frame.setVisible(true);
        });
    }
}
