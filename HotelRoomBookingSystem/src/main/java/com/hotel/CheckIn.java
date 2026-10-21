package com.hotel;

import java.sql.*;

public class CheckIn {

    public static void checkIn(int bookingId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE bookings SET status = 'CHECKED_IN' WHERE booking_id = ? AND status = 'BOOKED'")) {
            ps.setInt(1, bookingId);
            if (ps.executeUpdate() == 0) throw new SQLException("Booking not found or already checked in.");
            System.out.println("Check-in successful.");
        }
    }
}