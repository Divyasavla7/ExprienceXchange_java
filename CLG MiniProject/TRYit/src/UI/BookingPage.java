package UI;

import UI.DatabaseConnection;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class BookingPage extends JFrame {

    private int userId;
    private int experienceId;
    private JTextField attendeesField;
    private JLabel totalAmountLabel;
    private double experiencePrice;

    public BookingPage(int userId, int experienceId, double experiencePrice) {
        this.userId = userId;
        this.experienceId = experienceId;
        this.experiencePrice = experiencePrice;
        initComponents();
    }

    private void initComponents() {
        setTitle("ExperienceXchange - Book Experience");

        // Set main panel layout and background color
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 248, 255)); // Light blue background
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Heading
        JLabel headerLabel = new JLabel("Book Your Experience", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(25, 25, 112)); // Midnight blue color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(headerLabel, gbc);

        // Attendees label and field
        JLabel attendeesLabel = new JLabel("Number of Attendees:");
        attendeesLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        attendeesLabel.setForeground(Color.DARK_GRAY);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(attendeesLabel, gbc);

        attendeesField = new JTextField(5);
        attendeesField.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        mainPanel.add(attendeesField, gbc);

        // Total amount label
        JLabel amountLabel = new JLabel("Total Amount:");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        amountLabel.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(amountLabel, gbc);

        totalAmountLabel = new JLabel("0.00");
        totalAmountLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        totalAmountLabel.setForeground(Color.BLUE);
        gbc.gridx = 1;
        mainPanel.add(totalAmountLabel, gbc);

        // Calculate button
        JButton calculateButton = new JButton("Calculate Total");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 16));
        calculateButton.setBackground(new Color(50, 205, 50)); // Lime green background
        calculateButton.setForeground(Color.WHITE);
        calculateButton.addActionListener(e -> calculateTotal());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(calculateButton, gbc);

        // Book button
        JButton bookButton = new JButton("Book Now");
        bookButton.setFont(new Font("Arial", Font.BOLD, 16));
        bookButton.setBackground(new Color(30, 144, 255)); // Dodger blue background
        bookButton.setForeground(Color.WHITE);
        bookButton.addActionListener(e -> bookExperience());
        gbc.gridy = 4;
        mainPanel.add(bookButton, gbc);

        // Add padding around the content
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Set layout and window properties
        add(mainPanel);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    private void calculateTotal() {
        try {
            int attendees = Integer.parseInt(attendeesField.getText());
            double totalAmount = attendees * experiencePrice;
            totalAmountLabel.setText(String.format("%.2f", totalAmount));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of attendees.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void bookExperience() {
    try {
        int attendees = Integer.parseInt(attendeesField.getText());
        double totalAmount = attendees * experiencePrice;

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Bookings (experience_id, user_id, attendees, total_amount) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, experienceId);
            stmt.setInt(2, userId);
            stmt.setInt(3, attendees);
            stmt.setDouble(4, totalAmount);
            stmt.executeUpdate();

            // Get the generated booking ID
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int bookingId = generatedKeys.getInt(1);
                // Open transaction confirmation page
                new TransactionConfirmationPage(bookingId, totalAmount).setVisible(true);
                JOptionPane.showMessageDialog(this, "Booking Successful! Please confirm the transaction.");
                this.dispose(); // Close the booking page
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error booking experience: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Please enter a valid number of attendees.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}

}
