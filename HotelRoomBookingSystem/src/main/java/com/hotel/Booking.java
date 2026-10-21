package com.hotel;

import java.sql.*;

public class Booking {

    // customer + booking saved in ONE transaction; returns booking id
    public static int book(String name, String email, String phone, int roomId,
                           Date checkIn, Date checkOut) throws SQLException {

        if (!checkOut.after(checkIn)) throw new SQLException("Room is already booked.");

        Connection con = DBConnection.getConnection();
        try {
            con.setAutoCommit(false);

            // 1. add customer (ignored if the email already exists)
            PreparedStatement ps = con.prepareStatement(
                    "INSERT IGNORE INTO customers(name, email, phone) VALUES (?,?,?)");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.executeUpdate();

            // 2. get customer id using the unique email
            ps = con.prepareStatement("SELECT customer_id FROM customers WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int customerId = rs.getInt(1);

            // 3. is the room already booked for these dates?
            ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM bookings WHERE room_id = ? AND status IN ('BOOKED','CHECKED_IN') "
                  + "AND check_in_date < ? AND check_out_date > ?");
            ps.setInt(1, roomId);
            ps.setDate(2, checkOut);
            ps.setDate(3, checkIn);
            rs = ps.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) throw new SQLException("Room " + roomId + " is not available for these dates.");

            // 4. insert booking
            ps = con.prepareStatement(
                    "INSERT INTO bookings(customer_id, room_id, check_in_date, check_out_date, status) "
                  + "VALUES (?,?,?,?,'BOOKED')");
            ps.setInt(1, customerId);
            ps.setInt(2, roomId);
            ps.setDate(3, checkIn);
            ps.setDate(4, checkOut);
            ps.executeUpdate();

            // 5. get the new booking id
            ps = con.prepareStatement("SELECT MAX(booking_id) FROM bookings WHERE customer_id = ?");
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            rs.next();
            int bookingId = rs.getInt(1);

            con.commit();
            return bookingId;
        } catch (SQLException e) {
            con.rollback();            // nothing is saved if any step fails
            throw e;
        } finally {
            con.close();
        }
    }
}