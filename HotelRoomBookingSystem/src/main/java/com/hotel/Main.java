package com.hotel;

import java.sql.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        DBConnection.init();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1.Room Availability 2.Booking 3.Check-In 4.Check-Out 5.Billing 0.Exit");
            try {
                System.out.print("Choose: ");
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1 -> {
                        System.out.print("Check-in date (yyyy-mm-dd): ");
                        Date in = Date.valueOf(sc.nextLine());
                        System.out.print("Check-out date (yyyy-mm-dd): ");
                        Date out = Date.valueOf(sc.nextLine());
                        RoomAvailability.showAvailable(in, out);
                    }
                    case 2 -> {
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Email: ");
                        String email = sc.nextLine();
                        System.out.print("Phone: ");
                        String phone = sc.nextLine();
                        System.out.print("Room number: ");
                        int room = Integer.parseInt(sc.nextLine());
                        System.out.print("Check-in date (yyyy-mm-dd): ");
                        Date in = Date.valueOf(sc.nextLine());
                        System.out.print("Check-out date (yyyy-mm-dd): ");
                        Date out = Date.valueOf(sc.nextLine());
                        System.out.println("Booked! Booking ID = " + Booking.book(name, email, phone, room, in, out));
                    }
                    case 3 -> {
                        System.out.print("Booking ID: ");
                        CheckIn.checkIn(Integer.parseInt(sc.nextLine()));
                    }
                    case 4 -> {
                        System.out.print("Booking ID: ");
                        CheckOut.checkOut(Integer.parseInt(sc.nextLine()));
                    }
                    case 5 -> {
                        System.out.print("Booking ID: ");
                        Billing.showBill(Integer.parseInt(sc.nextLine()));
                    }
                    case 0 -> { return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}