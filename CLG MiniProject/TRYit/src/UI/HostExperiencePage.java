package UI;

import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.swing.*;

public class HostExperiencePage extends JFrame {
    private JTextField titleField, descriptionField, locationField, priceField, durationField;
    private int hostId;

    public HostExperiencePage(int hostId) {
        this.hostId = hostId;
        initComponents();
    }

    private void initComponents() {
        setTitle("Host Experience");

        // Create the main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title field
        JLabel titleLabel = new JLabel("Experience Title:");
        titleField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(titleField, gbc);

        // Description field
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(descriptionLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(descriptionField, gbc);

        // Location field
        JLabel locationLabel = new JLabel("Location:");
        locationField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(locationLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(locationField, gbc);

        // Price field
        JLabel priceLabel = new JLabel("Price:");
        priceField = new JTextField(10);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(priceField, gbc);

        // Duration field
        JLabel durationLabel = new JLabel("Duration (minutes):");
        durationField = new JTextField(10);
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(durationLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(durationField, gbc);

        // Submit button
        JButton submitButton = new JButton("Host Experience");
        submitButton.addActionListener(e -> createExperience());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(submitButton, gbc);

        add(mainPanel);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createExperience() {
        try {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String location = locationField.getText();
            BigDecimal price = new BigDecimal(priceField.getText());
            int duration = Integer.parseInt(durationField.getText());

            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO Experiences (title, description, location, host_id, price, duration) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, title);
                stmt.setString(2, description);
                stmt.setString(3, location);
                stmt.setInt(4, hostId);
                stmt.setBigDecimal(5, price);
                stmt.setInt(6, duration);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Experience hosted successfully!");
                this.dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
