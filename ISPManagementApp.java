import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }
}

class ISPManagementSystem {
    private java.util.List<User> users;
    private java.util.List<Admin> admins;

    public ISPManagementSystem() {
        users = new java.util.ArrayList<>();
        admins = new java.util.ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addAdmin(Admin admin) {
        admins.add(admin);
    }

    public User authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public Admin authenticateAdmin(String username, String password) {
        for (Admin admin : admins) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        return null;
    }
}

class UserModule {
    private User currentUser;

    public UserModule(User user) {
        this.currentUser = user;
    }

    public void viewAccountDetails() {
        JOptionPane.showMessageDialog(null, "Viewing account details for user: " + currentUser.getUsername());
    }

    // Other user functionalities
}

class AdminModule {
    private Admin currentAdmin;
    private ISPManagementSystem ispSystem;

    public AdminModule(Admin admin, ISPManagementSystem ispSystem) {
        this.currentAdmin = admin;
        this.ispSystem = ispSystem;
    }

    public void viewUserDetails(String username) {
        User user = ispSystem.authenticateUser(username, ""); // empty password for authentication
        if (user != null) {
            JOptionPane.showMessageDialog(null, "Viewing details for user: " + user.getUsername());
        } else {
            JOptionPane.showMessageDialog(null, "User not found.");
        }
    }

    // Other admin functionalities
}

class ISPManagementGUI {
    private ISPManagementSystem ispSystem;

    public ISPManagementGUI(ISPManagementSystem ispSystem) {
        this.ispSystem = ispSystem;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("ISP Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout());

        JButton userButton = new JButton("Login as User");
        JButton adminButton = new JButton("Login as Admin");

        frame.add(userButton);
        frame.add(adminButton);

        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog("Enter username:");
                String password = JOptionPane.showInputDialog("Enter password:");

                User authenticatedUser = ispSystem.authenticateUser(username, password);

                if (authenticatedUser != null) {
                    UserModule userModule = new UserModule(authenticatedUser);
                    userModule.viewAccountDetails();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials.");
                }
            }
        });

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog("Enter admin username:");
                String password = JOptionPane.showInputDialog("Enter admin password:");

                Admin authenticatedAdmin = ispSystem.authenticateAdmin(username, password);

                if (authenticatedAdmin != null) {
                    AdminModule adminModule = new AdminModule(authenticatedAdmin, ispSystem);
                    String userToView = JOptionPane.showInputDialog("Enter username to view details:");
                    adminModule.viewUserDetails(userToView);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid admin credentials.");
                }
            }
        });

        frame.setVisible(true);
    }
}

public class ISPManagementApp {
    public static void main(String[] args) {
        ISPManagementSystem ispSystem = new ISPManagementSystem();

        // Create users and admins
        User user1 = new User("user1", "password1");
        Admin admin1 = new Admin("admin1", "adminPassword1");

        ispSystem.addUser(user1);
        ispSystem.addAdmin(admin1);

        // Launch the GUI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ISPManagementGUI(ispSystem);
            }
        });
    }
}
