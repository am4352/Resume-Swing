package com.resumebuilder.gui;
import com.resumebuilder.model.Resume;
import com.resumebuilder.dao.ResumeDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MainWindow extends JFrame {
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTabbedPane tabbedPane;
    private JPanel previewPanel;

    // Define color scheme
    private static final Color BACKGROUND_COLOR = new Color(255, 150, 150); // Light red background
    private static final Color ACCENT_COLOR = new Color(220, 20, 60); // Crimson red
    private static final Color TEXT_COLOR = new Color(50, 50, 50); // Dark gray text
    private static final Color INPUT_BACKGROUND = new Color(180, 80, 80); // Darker red for input fields

    private Map<String, JTextField> textFields = new HashMap<>();
    private JTextArea summaryArea; 
  
    public MainWindow() {
        setTitle("Resume Builder");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create main split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBorder(null);
        splitPane.setDividerSize(1);
        splitPane.setDividerLocation(600);
        splitPane.setBackground(BACKGROUND_COLOR);
        add(splitPane, BorderLayout.CENTER);

        // Left panel (input forms)
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(BACKGROUND_COLOR);
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(TEXT_COLOR);
        leftPanel.add(tabbedPane, BorderLayout.CENTER);
        splitPane.setLeftComponent(leftPanel);

        // Right panel (resume preview)
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        previewPanel = new JPanel();
        previewPanel.setBackground(Color.WHITE);
        previewPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        rightPanel.add(previewPanel, BorderLayout.CENTER);
        splitPane.setRightComponent(rightPanel);

        // Set up top menu bar
        setupMenuBar();

        // Add resume score panel
        addResumeScorePanel();

        // Add tabs and their content
        addPersonalDetailsTab();
        addProfessionalSummaryTab();
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Arial", Font.PLAIN, 14));
        fileMenu.setForeground(TEXT_COLOR);
        menuBar.add(fileMenu);

        JButton templateButton = new JButton("Select template");
        styleButton(templateButton);

        JButton downloadButton = new JButton("Download PDF");
        styleButton(downloadButton);
        downloadButton.setBackground(ACCENT_COLOR);
        downloadButton.setForeground(Color.WHITE);

        JButton saveButton = new JButton("Save Resume");
        styleButton(saveButton);
        saveButton.addActionListener(e -> saveResume());

        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(templateButton);
        menuBar.add(Box.createHorizontalStrut(10));
        menuBar.add(downloadButton);
        menuBar.add(Box.createHorizontalStrut(10));
        menuBar.add(saveButton);

        setJMenuBar(menuBar);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(TEXT_COLOR);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        button.setFocusPainted(false);
    }

    private void addResumeScorePanel() {
        JPanel scorePanel = new JPanel(new BorderLayout());
        scorePanel.setBackground(BACKGROUND_COLOR);
        scorePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel scoreLabel = new JLabel("Your resume score");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.WHITE);
        scorePanel.add(scoreLabel, BorderLayout.NORTH);

        JProgressBar scoreBar = new JProgressBar(0, 100);
        scoreBar.setValue(44);
        scoreBar.setStringPainted(true);
        scoreBar.setForeground(ACCENT_COLOR);
        scoreBar.setBackground(Color.WHITE);
        scorePanel.add(scoreBar, BorderLayout.CENTER);

        JButton addEmploymentButton = new JButton("+ Add employment history");
        addEmploymentButton.setBackground(Color.WHITE);
        addEmploymentButton.setForeground(ACCENT_COLOR);
        addEmploymentButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        scorePanel.add(addEmploymentButton, BorderLayout.SOUTH);

        leftPanel.add(scorePanel, BorderLayout.NORTH);
    }

    private void addPersonalDetailsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] labels = {"Job Title", "First Name", "Last Name", "Email", "Phone", "Country", "City"};
        int gridy = 0;

        for (String label : labels) {
            gbc.gridx = 0;
            gbc.gridy = gridy;
            JLabel jLabel = new JLabel(label);
            jLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            jLabel.setForeground(Color.WHITE);
            panel.add(jLabel, gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            JTextField textField = new JTextField(20);
            textField.setFont(new Font("Arial", Font.PLAIN, 14));
            textField.setBackground(INPUT_BACKGROUND);
            textField.setForeground(Color.WHITE);
            textField.setBorder(BorderFactory.createLineBorder(INPUT_BACKGROUND));
            panel.add(textField, gbc);

            textFields.put(label, textField);

            gridy++;
        }

        tabbedPane.addTab("Personal Details", new JScrollPane(panel));
    }

    private void addProfessionalSummaryTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        summaryArea = new JTextArea(10, 30);
        summaryArea.setFont(new Font("Arial", Font.PLAIN, 14));
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setBackground(INPUT_BACKGROUND);
        summaryArea.setForeground(Color.WHITE);
        summaryArea.setBorder(BorderFactory.createLineBorder(INPUT_BACKGROUND));
        panel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);

        JLabel instruction = new JLabel("<html><font color='white'>Write 2-3 short, energetic sentences about how great you are. Mention the role and what you did. What were the big achievements? Describe your motivation and list your skills.</font></html>");
        instruction.setFont(new Font("Arial", Font.PLAIN, 14));
        instruction.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(instruction, BorderLayout.NORTH);

        tabbedPane.addTab("Professional Summary", panel);
    }

    private void saveResume() {
        Resume resume = new Resume();
        resume.setJobTitle(textFields.get("Job Title").getText());
        resume.setFirstName(textFields.get("First Name").getText());
        resume.setLastName(textFields.get("Last Name").getText());
        resume.setEmail(textFields.get("Email").getText());
        resume.setPhone(textFields.get("Phone").getText());
        resume.setCountry(textFields.get("Country").getText());
        resume.setCity(textFields.get("City").getText());
        resume.setProfessionalSummary(summaryArea.getText());

        ResumeDAO dao = new ResumeDAO();
        try {
            dao.saveResume(resume);
            JOptionPane.showMessageDialog(this, "Resume saved successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving resume: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}