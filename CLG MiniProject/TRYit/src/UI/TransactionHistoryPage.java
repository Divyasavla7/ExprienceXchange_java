package UI;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class TransactionHistoryPage extends JFrame {
    private int userId; // Actual user ID

    public TransactionHistoryPage(int userId) {
        this.userId = userId; // Store the user ID
        initComponents();
        fetchTransactionHistory(); // Fetch transaction history for the user
    }

    private void initComponents() {
        setTitle("TRYIT - Transaction History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel historyLabel = new JLabel("Your Transaction History:");
        historyLabel.setFont(new Font("Arial", Font.BOLD, 24));
        historyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        historyLabel.setForeground(new Color(25, 25, 112));

        mainPanel.add(historyLabel, BorderLayout.NORTH);
        
        JPanel transactionPanel = new JPanel();
        transactionPanel.setLayout(new BoxLayout(transactionPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(transactionPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void fetchTransactionHistory() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT Experiences.title, Transactions.transaction_date, Transactions.amount, Transactions.payment_status " +
                         "FROM Transactions " +
                         "JOIN Bookings ON Transactions.booking_id " +
                         "JOIN Experiences ON Bookings.experience_id  " +
                         "WHERE Bookings.user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId); // Set the actual user ID
            ResultSet rs = stmt.executeQuery();

            JPanel transactionPanel = (JPanel) ((JScrollPane) getContentPane().getComponent(1)).getViewport().getView();
            transactionPanel.removeAll(); // Clear previous entries

            if (!rs.next()) {
                JLabel noTransactionsLabel = new JLabel("No transactions found.");
                noTransactionsLabel.setFont(new Font("Arial", Font.ITALIC, 16));
                noTransactionsLabel.setForeground(new Color(255, 0, 0));
                transactionPanel.add(noTransactionsLabel);
            } else {
                do {
                    String title = rs.getString("title");
                    String date = rs.getString("transaction_date");
                    double amount = rs.getDouble("amount");
                    String status = rs.getString("payment_status");

                    JPanel itemPanel = new JPanel(new BorderLayout());
                    itemPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
                    itemPanel.setBackground(new Color(245, 245, 245));

                    JLabel titleLabel = new JLabel("Experience: " + title);
                    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    titleLabel.setForeground(new Color(0, 100, 0));

                    JLabel detailsLabel = new JLabel("Date: " + date + " | Amount: $" + amount + " | Status: " + status);
                    detailsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    detailsLabel.setForeground(new Color(105, 105, 105));

                    itemPanel.add(titleLabel, BorderLayout.NORTH);
                    itemPanel.add(detailsLabel, BorderLayout.CENTER);
                    transactionPanel.add(itemPanel);
                } while (rs.next());
            }

            transactionPanel.revalidate(); // Refresh the panel
            transactionPanel.repaint(); // Update the UI
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching transaction history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
