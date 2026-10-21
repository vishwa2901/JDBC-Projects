package com.hotel;

import java.sql.*;

public class DBConnection {

    static final String USER = "root";        // change
    static final String PASSWORD = "root";    // change

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_db", USER, PASSWORD);
    }

    // creates database + tables + sample rooms (called once from Main)
    public static void init() throws SQLException {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_db", USER, PASSWORD);
             Statement st = con.createStatement()) {

            st.executeUpdate("CREATE DATABASE IF NOT EXISTS hotel_db");
            st.executeUpdate("USE hotel_db");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS rooms ("
                    + "room_id INT PRIMARY KEY, room_type VARCHAR(20), price_per_night DECIMAL(10,2))");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS customers ("
                    + "customer_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(100), email VARCHAR(100) UNIQUE, phone VARCHAR(15))");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS bookings ("
                    + "booking_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "customer_id INT, room_id INT, "
                    + "check_in_date DATE, check_out_date DATE, "
                    + "status VARCHAR(15), "
                    + "FOREIGN KEY (customer_id) REFERENCES customers(customer_id), "
                    + "FOREIGN KEY (room_id) REFERENCES rooms(room_id))");

            // sample rooms (IGNORE skips them if they already exist)
            st.executeUpdate("INSERT IGNORE INTO rooms VALUES "
                    + "(101,'SINGLE',1500), (102,'SINGLE',1500), "
                    + "(201,'DOUBLE',2500), (202,'DOUBLE',2500), (301,'SUITE',5000)");
        }
    }
}