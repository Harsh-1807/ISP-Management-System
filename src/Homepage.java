import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Homepage extends JFrame {
    JButton createPlanButton, manageEmployeeButton, manageCustomerButton, logoutButton, manageDashButton;

    public Homepage() {
        setTitle("ISP Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Background image or color can be set here
            }
        };
        panel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome to ISP Management System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setBounds(50, 20, 300, 30);
        panel.add(welcomeLabel);

        // "create_icon.png" with actual file path
        createPlanButton = new JButton("Create Plan");
        createPlanButton.setBounds(50, 70, 150, 50);
        createPlanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open CreatePlanPage
                CreatePlan createPlanPage = new CreatePlan();
                createPlanPage.setVisible(true);
                dispose(); // Close the homepage window
            }
        });
        panel.add(createPlanButton);

        // Replace "employee_icon.png" with actual file path
        manageEmployeeButton = new JButton("Manage Employee");
        manageEmployeeButton.setBounds(50, 140, 150, 50);
        manageEmployeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open EmployeePage
                Employee employeePage = new Employee();
                employeePage.setVisible(true);
                dispose(); // Close the homepage window
            }
        });
        panel.add(manageEmployeeButton);
        
         // Replace "dashboard_icon.png" with actual file path
        manageDashButton = new JButton("Dashboard");
        manageDashButton.setBounds(50, 210, 150, 50);
        manageDashButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open DashboardPage
                DashboardPage dash = new DashboardPage();
                dash.setVisible(true);
                dispose(); // Close the homepage window
            }
        });
        panel.add(manageDashButton);

         // Replace "customer_icon.png" with actual file path
        manageCustomerButton = new JButton("Manage Customer");
        manageCustomerButton.setBounds(50, 280, 150, 50);
        manageCustomerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open CustomerPage
                CustomerPanel customerPage = new CustomerPanel();
                customerPage.setVisible(true);
                dispose(); // Close the homepage window
            }
        });
        panel.add(manageCustomerButton);

        add(panel, BorderLayout.CENTER);

        setSize(800, 880); // Adjusted size for better visibility of components
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Homepage::new);
    }
}
