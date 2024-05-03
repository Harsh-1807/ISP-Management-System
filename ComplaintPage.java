import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.sql.*;

public class ComplaintPage extends JFrame {
    JTextArea complaintTextArea;
    JButton backToHomepageButton;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/isp3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Harsh_123";

    public ComplaintPage() {
    	try {
            Image logo = Toolkit.getDefaultToolkit().getImage("ISP1.jpg");
            setIconImage(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    	setTitle("Add Complaint");
        
        getContentPane().setBackground(new Color(200, 230, 255));
        JLabel titleLabel = new JLabel("Add Complaint");
        titleLabel.setBounds(50, 20, 200, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        add(titleLabel);

        JLabel complaintLabel = new JLabel("Complaint:");
        complaintLabel.setBounds(50, 60, 80, 30);
        complaintLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(complaintLabel);

        backToHomepageButton = new JButton("Back to Homepage");
        backToHomepageButton.setBounds(170, 590, 150, 30);
        backToHomepageButton.setBackground(Color.GRAY);
        backToHomepageButton.setForeground(Color.WHITE);
        backToHomepageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                
                UserHomePage homepage = new UserHomePage(); 
                homepage.setVisible(true);
            }
        });
        add(backToHomepageButton);

        complaintTextArea = new JTextArea();
        complaintTextArea.setBounds(50, 100, 450, 450);
        complaintTextArea.setBackground(new Color(20, 230, 230));
        complaintTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        add(complaintTextArea);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(80, 590, 100, 30);
        submitButton.setBackground(Color.GREEN);
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String complaint = complaintTextArea.getText();
                if (complaint.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a complaint.");
                } else {
                    
                    if (saveComplaint(complaint)) {
                        JOptionPane.showMessageDialog(null, "Complaint submitted successfully!");
                        dispose(); 
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to submit complaint. Please try again later.");
                    }
                }
            }
        });
        add(submitButton);

        setSize(800, 900);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
    }

   
    private String getUsername() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT username FROM User"; // Assuming 'User' is the table name
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("username");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    
    private boolean saveComplaint(String complaint) {
        String username = getUsername(); 
        if (username == null) {
            
            return false;
        }
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
        	String sql = "INSERT INTO complaints (username, complaint_text, complaint_date) VALUES (?, ?, CURRENT_DATE())";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, complaint);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComplaintPage());
    }
}
