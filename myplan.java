import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
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
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Set logo icon
        try {
            Image logo = Toolkit.getDefaultToolkit().getImage("ISP1.jpg");
            setIconImage(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a JLabel to hold the background image
        JLabel backgroundLabel = new JLabel(new ImageIcon("Screenshot 2022-12-23 182637.png"));
        backgroundLabel.setLayout(new BorderLayout());

        // Panel for holding components
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        mainPanel.setOpaque(false); // Make the panel transparent

        // Text area for displaying results
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setFont(new Font("Arial", Font.PLAIN, 16)); // Set font
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Back button
        JButton backButton = new JButton("Back to Homepage");
        backButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        backButton.setForeground(Color.WHITE); // Text color
        backButton.setFont(new Font("Arial", Font.BOLD, 14)); // Font
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                UserHomePage homepage = new UserHomePage();
                homepage.setVisible(true);
            }
        });
        mainPanel.add(backButton, BorderLayout.SOUTH);


        // Add the main panel to the background label
        backgroundLabel.add(mainPanel);

        // Set the content pane of the JFrame to the background label
        setContentPane(backgroundLabel);

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
