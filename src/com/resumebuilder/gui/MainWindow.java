package com.resumebuilder.gui;
import java.io.File;
import com.resumebuilder.dao.ResumeDAO;
import com.resumebuilder.model.Resume;
import com.resumebuilder.util.PdfGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MainWindow extends JFrame {
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTabbedPane tabbedPane;
    private JPanel previewPanel;

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);

    private Map<String, JTextField> textFields = new HashMap<>();
    private JTextArea summaryArea;

    public MainWindow() {
        setTitle("Professional Resume Builder");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set modern UI look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            customizeUIComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
        mainContainer.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainContainer.setBackground(BACKGROUND_COLOR);
        setContentPane(mainContainer);

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBorder(null);
        splitPane.setDividerSize(5);
        splitPane.setDividerLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.6));
        splitPane.setBackground(BACKGROUND_COLOR);
        mainContainer.add(splitPane, BorderLayout.CENTER);

        // Left panel (input forms)
        leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(BACKGROUND_COLOR);

        // Header for left panel
        JLabel headerLabel = new JLabel("Create Your Professional Resume");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setBorder(new EmptyBorder(0, 10, 10, 10));
        leftPanel.add(headerLabel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(TEXT_COLOR);
        leftPanel.add(tabbedPane, BorderLayout.CENTER);
        splitPane.setLeftComponent(leftPanel);

        // Right panel (resume preview)
        rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));

        JLabel previewLabel = new JLabel("Live Preview");
        previewLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        previewLabel.setForeground(PRIMARY_COLOR);
        previewLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        rightPanel.add(previewLabel, BorderLayout.NORTH);

        previewPanel = new JPanel();
        previewPanel.setBackground(Color.WHITE);
        rightPanel.add(previewPanel, BorderLayout.CENTER);
        splitPane.setRightComponent(rightPanel);

        setupMenuBar();
        addPersonalDetailsTab();
        addProfessionalSummaryTab();
    }

    private void customizeUIComponents() {
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", TEXT_COLOR);
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("TabbedPane.selected", SECONDARY_COLOR);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(PRIMARY_COLOR);
        menuBar.setBorder(null);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fileMenu.setForeground(Color.WHITE);
        menuBar.add(fileMenu);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton saveButton = createStyledButton("Save Resume");
        JButton downloadButton = createStyledButton("Download PDF");

        saveButton.addActionListener(e -> saveResume());
        downloadButton.addActionListener(e -> downloadPdf());

        buttonPanel.add(saveButton);
        buttonPanel.add(downloadButton);

        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(buttonPanel);

        setJMenuBar(menuBar);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });

        return button;
    }

    private void addPersonalDetailsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Job Title", "First Name", "Last Name", "Email", "Phone", "Country", "City"};
        int gridy = 0;

        for (String label : labels) {
            // Label
            gbc.gridx = 0;
            gbc.gridy = gridy;
            gbc.weightx = 0.2;
            JLabel jLabel = new JLabel(label);
            jLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            panel.add(jLabel, gbc);

            // Text field
            gbc.gridx = 1;
            gbc.weightx = 0.8;
            JTextField textField = new JTextField();
            textFields.put(label, textField);
            panel.add(textField, gbc);

            gridy++;
        }

        // Wrap in scroll pane for responsiveness
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        tabbedPane.addTab("Personal Details", scrollPane);
    }

    private void addProfessionalSummaryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel instruction = new JLabel("<html><div style='width: 300px; text-align: justify'>" +
                "Write 2-3 impactful sentences highlighting your professional expertise, " +
                "key achievements, and what makes you stand out in your field.</div></html>");
        instruction.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instruction.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(instruction, BorderLayout.NORTH);

        summaryArea = new JTextArea();
        summaryArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JScrollPane scrollPane = new JScrollPane(summaryArea);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Professional Summary", panel);
    }

    private void saveResume() {
        String jobTitle = textFields.get("Job Title").getText();
        String firstName = textFields.get("First Name").getText();
        String lastName = textFields.get("Last Name").getText();
        String email = textFields.get("Email").getText();
        String phone = textFields.get("Phone").getText();
        String country = textFields.get("Country").getText();
        String city = textFields.get("City").getText();
        String summary = summaryArea.getText();

        // Create Resume object
        Resume resume = new Resume(jobTitle, firstName, lastName, email, phone, country, city, summary);

        // Save to database
        String dbUrl = "jdbc:mysql://localhost:3306/student"; // Update with your database URL
        String username = "root"; // Update with your username
        String password = "anuj"; // Update with your password

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
            ResumeDAO resumeDAO = new ResumeDAO(connection);
            resumeDAO.saveResume(resume);
            JOptionPane.showMessageDialog(this, "Resume saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving resume: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void downloadPdf() {
        // Get resume details from the form
        String jobTitle = textFields.get("Job Title").getText();
        String firstName = textFields.get("First Name").getText();
        String lastName = textFields.get("Last Name").getText();
        String email = textFields.get("Email").getText();
        String phone = textFields.get("Phone").getText();
        String country = textFields.get("Country").getText();
        String city = textFields.get("City").getText();
        String summary = summaryArea.getText();

        // Create Resume object
        Resume resume = new Resume(jobTitle, firstName, lastName, email, phone, country, city, summary);

        // Specify the file path for the PDF
        String filePath = "C:\\FINAL APP\\resume.pdf"; // Update with a valid path

        // Create parent directories if they do not exist
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // This will create the necessary directories

        // Generate PDF
        PdfGenerator pdfGenerator = new PdfGenerator();
        try {
            pdfGenerator.generatePdf(resume, filePath);
            JOptionPane.showMessageDialog(this, "PDF generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}
