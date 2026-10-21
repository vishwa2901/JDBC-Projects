
package com.bank;

import java.sql.*;

public class Withdrawal {

    public void withdraw(int accountNumber, double amount) throws SQLException {
        if (amount <= 0) throw new SQLException("Amount must be greater than 0.");

        Connection con = DBConnection.getConnection();
        try {
            con.setAutoCommit(false);

            // deducts only if balance is enough
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE account_number = ? AND balance >= ?");
            ps.setDouble(1, amount);
            ps.setInt(2, accountNumber);
            ps.setDouble(3, amount);
            if (ps.executeUpdate() == 0) throw new SQLException("Insufficient balance or account not found.");

            TransactionHistory.record(con, accountNumber, "WITHDRAWAL", amount, null);
            con.commit();
            System.out.println("Withdrawal successful.");
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.close();
        }
    }
}