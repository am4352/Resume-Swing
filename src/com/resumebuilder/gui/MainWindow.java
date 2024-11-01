package com.resumebuilder.gui;

import com.resumebuilder.dao.ResumeDAO;
import com.resumebuilder.model.Resume;
import com.resumebuilder.util.PdfGenerator; // test

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainWindow extends JFrame {
    private JTabbedPane tabbedPane;
    private JTextArea summaryArea, skillsArea, educationArea;
    private JTextField jobTitleField, firstNameField, lastNameField, emailField, phoneField, countryField, cityField;
    private JTextField jobRoleField, companyField, startDateField, endDateField;
    private JTextArea responsibilitiesArea;
    private JTextField degreeField, universityField, graduationYearField;

    public MainWindow() {
        setTitle("Resume Builder");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tabbedPane = new JTabbedPane();
        setContentPane(tabbedPane); // Set tabbedPane as the main content

        addPersonalDetailsTab();
        addProfessionalSummaryTab();
        addSkillsTab();
        addProfessionalExperienceTab();
        addEducationTab();

        setupMenuBar();
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JButton downloadPdfButton = new JButton("Download PDF");
        downloadPdfButton.addActionListener(e -> downloadPdf());
        menuBar.add(downloadPdfButton);

        JButton saveButton = new JButton("Save Resume");
        saveButton.addActionListener(e -> saveResume());
        menuBar.add(saveButton);
    }

    private void addPersonalDetailsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontal space

        String[] labels = {"Job Title", "First Name", "Last Name", "Email", "Phone", "Country", "City"};
        JTextField[] fields = {
                jobTitleField = new JTextField(),
                firstNameField = new JTextField(),
                lastNameField = new JTextField(),
                emailField = new JTextField(),
                phoneField = new JTextField(),
                countryField = new JTextField(),
                cityField = new JTextField()
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }

        tabbedPane.addTab("Personal Details", panel);
    }

    private void addProfessionalSummaryTab() {
        JPanel panel = new JPanel(new BorderLayout());
        summaryArea = new JTextArea(5, 30);
        panel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);
        tabbedPane.addTab("Professional Summary", panel);
    }

    private void addSkillsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        skillsArea = new JTextArea(5, 30);
        panel.add(new JScrollPane(skillsArea), BorderLayout.CENTER);
        tabbedPane.addTab("Skills", panel);
    }

    private void addProfessionalExperienceTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontal space

        // Initialize fields
        jobRoleField = new JTextField();
        companyField = new JTextField();
        startDateField = new JTextField();
        endDateField = new JTextField();
        responsibilitiesArea = new JTextArea(5, 20);

        // Add components to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Job Role"), gbc);
        gbc.gridx = 1;
        panel.add(jobRoleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Company"), gbc);
        gbc.gridx = 1;
        panel.add(companyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Start Date"), gbc);
        gbc.gridx = 1;
        panel.add(startDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("End Date"), gbc);
        gbc.gridx = 1;
        panel.add(endDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Responsibilities"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(responsibilitiesArea), gbc);

        tabbedPane.addTab("Professional Experience", panel);
    }

    private void addEducationTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontal space

        // Initialize JTextFields for Education fields
        degreeField = new JTextField();
        universityField = new JTextField();
        graduationYearField = new JTextField();

        // Add components to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Degree"), gbc);
        gbc.gridx = 1;
        panel.add(degreeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("University"), gbc);
        gbc.gridx = 1;
        panel.add(universityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Graduation Year"), gbc);
        gbc.gridx = 1;
        panel.add(graduationYearField, gbc);

        // Add the panel to the tabbedPane
        tabbedPane.addTab("Education", panel);
    }

    private void downloadPdf() {
        Resume resume = new Resume();
        resume.setJobTitle(jobTitleField.getText());
        resume.setFirstName(firstNameField.getText());
        resume.setLastName(lastNameField.getText());
        resume.setEmail(emailField.getText());
        resume.setPhone(phoneField.getText());
        resume.setCountry(countryField.getText());
        resume.setCity(cityField.getText());
        resume.setProfessionalSummary(summaryArea.getText());
        resume.setSkills(skillsArea.getText());
        resume.setJobRole(jobRoleField.getText());
        resume.setCompany(companyField.getText());
        resume.setStartDate(startDateField.getText());
        resume.setEndDate(endDateField.getText());
        resume.setResponsibilities(responsibilitiesArea.getText());
        resume.setDegree(degreeField.getText());
        resume.setUniversity(universityField.getText());
        resume.setGraduationYear(graduationYearField.getText());

        PdfGenerator pdfGenerator = new PdfGenerator();
        pdfGenerator.generatePdf(resume, "resume.pdf");

        JOptionPane.showMessageDialog(this, "PDF downloaded successfully.");
    }

    private void saveResume() {
        Resume resume = new Resume();
        resume.setJobTitle(jobTitleField.getText());
        resume.setFirstName(firstNameField.getText());
        resume.setLastName(lastNameField.getText());
        resume.setEmail(emailField.getText());
        resume.setPhone(phoneField.getText());
        resume.setCountry(countryField.getText());
        resume.setCity(cityField.getText());
        resume.setProfessionalSummary(summaryArea.getText());
        resume.setSkills(skillsArea.getText());
        resume.setJobRole(jobRoleField.getText());
        resume.setCompany(companyField.getText());
        resume.setStartDate(startDateField.getText());
        resume.setEndDate(endDateField.getText());
        resume.setResponsibilities(responsibilitiesArea.getText());
        resume.setDegree(degreeField.getText());
        resume.setUniversity(universityField.getText());
        resume.setGraduationYear(graduationYearField.getText());

        ResumeDAO dao = new ResumeDAO();
        try {
            dao.saveResume(resume);
            JOptionPane.showMessageDialog(this, "Resume saved successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to save resume: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
