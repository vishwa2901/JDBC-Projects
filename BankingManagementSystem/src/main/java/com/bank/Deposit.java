
package com.bank;

import java.sql.*;

public class Deposit {

    public void deposit(int accountNumber, double amount) throws SQLException {
        if (amount <= 0) throw new SQLException("Amount must be greater than 0.");

        Connection con = DBConnection.getConnection();
        try {
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE account_number = ?");
            ps.setDouble(1, amount);
            ps.setInt(2, accountNumber);
            if (ps.executeUpdate() == 0) throw new SQLException("Account does not exist.");

            TransactionHistory.record(con, accountNumber, "DEPOSIT", amount, null);
            con.commit();
            System.out.println("Deposit successful.");
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.close();
        }
    }
}