package com.resumebuilder.dao;

import com.resumebuilder.model.Resume;
import com.resumebuilder.dao.DatabaseConnection;

import java.sql.*;

public class ResumeDAO {
    public void saveResume(Resume resume) throws SQLException {
        String sql = "INSERT INTO resumes (job_title, first_name, last_name, email, phone, country, city, professional_summary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, resume.getJobTitle());
            pstmt.setString(2, resume.getFirstName());  hello
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

    // Add more methods for updating, deleting, and retrieving resumes
}