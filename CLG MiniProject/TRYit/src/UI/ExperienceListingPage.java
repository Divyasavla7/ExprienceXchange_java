package UI;
import UI.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import java.awt.*;

public class ExperienceListingPage extends JFrame {
    private int userId;

    public ExperienceListingPage(int userId) {
        this.userId = userId;
        initComponents();
        fetchExperiences();
    }

    private void initComponents() {
        setTitle("ExperienceXchange- Experience Listing");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main Content Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Available Experiences");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(102, 102, 102));
        mainPanel.add(titleLabel);

        add(mainPanel, BorderLayout.CENTER);
        pack();
    }

    private void fetchExperiences() {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT id, title, location, price FROM Experiences ORDER BY date_listed DESC";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        // Panel to hold experience listings
        JPanel experiencePanel = new JPanel();
        experiencePanel.setLayout(new BoxLayout(experiencePanel, BoxLayout.Y_AXIS));
        experiencePanel.setBackground(Color.WHITE);

        while (rs.next()) {
            int experienceId = rs.getInt("id");
            String title = rs.getString("title");
            String location = rs.getString("location");
            double price = rs.getDouble("price");

            // Individual experience panel
            JPanel singleExperiencePanel = new JPanel();
            singleExperiencePanel.setBackground(new Color(240, 240, 240));
            singleExperiencePanel.setLayout(new BoxLayout(singleExperiencePanel, BoxLayout.Y_AXIS));
            singleExperiencePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel experienceTitle = new JLabel(title);
            experienceTitle.setFont(new Font("Arial", Font.BOLD, 18));
            experienceTitle.setForeground(new Color(102, 102, 255));

            JLabel experienceLocation = new JLabel("Location: " + location);
            experienceLocation.setFont(new Font("Arial", Font.PLAIN, 14));

            JLabel experiencePrice = new JLabel("Price: $" + price);
            experiencePrice.setFont(new Font("Arial", Font.PLAIN, 14));

            singleExperiencePanel.add(experienceTitle);
            singleExperiencePanel.add(experienceLocation);
            singleExperiencePanel.add(experiencePrice);
            singleExperiencePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            singleExperiencePanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    new BookingPage(userId, experienceId, price).setVisible(true);
                }
            });

            experiencePanel.add(singleExperiencePanel);
            experiencePanel.add(Box.createVerticalStrut(10));
        }

        add(new JScrollPane(experiencePanel), BorderLayout.CENTER);
        revalidate();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error fetching experiences: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExperienceListingPage(1).setVisible(true);  // Example usage with a dummy user ID
        });
    }
}