package com.hotel;

import java.sql.*;

public class Billing {

    // nights = DATEDIFF(check_out, check_in);  bill = nights x price per night
    public static void showBill(int bookingId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT c.name, r.room_id, r.room_type, r.price_per_night, "
                   + "b.check_in_date, b.check_out_date, b.status, "
                   + "DATEDIFF(b.check_out_date, b.check_in_date) AS nights "
                   + "FROM bookings b "
                   + "JOIN customers c ON b.customer_id = c.customer_id "
                   + "JOIN rooms r ON b.room_id = r.room_id "
                   + "WHERE b.booking_id = ?")) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) throw new SQLException("Booking not found.");

            int nights = rs.getInt("nights");
            double price = rs.getDouble("price_per_night");

            System.out.println("------------- BILL -------------");
            System.out.println("Booking ID : " + bookingId);
            System.out.println("Guest      : " + rs.getString("name"));
            System.out.println("Room       : " + rs.getInt("room_id") + " (" + rs.getString("room_type") + ")");
            System.out.println("Stay       : " + rs.getDate("check_in_date") + " to " + rs.getDate("check_out_date"));
            System.out.println("Nights     : " + nights);
            System.out.println("Rate       : " + price);
            System.out.println("TOTAL      : " + (nights * price));
            System.out.println("Status     : " + rs.getString("status"));
            System.out.println("--------------------------------");
        }
    }
}