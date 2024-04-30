import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class ViewPlansPage extends JFrame {
    JTextField searchTextField;
    JTable plansTable;
    JButton buyButton; // New Buy button

    // Database connection details
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/isp3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Harsh_123";

    private int selectedPlanId; // Variable to store the selected plan ID

    public ViewPlansPage() {
        JLabel titleLabel = new JLabel("View Plans");
        titleLabel.setBounds(50, 20, 200, 30);
        add(titleLabel);

        JLabel searchLabel = new JLabel("Search by Speed:");
        searchLabel.setBounds(50, 60, 150, 30);
        add(searchLabel);

        searchTextField = new JTextField();
        searchTextField.setBounds(180, 60, 120, 30);
        add(searchTextField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(320, 60, 100, 30);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchSpeed = searchTextField.getText();
                // Call a method to fetch plans based on the entered speed
                ArrayList<Plan> plans = getPlansBySpeedFromDatabase(searchSpeed);
                displayPlans(plans);
            }
        });
        add(searchButton);

        // New Buy button
        buyButton = new JButton("Buy");
        buyButton.setBounds(50, 310, 100, 30);
        buyButton.setEnabled(false); // Disable initially
        buyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Assuming username is obtained from user input
                String username = JOptionPane.showInputDialog("Enter your username:");
                if (username != null && !username.isEmpty()) {
                    buyPlan(username);
                } else {
                    JOptionPane.showMessageDialog(ViewPlansPage.this, "Please enter your username.");
                }
            }
        });
        add(buyButton);

        plansTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(plansTable);
        scrollPane.setBounds(50, 100, 600, 200);
        add(scrollPane);

        setSize(700, 800);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Changed to dispose on close, as it's a subpage
    }

    // Method to fetch plans based on the entered speed from the database
    private ArrayList<Plan> getPlansBySpeedFromDatabase(String speed) {
        ArrayList<Plan> plans = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Open a connection
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            // Execute a query
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM Plan WHERE speed = " + speed; // Assuming your table name is 'plans'
            ResultSet rs = stmt.executeQuery(sql);

            // Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                int id = rs.getInt("plan_id");
                String name = rs.getString("plan_name");
                int planSpeed = rs.getInt("speed");
                double price = rs.getDouble("price");
                int duration = rs.getInt("duration"); // Assuming the column name for duration is 'duration'
                // Create Plan object and add to list
                plans.add(new Plan(id, name, planSpeed, price, duration));
            }
            // Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } finally {
            // Finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try

        return plans;
    }

    // Method to display plans in the JTable
    private void displayPlans(ArrayList<Plan> plans) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Plan ID"); // Add Plan ID column
        model.addColumn("Plan Name");
        model.addColumn("Speed");
        model.addColumn("Price");
        model.addColumn("Duration");

        for (Plan plan : plans) {
            model.addRow(new Object[]{plan.getId(), plan.getName(), plan.getSpeed(), plan.getPrice(), plan.getDuration()});
        }

        plansTable.setModel(model);

        // Add ListSelectionListener to the table
        plansTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = plansTable.getSelectedRow();
                    if (selectedRow != -1) { // If a row is selected
                        int option = JOptionPane.showConfirmDialog(ViewPlansPage.this, "Do you want to select this plan?", "Select Plan", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            // Retrieve the selected plan ID
                            selectedPlanId = (int) plansTable.getValueAt(selectedRow, 0);
                            String username = JOptionPane.showInputDialog("Enter your username:");
                            if (username != null && !username.isEmpty()) {
                                buyPlan(username);
                            } else {
                                JOptionPane.showMessageDialog(ViewPlansPage.this, "Please enter your username.");
                            }
                        }
                    }
                }
            }
        });
    }

    // Method to insert the selected plan ID and username into the database
    private void buyPlan(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Open a connection
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            // Prepare statement for insertion
            String sql = "INSERT INTO UserPlan (username, plan_id) VALUES (?, ?,";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setInt(2, selectedPlanId);

            // Execute the update
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Plan purchased successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to purchase plan.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while purchasing plan.");
        } finally {
            // Close resources
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Sample Plan class, replace it with your actual Plan class
    private class Plan {
        private int id;
        private String name;
        private int speed;
        private double price;
        private int duration;

        public Plan(int id, String name, int speed, double price, int duration) {
            this.id = id;
            this.name = name;
            this.speed = speed;
            this.price = price;
            this.duration = duration;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getSpeed() {
            return speed;
        }

        public double getPrice() {
            return price;
        }

        public int getDuration() {
            return duration;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewPlansPage());
    }
}
