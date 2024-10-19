/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;
import UI.DatabaseConnection;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class UserDashboard extends JFrame {
    private int userId;

    public UserDashboard(int userId) {
        this.userId = userId;
        initComponents();
        fetchUserBookings();
    }

    private void initComponents() {
        setTitle("TRYIT - User Dashboard");
        getContentPane().setBackground(new Color(255, 250, 240));

        JLabel dashboardLabel = new JLabel("User Dashboard");
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 30));
        dashboardLabel.setForeground(new Color(0, 102, 204));

        JButton logOutButton = new JButton("Logout");
        logOutButton.setFont(new Font("Arial", Font.BOLD, 16));
        logOutButton.setBackground(new Color(255, 51, 51));
        logOutButton.setForeground(Color.WHITE);
        logOutButton.setFocusPainted(false);
        logOutButton.setPreferredSize(new Dimension(120, 40));
        logOutButton.addActionListener(e -> {
            new LoginPage().setVisible(true);
            this.dispose();
        });

        JButton backButton = new JButton("Back to Landing Page");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(0, 128, 128));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.addActionListener(e -> new LandingPage(userId).setVisible(true));

        // Assuming this is in a button click event or similar
JButton viewTransactionsButton = new JButton("View Transaction History");


        viewTransactionsButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewTransactionsButton.setBackground(new Color(100, 149, 237));
        viewTransactionsButton.setForeground(Color.WHITE);
        viewTransactionsButton.setFocusPainted(false);
        viewTransactionsButton.setPreferredSize(new Dimension(160, 40));
        viewTransactionsButton.addActionListener(e -> new TransactionHistoryPage(userId).setVisible(true));

        JPanel bookingsPanel = new JPanel();
        bookingsPanel.setBackground(new Color(255, 255, 204));
        bookingsPanel.setLayout(new BoxLayout(bookingsPanel, BoxLayout.Y_AXIS));
        bookingsPanel.setBorder(BorderFactory.createTitledBorder("Your Bookings"));

        setLayout(new BorderLayout());
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 153, 153));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        headerPanel.add(dashboardLabel);
        headerPanel.add(backButton);
        headerPanel.add(viewTransactionsButton);
        headerPanel.add(logOutButton);

        JScrollPane scrollPane = new JScrollPane(bookingsPanel);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    

// In the UserDashboard class, add:
private void fetchUserBookings() {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT Experiences.title, Bookings.attendees, Bookings.total_amount FROM Bookings " +
                     "JOIN Experiences ON Bookings.experience_id = Experiences.id " +
                     "WHERE Bookings.user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String experienceTitle = rs.getString("title");
            int attendees = rs.getInt("attendees");
            double totalAmount = rs.getDouble("total_amount");

            // Add booking details to the dashboard
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error fetching bookings: " + e.getMessage());
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT id FROM Users WHERE username = ''"; // Replace 'your_username' with the actual username
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    new UserDashboard(userId).setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }}