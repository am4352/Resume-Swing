package com.resumebuilder.dao;

import com.resumebuilder.model.Resume;

import java.sql.*;

// Database connection class
class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/student"; // Update with your database name
    private static final String USER = "root"; // Update with your username
    private static final String PASSWORD = "Shashwat"; // Update with your password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

public class ResumeDAO {

    public void saveResume(Resume resume) throws SQLException {
        String sql = "INSERT INTO resumes (job_title, first_name, last_name, email, phone, country, city, professional_summary, skills, professional_experience, job_role, company, start_date, end_date, responsibilities, degree, university, graduation_year) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, resume.getJobTitle());
            pstmt.setString(2, resume.getFirstName());
            pstmt.setString(3, resume.getLastName());
            pstmt.setString(4, resume.getEmail());
            pstmt.setString(5, resume.getPhone());
            pstmt.setString(6, resume.getCountry());
            pstmt.setString(7, resume.getCity());
            pstmt.setString(8, resume.getProfessionalSummary());
            pstmt.setString(9, resume.getSkills());
            pstmt.setString(10, resume.getProfessionalExperience());
            pstmt.setString(11, resume.getJobRole());
            pstmt.setString(12, resume.getCompany());
            pstmt.setString(13, resume.getStartDate());
            pstmt.setString(14, resume.getEndDate());
            pstmt.setString(15, resume.getResponsibilities());
            pstmt.setString(16, resume.getDegree());
            pstmt.setString(17, resume.getUniversity());
            pstmt.setString(18, resume.getGraduationYear());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating resume failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    resume.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating resume failed, no ID obtained.");
                }
            }
        }
    }
}