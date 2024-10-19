package UI;
// LandingPage.javapackage UI;
import UI.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import java.awt.*;

public class LandingPage extends JFrame {
    private int userId;

    public LandingPage(int userId) {
        this.userId = userId;
        initComponents();
        fetchFeaturedExperiences();
    }

    private void initComponents() {
        setTitle("ExperienceXchange- Landing Page");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(102, 153, 255));
        JLabel logoLabel = new JLabel("ExperienceXchange");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 36));
        logoLabel.setForeground(Color.WHITE);
        headerPanel.add(logoLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Search Field
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 18));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Buttons
        JButton findExperienceButton = new JButton("Find an Experience");
        findExperienceButton.setFont(new Font("Arial", Font.BOLD, 18));
        findExperienceButton.setBackground(new Color(0, 204, 102));
        findExperienceButton.setForeground(Color.WHITE);
        findExperienceButton.addActionListener(e -> new ExperienceListingPage(userId).setVisible(true));

        JButton hostExperienceButton = new JButton("Host an Experience");
        hostExperienceButton.setFont(new Font("Arial", Font.BOLD, 18));
        hostExperienceButton.setBackground(new Color(255, 153, 51));
        hostExperienceButton.setForeground(Color.WHITE);
        hostExperienceButton.addActionListener(e -> new RegisterPage().setVisible(true));

        // Featured Experiences Label
        JLabel featuredLabel = new JLabel("Featured Experiences:");
        featuredLabel.setFont(new Font("Arial", Font.BOLD, 24));
        featuredLabel.setForeground(new Color(102, 102, 102));

        // Adding Components to Main Panel
        mainPanel.add(searchField);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(findExperienceButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(hostExperienceButton);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(featuredLabel);

        add(mainPanel, BorderLayout.CENTER);
        pack();
    }

    private void fetchFeaturedExperiences() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT title, location, price FROM Experiences ORDER BY date_listed DESC LIMIT 5";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Panel to hold featured experiences
            JPanel featuredPanel = new JPanel();
            featuredPanel.setLayout(new BoxLayout(featuredPanel, BoxLayout.Y_AXIS));
            featuredPanel.setBackground(Color.WHITE);

            while (rs.next()) {
                String title = rs.getString("title");
                String location = rs.getString("location");
                double price = rs.getDouble("price");

                // Experience panel
                JPanel experiencePanel = new JPanel();
                experiencePanel.setBackground(new Color(240, 240, 240));
                experiencePanel.setLayout(new BoxLayout(experiencePanel, BoxLayout.Y_AXIS));
                experiencePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JLabel titleLabel = new JLabel(title);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
                titleLabel.setForeground(new Color(102, 102, 255));

                JLabel locationLabel = new JLabel("Location: " + location);
                locationLabel.setFont(new Font("Arial", Font.PLAIN, 14));

                JLabel priceLabel = new JLabel("Price: Rs" + price);
                priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));

                experiencePanel.add(titleLabel);
                experiencePanel.add(locationLabel);
                experiencePanel.add(priceLabel);
                experiencePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                experiencePanel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        new ExperienceListingPage(userId).setVisible(true);
                    }
                });

                featuredPanel.add(experiencePanel);
                featuredPanel.add(Box.createVerticalStrut(10));
            }

            // Adding featured experiences to main panel
            add(featuredPanel, BorderLayout.SOUTH);
            revalidate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching experiences: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT id FROM Users WHERE username = 'your_username'"; // Replace 'your_username' with the actual username
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    new LandingPage(userId).setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}


