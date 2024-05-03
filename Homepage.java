import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Homepage extends JFrame {
    private JButton createPlanButton, manageEmployeeButton, manageCustomerButton, logoutButton, manageDashButton;

    public Homepage() {
        setTitle("ISP Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBackground(new Color(240, 240, 240));

        try {
            Image logo = Toolkit.getDefaultToolkit().getImage("ISP1.jpg");
            setIconImage(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel welcomeLabel = new JLabel("Welcome to ISP Management System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        panel.add(welcomeLabel, gbc);

        createPlanButton = new JButton("Create Plan");
        createPlanButton.addActionListener(e -> {
            dispose();
            CreatePlan homepage = new CreatePlan();
            homepage.setVisible(true);
        });
        customizeButton(createPlanButton);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(createPlanButton, gbc);

        manageEmployeeButton = new JButton("Manage Employee");
        customizeButton(manageEmployeeButton);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(manageEmployeeButton, gbc);
        manageEmployeeButton.addActionListener(e -> {
            dispose();
            Employee homepage2 = new Employee();
            homepage2.setVisible(true);
        });

        manageDashButton = new JButton("Dashboard");
        customizeButton(manageDashButton);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(manageDashButton, gbc);
        manageDashButton.addActionListener(e -> {
            dispose();
            DashboardPage homepage22 = new DashboardPage();
            homepage22.setVisible(true);
        });

        manageCustomerButton = new JButton("Customers Information");
        customizeButton(manageCustomerButton);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(manageCustomerButton, gbc);
        manageCustomerButton.addActionListener(e -> {
            dispose();
             CustomerPanel homepage223= new CustomerPanel();
            homepage223.setVisible(true);
        });
        try {
            Image logo = Toolkit.getDefaultToolkit().getImage("ISP1.jpg");
            setIconImage(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(panel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    
    private void customizeButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(57, 106, 252));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(200, 50));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Homepage::new);
    }
}
