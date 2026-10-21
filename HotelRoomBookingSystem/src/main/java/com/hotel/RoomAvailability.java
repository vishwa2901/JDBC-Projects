package com.hotel;

import java.sql.*;

public class RoomAvailability {

    // shows rooms that have NO active booking overlapping the given dates
    public static void showAvailable(Date checkIn, Date checkOut) throws SQLException {
        if (!checkOut.after(checkIn)) throw new SQLException("Check-out must be after check-in.");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT room_id, room_type, price_per_night FROM rooms WHERE room_id NOT IN "
                   + "(SELECT room_id FROM bookings WHERE status IN ('BOOKED','CHECKED_IN') "
                   + "AND check_in_date < ? AND check_out_date > ?)")) {
            ps.setDate(1, checkOut);
            ps.setDate(2, checkIn);
            ResultSet rs = ps.executeQuery();

            System.out.println("ROOM | TYPE | PRICE/NIGHT");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(rs.getInt("room_id") + " | " + rs.getString("room_type")
                        + " | " + rs.getDouble("price_per_night"));
            }
            if (!found) System.out.println("No rooms available for these dates.");
        }
    }
}