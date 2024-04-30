import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class CustomerPanel extends JFrame {
    JButton viewComplaintsButton, viewUsersButton, viewPlansButton;
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

        // Table and model
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(250, 50, 600, 400);
        add(scrollPane);

        setSize(900, 550);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
