import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserHomePage extends JFrame {
    JButton buyPlanButton, viewMyPlanButton, addComplaintButton,myplan;

    public UserHomePage() {
        setTitle("ISP Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 850);
        setLocationRelativeTo(null); // Center the frame on the screen

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome to ISP Management System");
        welcomeLabel.setBounds(20, 20, 260, 30);
        panel.add(welcomeLabel);

        buyPlanButton = new JButton("Buy a Plan");
        buyPlanButton.setBounds(50, 70, 180, 30);
        buyPlanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open BuyPlanPage
                ViewPlansPage buyPlanPage = new ViewPlansPage();
                buyPlanPage.setVisible(true);
                dispose(); // Close the homepage window
            }
        });
        panel.add(buyPlanButton);

        viewMyPlanButton = new JButton("Change Information");
        viewMyPlanButton.setBounds(50, 110, 180, 30);
        viewMyPlanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open ViewMyPlanPage
                ChangePasswordPage viewMyPlanPage = new ChangePasswordPage();
                viewMyPlanPage.setVisible(true);
                dispose(); // Close the homepage window
            }
        });
        panel.add(viewMyPlanButton);
         
        addComplaintButton = new JButton("Add Complaint");
        addComplaintButton.setBounds(50, 150, 180, 30);
        addComplaintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open ComplaintPage
                ComplaintPage complaintPage = new ComplaintPage();
                complaintPage.setVisible(true);
                dispose(); // Close the homepage window
            }
        });
        panel.add(addComplaintButton);
        myplan = new JButton("myplan");
        myplan.setBounds(50, 150, 180, 30);
        myplan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open ComplaintPage
                myplan plan = new myplan();
                plan.setVisible(true);
                dispose(); // Close the homepage window
            }
        });
        panel.add(addComplaintButton);
        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserHomePage::new);
    }
}
