
package com.bank;

import java.sql.*;

public class DBConnection {

    static final String USER = "root";        // change
    static final String PASSWORD = "root";    // change

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", USER, PASSWORD);
    }

    // creates database + tables
    public static void init() throws SQLException {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", USER, PASSWORD);
             Statement st = con.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS bank_db");
        }

        try (Connection con = getConnection(); Statement st = con.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS customers ("
                    + "customer_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(100), email VARCHAR(100) UNIQUE, phone VARCHAR(15))");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS accounts ("
                    + "account_number INT AUTO_INCREMENT PRIMARY KEY, "
                    + "customer_id INT, balance DECIMAL(12,2) DEFAULT 0, "
                    + "FOREIGN KEY (customer_id) REFERENCES customers(customer_id)) AUTO_INCREMENT = 1001");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS transactions ("
                    + "txn_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "account_number INT, txn_type VARCHAR(20), amount DECIMAL(12,2), "
                    + "other_account INT, txn_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (account_number) REFERENCES accounts(account_number))");
        }
    }
}