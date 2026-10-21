
package com.studentmanagement;

import java.sql.Connection;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentManagement sm = new StudentManagement();

        // Create database and tables automatically
        try {
            Connection con = DBConnection.getConnection();
            con.close();

            System.out.println("Database and tables are ready.");

        } catch (Exception e) {
            System.out.println("Database connection failed.");
            System.out.println(e.getMessage());
            sc.close();
            return;
        }

        int choice = 0;

        while (choice != 13) {

            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Register Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Add Course");
            System.out.println("6. View Courses");
            System.out.println("7. Enroll Student");
            System.out.println("8. View Enrollments");
            System.out.println("9. Mark Attendance");
            System.out.println("10. View Attendance");
            System.out.println("11. Enter Marks");
            System.out.println("12. View Marks and Generate Results");
            System.out.println("13. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:
                        sm.addStudent();
                        break;

                    case 2:
                        sm.viewStudents();
                        break;

                    case 3:
                        sm.updateStudent();
                        break;

                    case 4:
                        sm.deleteStudent();
                        break;

                    case 5:
                        sm.addCourse();
                        break;

                    case 6:
                        sm.viewCourses();
                        break;

                    case 7:
                        sm.enrollStudent();
                        break;

                    case 8:
                        sm.viewEnrollments();
                        break;

                    case 9:
                        sm.markAttendance();
                        break;

                    case 10:
                        sm.viewAttendance();
                        break;

                    case 11:
                        sm.enterMarks();
                        break;

                    case 12:
                        System.out.println("\n1. View Marks");
                        System.out.println("2. Generate Results");
                        System.out.print("Enter choice: ");

                        int subChoice =
                            Integer.parseInt(sc.nextLine());

                        if (subChoice == 1) {
                            sm.viewMarks();
                        } else if (subChoice == 2) {
                            sm.generateResult();
                        } else {
                            System.out.println("Invalid choice.");
                        }
                        break;

                    case 13:
                        System.out.println("Thank you!");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (Exception e) {
                System.out.println("Operation failed: "
                    + e.getMessage());
            }
        }

        sc.close();
    }
}
