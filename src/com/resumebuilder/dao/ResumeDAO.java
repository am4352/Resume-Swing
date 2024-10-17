package com.resumebuilder.dao;

import com.resumebuilder.model.Resume;

import java.sql.*;

public class ResumeDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/student"; // Update with your database URL
    private static final String USER = "root"; // Update with your database username
    private static final String PASSWORD = "anuj"; // Update with your database password

    private Connection connection;
    public ResumeDAO(Connection connection) {
        this.connection = connection;
    }

    public ResumeDAO() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void saveResume(Resume resume) throws SQLException {
        String sql = "INSERT INTO resumes (job_title, first_name, last_name, email, phone, country, city, professional_summary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, resume.getJobTitle());
            pstmt.setString(2, resume.getFirstName());
            pstmt.setString(3, resume.getLastName());
            pstmt.setString(4, resume.getEmail());
            pstmt.setString(5, resume.getPhone());
            pstmt.setString(6, resume.getCountry());
            pstmt.setString(7, resume.getCity());
            pstmt.setString(8, resume.getProfessionalSummary());

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

    // Close the connection when done
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
