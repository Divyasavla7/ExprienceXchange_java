package UI;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class HostDashboard extends JFrame {
    private int hostId;
    private JPanel experiencePanel; // Declare experiencePanel as an instance variable

    public HostDashboard(int hostId) {
        this.hostId = hostId;
        initComponents();
        fetchHostedExperiences();
    }

    private void initComponents() {
        setTitle("ExperienceXchange- Host Dashboard");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 248, 255));

        JLabel dashboardLabel = new JLabel("Host Dashboard");
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 26));
        dashboardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dashboardLabel.setForeground(new Color(25, 25, 112));

        JButton hostExperienceButton = new JButton("Host New Experience");
        hostExperienceButton.setFont(new Font("Arial", Font.PLAIN, 18));
        hostExperienceButton.setBackground(new Color(50, 205, 50));
        hostExperienceButton.setForeground(Color.WHITE);
        hostExperienceButton.setFocusPainted(false);
        hostExperienceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        hostExperienceButton.setPreferredSize(new Dimension(200, 50));
        hostExperienceButton.addActionListener(e -> new HostExperiencePage(hostId).setVisible(true));

        JButton logOutButton = new JButton("Logout");
        logOutButton.setFont(new Font("Arial", Font.PLAIN, 18));
        logOutButton.setBackground(new Color(220, 20, 60));
        logOutButton.setForeground(Color.WHITE);
        logOutButton.setFocusPainted(false);
        logOutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logOutButton.setPreferredSize(new Dimension(200, 50));
        logOutButton.addActionListener(e -> {
            new LoginPage().setVisible(true);
            this.dispose();
        });

        JButton backButton = new JButton("Back to Landing Page");
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.setBackground(new Color(0, 128, 128));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.addActionListener(e -> {
            new LandingPage(hostId).setVisible(true);
            this.dispose();
        });

        // Initialize experiencePanel here
        experiencePanel = new JPanel();
        experiencePanel.setLayout(new BoxLayout(experiencePanel, BoxLayout.Y_AXIS));
        experiencePanel.setBackground(new Color(245, 245, 245));
        experiencePanel.setBorder(BorderFactory.createTitledBorder("Your Hosted Experiences"));

        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(dashboardLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(hostExperienceButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(backButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(logOutButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(experiencePanel); // Add experiencePanel to the main panel

        add(panel);
    }

    private void fetchHostedExperiences() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT title, date_listed, price FROM Experiences WHERE host_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, hostId);
            ResultSet rs = stmt.executeQuery();

            // Clear previous experiences
            experiencePanel.removeAll();

            while (rs.next()) {
                String title = rs.getString("title");
                String dateListed = rs.getString("date_listed");
                double price = rs.getDouble("price");

                JLabel experienceLabel = new JLabel("Experience: " + title + " | Listed on: " + dateListed + " | Price: $" + price);
                experienceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                experienceLabel.setForeground(new Color(25, 25, 112));
                experiencePanel.add(experienceLabel);
            }

            // Revalidate and repaint to update the UI
            experiencePanel.revalidate();
            experiencePanel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching hosted experiences: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Replace with actual host ID after login
        SwingUtilities.invokeLater(() -> {
            int hostId = 1; // Example host ID for testing; replace with actual retrieval from the database
            new HostDashboard(hostId).setVisible(true);
        });
    }
}
