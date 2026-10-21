package com.bank;

import java.sql.*;

public class FundTransfer {

    public void transfer(int from, int to, double amount) throws SQLException {
        if (amount <= 0) throw new SQLException("Amount must be greater than 0.");
        if (from == to) throw new SQLException("Cannot transfer to the same account.");

        Connection con = DBConnection.getConnection();
        try {
            con.setAutoCommit(false);                       // start transaction

            // Step 1: debit sender (only if balance is enough)
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE account_number = ? AND balance >= ?");
            ps.setDouble(1, amount);
            ps.setInt(2, from);
            ps.setDouble(3, amount);
            if (ps.executeUpdate() == 0) throw new SQLException("Insufficient balance or sender account not found.");

            // Step 2: credit receiver
            ps = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE account_number = ?");
            ps.setDouble(1, amount);
            ps.setInt(2, to);
            if (ps.executeUpdate() == 0) throw new SQLException("Receiver account not found.");

            // Step 3: history for both accounts
            TransactionHistory.record(con, from, "TRANSFER_OUT", amount, to);
            TransactionHistory.record(con, to, "TRANSFER_IN", amount, from);

            con.commit();                                   // all steps OK
            System.out.println("Transfer successful.");
        } catch (SQLException e) {
            con.rollback();                                 // any failure: sender's money is restored
            System.out.println("Transfer failed. Rolled back, no money deducted.");
            throw e;
        } finally {
            con.close();
        }
    }
}
