import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DashboardPage extends JFrame {

    // Database connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/isp3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Harsh_123";

    public DashboardPage() {
        setTitle("ISP Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Header
        textArea.append("ISP Dashboard\n");
        textArea.append("-------------------------------\n\n");

        // Insights
        addInsights(textArea);

        add(panel);
        setVisible(true);
    }

    private void addInsights(JTextArea textArea) {
        // Get total number of users
        int totalUsers = getTotalUsers();
        textArea.append("Total Users: " + totalUsers + "\n\n");

        // Get breakdown of users by type
        String userBreakdown = getUserBreakdown();
        textArea.append("User Breakdown:\n" + userBreakdown + "\n");

        // Get distribution of users across plans
        String userDistributionByPlan = getUserDistributionByPlan();
        textArea.append("User Distribution by Plan:\n" + userDistributionByPlan + "\n");

        // Get most popular plan
        String mostPopularPlan = getMostPopularPlan();
        textArea.append("Most Popular Plan: " + mostPopularPlan + "\n");

        // Get least popular plan
        String leastPopularPlan = getLeastPopularPlan();
        textArea.append("Least Popular Plan: " + leastPopularPlan + "\n");

        // Get total number of complaints
        int totalComplaints = getTotalComplaints();
        textArea.append("\nTotal Complaints: " + totalComplaints + "\n");

        // Get average price of the plans
        double averagePrice = getAveragePriceOfPlans();
        textArea.append("Average Price of Plans: Rs" + String.format("%.2f", averagePrice) + "\n");
    }

    private int getTotalUsers() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM User")) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private String getUserBreakdown() {
        StringBuilder breakdown = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT usertype, COUNT(*) AS count FROM User GROUP BY usertype")) {
            while (rs.next()) {
                breakdown.append(rs.getString("usertype")).append(": ").append(rs.getInt("count")).append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return breakdown.toString();
    }

    private String getUserDistributionByPlan() {
        StringBuilder distribution = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT Plan.plan_name, COUNT(User.username) AS count " +
                     "FROM Plan LEFT JOIN User ON Plan.plan_id = User.plan_id " +
                     "GROUP BY Plan.plan_name")) {
            while (rs.next()) {
                distribution.append(rs.getString("plan_name")).append(": ").append(rs.getInt("count")).append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return distribution.toString();
    }

    private String getMostPopularPlan() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT plan_name FROM (SELECT Plan.plan_name, COUNT(User.username) AS count " +
                     "FROM Plan LEFT JOIN User ON Plan.plan_id = User.plan_id " +
                     "GROUP BY Plan.plan_name) AS counts ORDER BY count DESC LIMIT 1")) {
            if (rs.next()) {
                return rs.getString("plan_name");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }
  
    private String getLeastPopularPlan() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT plan_name FROM (SELECT Plan.plan_name, COUNT(User.username) AS count " +
                     "FROM Plan LEFT JOIN User ON Plan.plan_id = User.plan_id " +
                     "GROUP BY Plan.plan_name) AS counts ORDER BY count ASC LIMIT 1")) {
            if (rs.next()) {
                return rs.getString("plan_name");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private int getTotalComplaints() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM complaints")) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private double getAveragePriceOfPlans() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT AVG(price) AS average_price FROM Plan")) {
            if (rs.next()) {
                return rs.getDouble("average_price");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DashboardPage::new);
    }
}
