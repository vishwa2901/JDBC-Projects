package com.bank;

import java.sql.*;

public class AccountCreation {

    public int createAccount(String name, String email, String phone, double openingBalance) throws SQLException {
        Connection con = DBConnection.getConnection();
        try {
            con.setAutoCommit(false);

            // 1. insert customer
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO customers(name, email, phone) VALUES (?,?,?)");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.executeUpdate();

            // 2. insert account for that customer (LAST_INSERT_ID = the customer just inserted)
            ps = con.prepareStatement("INSERT INTO accounts(customer_id, balance) VALUES (LAST_INSERT_ID(), ?)");
            ps.setDouble(1, openingBalance);
            ps.executeUpdate();

            // 3. get the new account number
            ResultSet rs = con.createStatement().executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            int accountNumber = rs.getInt(1);

            if (openingBalance > 0)
                TransactionHistory.record(con, accountNumber, "DEPOSIT", openingBalance, null);

            con.commit();
            return accountNumber;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.close();
        }
    }

    public void checkBalance(int accountNumber) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT c.name, a.balance FROM accounts a "
                   + "JOIN customers c ON a.customer_id = c.customer_id WHERE a.account_number = ?")) {
            ps.setInt(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) throw new SQLException("Account does not exist.");
            System.out.println(rs.getString("name") + " | Balance: " + rs.getDouble("balance"));
        }
    }
}