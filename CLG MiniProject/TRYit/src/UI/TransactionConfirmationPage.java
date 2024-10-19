/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class TransactionConfirmationPage extends JFrame {

    private int bookingId; // To link the transaction to the booking
    private double totalAmount; // The total amount for the transaction

    public TransactionConfirmationPage(int bookingId, double totalAmount) {
        this.bookingId = bookingId;
        this.totalAmount = totalAmount;
        initComponents();
    }

    private void initComponents() {
        setTitle("Complete Transaction");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255)); // Light Blue Background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Complete Your Transaction", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, gbc);

        // Total Amount Label
        JLabel amountLabel = new JLabel("Total Amount: $" + String.format("%.2f", totalAmount));
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 1;
        mainPanel.add(amountLabel, gbc);

        // Payment Status Label
        JLabel paymentStatusLabel = new JLabel("Payment Status:");
        paymentStatusLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 2;
        mainPanel.add(paymentStatusLabel, gbc);

        // Payment Status (Placeholder for now)
        JTextField paymentStatusField = new JTextField("completed", 15);
        paymentStatusField.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 3;
        mainPanel.add(paymentStatusField, gbc);

        // Confirm Button
        JButton confirmButton = new JButton("Confirm Transaction");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 16));
        confirmButton.addActionListener(e -> completeTransaction());
        gbc.gridy = 4;
        mainPanel.add(confirmButton, gbc);

        add(mainPanel);
        setVisible(true);
    }

    private void completeTransaction() {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "INSERT INTO Transactions (booking_id, payment_status, amount) VALUES (?, 'completed', ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, bookingId);
        stmt.setDouble(2, totalAmount);
        stmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "Transaction Completed Successfully!");
        dispose(); // Close the transaction confirmation window
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error completing transaction: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

}
