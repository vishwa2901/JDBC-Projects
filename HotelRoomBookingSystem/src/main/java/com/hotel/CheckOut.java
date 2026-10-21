package com.hotel;

import java.sql.*;

public class CheckOut {

    public static void checkOut(int bookingId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE bookings SET status = 'CHECKED_OUT' WHERE booking_id = ? AND status = 'CHECKED_IN'")) {
            ps.setInt(1, bookingId);
            if (ps.executeUpdate() == 0) throw new SQLException("Booking not found or guest is not checked in.");
            System.out.println("Check-out successful.");
        }
        //Billing.showBill(bookingId);       // print the final bill
    }
}