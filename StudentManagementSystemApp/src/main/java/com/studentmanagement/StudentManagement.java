
package com.studentmanagement;

import java.sql.*;
import java.util.Scanner;

public class StudentManagement {

    Scanner sc = new Scanner(System.in);

    // 1. Student Registration
    public void addStudent() throws Exception {

        Connection con = DBConnection.getConnection();

        System.out.print("Enter student name: ");
        String name = sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter phone: ");
        String phone = sc.nextLine();

        String sql =
            "INSERT INTO students(name, email, phone) VALUES(?,?,?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, phone);

        ps.executeUpdate();

        System.out.println("Student registered successfully.");

        ps.close();
        con.close();
    }

    // View all students
    public void viewStudents() throws Exception {

        Connection con = DBConnection.getConnection();

        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery(
            "SELECT * FROM students");

        while (rs.next()) {
            System.out.println(
                rs.getInt("student_id") + " | " +
                rs.getString("name") + " | " +
                rs.getString("email") + " | " +
                rs.getString("phone"));
        }

        rs.close();
        st.close();
        con.close();
    }

    // Update student
    public void updateStudent() throws Exception {

        Connection con = DBConnection.getConnection();

        System.out.print("Enter student ID: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Enter new name: ");
        String name = sc.nextLine();

        System.out.print("Enter new email: ");
        String email = sc.nextLine();

        System.out.print("Enter new phone: ");
        String phone = sc.nextLine();

        PreparedStatement ps = con.prepareStatement(
            "UPDATE students SET name=?, email=?, phone=? " +
            "WHERE student_id=?");

        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, phone);
        ps.setInt(4, id);

        int rows = ps.executeUpdate();

        System.out.println(rows > 0
            ? "Student updated successfully."
            : "Student ID not found.");

        ps.close();
        con.close();
    }

    // Delete student
    public void deleteStudent() throws Exception {

        Connection con = DBConnection.getConnection();

        System.out.print("Enter student ID: ");
        int id = Integer.parseInt(sc.nextLine());

        PreparedStatement ps = con.prepareStatement(
            "DELETE FROM students WHERE student_id=?");

        ps.setInt(1, id);

        try {
            int rows = ps.executeUpdate();

            System.out.println(rows > 0
                ? "Student deleted successfully."
                : "Student ID not found.");

        } catch (SQLException e) {
            System.out.println(
                "Cannot delete: student has course enrollments.");
        }

        ps.close();
        con.close();
    }

    // 2. Add course
    public void addCourse() throws Exception {

        Connection con = DBConnection.getConnection();

        System.out.print("Enter course name: ");
        String name = sc.nextLine();

        System.out.print("Enter maximum marks: ");
        int max = Integer.parseInt(sc.nextLine());

        if (max <= 0) {
            System.out.println("Maximum marks must be positive.");
            con.close();
            return;
        }

        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO courses(course_name, max_marks) VALUES(?,?)");

        ps.setString(1, name);
        ps.setInt(2, max);

        ps.executeUpdate();

        System.out.println("Course added successfully.");

        ps.close();
        con.close();
    }

    // View courses
    public void viewCourses() throws Exception {

        Connection con = DBConnection.getConnection();

        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery(
            "SELECT * FROM courses");

        while (rs.next()) {
            System.out.println(
                rs.getInt("course_id") + " | " +
                rs.getString("course_name") + " | Max marks: " +
                rs.getInt("max_marks"));
        }

        rs.close();
        st.close();
        con.close();
    }

    // 3. Course enrollment with transaction
    public void enrollStudent() throws Exception {

        Connection con = DBConnection.getConnection();

        System.out.print("Enter student ID: ");
        int sid = Integer.parseInt(sc.nextLine());

        System.out.print("Enter course ID: ");
        int cid = Integer.parseInt(sc.nextLine());

        try {
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO enrollments " +
                "(student_id, course_id, enrollment_date) " +
                "VALUES(?,?,?)");

            ps.setInt(1, sid);
            ps.setInt(2, cid);
            ps.setDate(3, Date.valueOf(
                java.time.LocalDate.now()));

            ps.executeUpdate();

            con.commit();

            System.out.println("Student enrolled successfully.");

            ps.close();

        } catch (SQLException e) {

            con.rollback();

            System.out.println(
                "Enrollment failed. Check student/course IDs " +
                "or whether the student is already enrolled.");

        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

    // View enrollments using JOIN
    public void viewEnrollments() throws Exception {

        Connection con = DBConnection.getConnection();

        String sql =
            "SELECT e.enrollment_id, s.name, c.course_name, " +
            "e.enrollment_date " +
            "FROM enrollments e " +
            "JOIN students s ON e.student_id=s.student_id " +
            "JOIN courses c ON e.course_id=c.course_id";

        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            System.out.println(
                "Enrollment ID: " + rs.getInt("enrollment_id") +
                " | Student: " + rs.getString("name") +
                " | Course: " + rs.getString("course_name") +
                " | Date: " + rs.getDate("enrollment_date"));
        }

        rs.close();
        st.close();
        con.close();
    }

    // 4. Attendance management
    public void markAttendance() throws Exception {

        Connection con = DBConnection.getConnection();

        System.out.print("Enter enrollment ID: ");
        int eid = Integer.parseInt(sc.nextLine());

        System.out.print("Enter date (yyyy-mm-dd): ");
        Date date = Date.valueOf(sc.nextLine());

        System.out.print("Enter status (Present/Absent): ");
        String status = sc.nextLine();

        if (!status.equalsIgnoreCase("Present") &&
            !status.equalsIgnoreCase("Absent")) {

            System.out.println("Invalid attendance status.");
            con.close();
            return;
        }

        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO attendance " +
            "(enrollment_id, attendance_date, status) VALUES(?,?,?)");

        ps.setInt(1, eid);
        ps.setDate(2, date);

        ps.setString(3,
            status.equalsIgnoreCase("Present")
                ? "Present" : "Absent");

        try {
            ps.executeUpdate();
            System.out.println("Attendance recorded successfully.");
        } catch (SQLException e) {
            System.out.println(
                "Failed. Check enrollment ID or duplicate date.");
        }

        ps.close();
        con.close();
    }

    // View attendance
    public void viewAttendance() throws Exception {

        Connection con = DBConnection.getConnection();

        String sql =
            "SELECT s.name, c.course_name, a.attendance_date, a.status " +
            "FROM attendance a " +
            "JOIN enrollments e ON a.enrollment_id=e.enrollment_id " +
            "JOIN students s ON e.student_id=s.student_id " +
            "JOIN courses c ON e.course_id=c.course_id " +
            "ORDER BY a.attendance_date";

        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            System.out.println(
                rs.getString("name") + " | " +
                rs.getString("course_name") + " | " +
                rs.getDate("attendance_date") + " | " +
                rs.getString("status"));
        }

        rs.close();
        st.close();
        con.close();
    }

    // 5. Marks entry
    public void enterMarks() throws Exception {

        Connection con = DBConnection.getConnection();

        System.out.print("Enter enrollment ID: ");
        int eid = Integer.parseInt(sc.nextLine());

        System.out.print("Enter marks obtained: ");
        double marks = Double.parseDouble(sc.nextLine());

        PreparedStatement check = con.prepareStatement(
            "SELECT c.max_marks FROM enrollments e " +
            "JOIN courses c ON e.course_id=c.course_id " +
            "WHERE e.enrollment_id=?");

        check.setInt(1, eid);

        ResultSet rs = check.executeQuery();

        if (!rs.next()) {
            System.out.println("Enrollment ID not found.");
            rs.close();
            check.close();
            con.close();
            return;
        }

        int max = rs.getInt("max_marks");

        rs.close();
        check.close();

        if (marks < 0 || marks > max) {
            System.out.println(
                "Marks must be between 0 and " + max);
            con.close();
            return;
        }

        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO marks(enrollment_id, marks_obtained) " +
            "VALUES(?,?) ON DUPLICATE KEY UPDATE " +
            "marks_obtained=VALUES(marks_obtained)");

        ps.setInt(1, eid);
        ps.setDouble(2, marks);

        ps.executeUpdate();

        System.out.println("Marks saved successfully.");

        ps.close();
        con.close();
    }

    // View marks
    public void viewMarks() throws Exception {

        Connection con = DBConnection.getConnection();

        String sql =
            "SELECT s.name, c.course_name, m.marks_obtained, " +
            "c.max_marks " +
            "FROM marks m " +
            "JOIN enrollments e ON m.enrollment_id=e.enrollment_id " +
            "JOIN students s ON e.student_id=s.student_id " +
            "JOIN courses c ON e.course_id=c.course_id";

        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            System.out.println(
                rs.getString("name") + " | " +
                rs.getString("course_name") + " | Marks: " +
                rs.getDouble("marks_obtained") + "/" +
                rs.getInt("max_marks"));
        }

        rs.close();
        st.close();
        con.close();
    }

    // Result generation
    public void generateResult() throws Exception {

        Connection con = DBConnection.getConnection();

        String sql =
            "SELECT s.name, c.course_name, m.marks_obtained, " +
            "c.max_marks " +
            "FROM marks m " +
            "JOIN enrollments e ON m.enrollment_id=e.enrollment_id " +
            "JOIN students s ON e.student_id=s.student_id " +
            "JOIN courses c ON e.course_id=c.course_id " +
            "ORDER BY s.name";

        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {

            String name = rs.getString("name");
            String course = rs.getString("course_name");
            double marks = rs.getDouble("marks_obtained");
            int max = rs.getInt("max_marks");

            double percentage = marks * 100.0 / max;

            String result = percentage >= 40 ? "PASS" : "FAIL";

            System.out.println("\nStudent: " + name);
            System.out.println("Course: " + course);
            System.out.println("Marks: " + marks + "/" + max);
            System.out.printf("Percentage: %.2f%%%n", percentage);
            System.out.println("Result: " + result);
        }

        rs.close();
        st.close();
        con.close();
    }
}
