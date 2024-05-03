import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserHomePage extends JFrame {
    private JButton buyPlanButton, viewMyPlanButton, addComplaintButton, myplanButton;

    public UserHomePage() {
        setTitle("ISP Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null); 

        JPanel panel = new JPanel();
        panel.setLayout(null);
        try {
            Image logo = Toolkit.getDefaultToolkit().getImage("ISP1.jpg");
            setIconImage(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel welcomeLabel = new JLabel("Welcome to ISP Management System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setBounds(50, 30, 700, 40);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(welcomeLabel);

        buyPlanButton = createStyledButton("Buy a Plan");
        buyPlanButton.setBounds(200, 120, 400, 60);
        buyPlanButton.addActionListener(e -> openViewPlansPage());
        panel.add(buyPlanButton);

        viewMyPlanButton = createStyledButton("Change Information");
        viewMyPlanButton.setBounds(200, 210, 400, 60);
        viewMyPlanButton.addActionListener(e -> openChangePasswordPage());
        panel.add(viewMyPlanButton);

        addComplaintButton = createStyledButton("Add Complaint");
        addComplaintButton.setBounds(200, 300, 400, 60);
        addComplaintButton.addActionListener(e -> openComplaintPage());
        panel.add(addComplaintButton);

        myplanButton = createStyledButton("My Plan");
        myplanButton.setBounds(200, 390, 400, 60);
        myplanButton.addActionListener(e -> openMyPlanPage());
        panel.add(myplanButton);

        add(panel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(57, 106, 252));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        return button;
    }

    private void openViewPlansPage() {
        ViewPlansPage buyPlanPage = new ViewPlansPage();
        buyPlanPage.setVisible(true);
        dispose();
    }

    private void openChangePasswordPage() {
        ChangePasswordPage viewMyPlanPage = new ChangePasswordPage();
        viewMyPlanPage.setVisible(true);
        dispose(); 
    }

    private void openComplaintPage() {
        ComplaintPage complaintPage = new ComplaintPage();
        complaintPage.setVisible(true);
        dispose(); 
    }

    private void openMyPlanPage() {
        myplan myPlanPage = new myplan();
        myPlanPage.setVisible(true);
        dispose(); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserHomePage::new);
    }
}
