package com.bank;

import java.sql.*;

public class TransactionHistory {

    // used by other classes inside their transaction
    static void record(Connection con, int account, String type, double amount, Integer other) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO transactions(account_number, txn_type, amount, other_account) VALUES (?,?,?,?)");
        ps.setInt(1, account);
        ps.setString(2, type);
        ps.setDouble(3, amount);
        ps.setObject(4, other);          // null is stored as NULL
        ps.executeUpdate();
    }

    public void showHistory(int accountNumber) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT txn_id, txn_type, amount, other_account, txn_time "
                   + "FROM transactions WHERE account_number = ? ORDER BY txn_id DESC")) {
            ps.setInt(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            System.out.println("ID | TYPE | AMOUNT | OTHER A/C | TIME");
            while (rs.next()) {
                System.out.println(rs.getInt("txn_id") + " | " + rs.getString("txn_type") + " | "
                        + rs.getDouble("amount") + " | " + rs.getObject("other_account") + " | "
                        + rs.getTimestamp("txn_time"));
            }
        }
    }
}