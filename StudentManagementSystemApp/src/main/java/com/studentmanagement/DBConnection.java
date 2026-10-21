
package com.studentmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/";

    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection con = DriverManager.getConnection(
                URL, USER, PASSWORD);

        Statement st = con.createStatement();

        st.executeUpdate(
                "CREATE DATABASE IF NOT EXISTS studentdatabase");

        st.close();
        con.close();

        Connection connection = DriverManager.getConnection(
                URL + "studentdatabase", USER, PASSWORD);

        Statement table = connection.createStatement();

        table.executeUpdate(
            "CREATE TABLE IF NOT EXISTS students (" +
            "student_id INT PRIMARY KEY AUTO_INCREMENT, " +
            "name VARCHAR(100) NOT NULL, " +
            "email VARCHAR(100) UNIQUE NOT NULL, " +
            "phone VARCHAR(15))");

        table.executeUpdate(
            "CREATE TABLE IF NOT EXISTS courses (" +
            "course_id INT PRIMARY KEY AUTO_INCREMENT, " +
            "course_name VARCHAR(100) NOT NULL, " +
            "max_marks INT NOT NULL DEFAULT 100)");

        table.executeUpdate(
            "CREATE TABLE IF NOT EXISTS enrollments (" +
            "enrollment_id INT PRIMARY KEY AUTO_INCREMENT, " +
            "student_id INT NOT NULL, " +
            "course_id INT NOT NULL, " +
            "enrollment_date DATE NOT NULL, " +
            "UNIQUE(student_id, course_id), " +
            "FOREIGN KEY(student_id) REFERENCES students(student_id), " +
            "FOREIGN KEY(course_id) REFERENCES courses(course_id))");

        table.executeUpdate(
            "CREATE TABLE IF NOT EXISTS attendance (" +
            "attendance_id INT PRIMARY KEY AUTO_INCREMENT, " +
            "enrollment_id INT NOT NULL, " +
            "attendance_date DATE NOT NULL, " +
            "status VARCHAR(10) NOT NULL, " +
            "UNIQUE(enrollment_id, attendance_date), " +
            "FOREIGN KEY(enrollment_id) REFERENCES enrollments(enrollment_id))");

        table.executeUpdate(
            "CREATE TABLE IF NOT EXISTS marks (" +
            "marks_id INT PRIMARY KEY AUTO_INCREMENT, " +
            "enrollment_id INT NOT NULL UNIQUE, " +
            "marks_obtained DECIMAL(5,2) NOT NULL, " +
            "FOREIGN KEY(enrollment_id) REFERENCES enrollments(enrollment_id))");

        table.close();

        return connection;
    }
}
