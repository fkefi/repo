package com.cybersolvers.mycvision;

import java.net.URL;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.sql.SQLException;
import java.io.File;
import java.net.URL;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class CustomCriteriaApp {
    // Constants
    private static final int MAX_UNIVERSITIES = 10;
    private static final String[] DEGREE_OPTIONS = {"-", "Yes", "No"};
    private static final String[] WORK_EXPERIENCE_OPTIONS = {
        "1 year", "2 years", "3 years", "4 years", "5 years", "6+ years"
    };
    private static final String[] WEIGHT_CRITERIA = {
        "undergraduateUniversity",
        "undergraduateDepartment",
        "undergraduateGrade",
        "masterUniversity",
        "masterDepartment",
        "masterGrade",
        "phdUniversity", 
        "phdDepartment",
        "phdGrade",
        "englishLevel",
        "frenchLevel",
        "germanLevel",
        "spanishLevel",
        "chineseLevel",
        "otherLanguageLevel",
        "workExperienceYears",
        "officeSkills",
        "programmingLanguage"
    };

    URL resource = ResumeService.class.getClassLoader().getResource("my_database.db");
    String dbPath = resource.getPath();
    String dbUrl = "jdbc:sqlite:" + dbPath;
    private final SQLiteHandler handler;

    // Data structures
    private static Map<String, JTextField> weightFields = new HashMap<>();
    private static Map<String, List<String>> selections = new HashMap<>();
    private static Map<String, String> degreeSelections = new HashMap<>();

    // UI Components
    private static JFrame mainFrame;
    private static JPanel mainPanel;

    // Constructor
    public CustomCriteriaApp() throws SQLException {
        handler = new SQLiteHandler(dbUrl);
    }

    public JFrame createAndShowGUI() {
        initializeFrame();
        initializeMainPanel();
        addComponents();
        finalizeFrame();
        return mainFrame;
    }

    private static void initializeFrame() {
        mainFrame = new JFrame("myCVision - CV Evaluation System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(900, 800);
        mainFrame.setLayout(new BorderLayout());
        loadLogo();
    }

    private static void loadLogo() {
        try {
            ImageIcon logoIcon = new ImageIcon("src/resources/logo.PNG");
            mainFrame.setIconImage(logoIcon.getImage());
        } catch (Exception e) {
            System.err.println("Logo not found: " + e.getMessage());
        }
    }

    private static void initializeMainPanel() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void addComponents() {
        GridBagConstraints gbc = createGridBagConstraints();
        
        // Add degree selection section
        addDegreeSelections(gbc);
        
        // Add university sections
        addUniversitySection(gbc, "Bachelor", MAX_UNIVERSITIES);
        addUniversitySection(gbc, "Master", MAX_UNIVERSITIES);
        addUniversitySection(gbc, "PhD", MAX_UNIVERSITIES);
        
        // Add skills sections
        addLanguageSection(gbc);
        addWorkExperienceSection(gbc);
        addTechnicalSkillsSection(gbc);
        
        // Add submit button
        addSubmitButton(gbc);
    }

    private static GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        return gbc;
    }

    private static void addDegreeSelections(GridBagConstraints gbc) {
        String[] degreeTypes = {"Bachelor", "Master", "PhD"};
        for (String degreeType : degreeTypes) {
            JLabel label = new JLabel("Do you want " + degreeType + "'s Degree?");
            JComboBox<String> comboBox = new JComboBox<>(DEGREE_OPTIONS);
            
            mainPanel.add(label, gbc);
            gbc.gridx = 1;
            mainPanel.add(comboBox, gbc);
            gbc.gridx = 0;
            gbc.gridy++;

            final String degreeKey = degreeType.toLowerCase();
            comboBox.addActionListener(e -> degreeSelections.put(degreeKey, (String)comboBox.getSelectedItem()));
        }
    }

    private static void addUniversitySection(GridBagConstraints gbc, String level, int maxSelections) {
        JLabel sectionLabel = new JLabel(level + " Education Section");
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridwidth = 2;
        mainPanel.add(sectionLabel, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;

        String prefix = level.equals("Bachelor") ? "undergraduate" : level.toLowerCase();
        
        // Add university weight field
        String universityKey = prefix + "University";
        addWeightField(gbc, universityKey, level + " University Weight");
        
        // Add department weight field
        String departmentKey = prefix + "Department";
        addWeightField(gbc, departmentKey, level + " Department Weight");
        
        // Add grade weight field
        String gradeKey = prefix + "Grade";
        addWeightField(gbc, gradeKey, level + " Grade Weight");
            
        // Add university selection
        if (level.equals("Bachelor")) {
            addMultipleSelection(gbc, 
                level + " Universities", 
                getUniversityList(), 
                level.toLowerCase() + "_universities",
                maxSelections);
        }
        // Add department selection
        addMultipleSelection(gbc,
            level + " Departments",
            getDepartmentList(),
            level.toLowerCase() + "_departments",
            maxSelections);
    }

    private static void addLanguageSection(GridBagConstraints gbc) {
        String[] languages = {"English", "French", "German", "Spanish", "Chinese", "Other"};
        
        JLabel sectionLabel = new JLabel("Language Skills");
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridwidth = 2;
        mainPanel.add(sectionLabel, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
    
        for (String language : languages) {
            String key;
            if (language.equals("Other")) {
                key = "otherLanguageLevel";
            } else {
                key = language.toLowerCase() + "Level";
            }
            addWeightField(gbc, key, language + " Language Weight");
        }
    }

    private static void addWorkExperienceSection(GridBagConstraints gbc) {
        // Add work experience weight field
        addWeightField(gbc, "workExperienceYears", "Work Experience Weight");
        
        JLabel workExpLabel = new JLabel("Years of Related Work Experience:");
        JComboBox<String> workExpCombo = new JComboBox<>(WORK_EXPERIENCE_OPTIONS);
        
        mainPanel.add(workExpLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(workExpCombo, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    private static void addTechnicalSkillsSection(GridBagConstraints gbc) {
        // Add weight fields for both office and programming skills
        addWeightField(gbc, "officeSkills", "Office Skills Weight");
        addWeightField(gbc, "programmingLanguage", "Programming Skills Weight");
        
        addMultipleSelection(gbc,
            "Technical Skills",
            getTechnicalSkillsList(),
            "technical_skills",
            10);
    }

    private static void addWeightField(GridBagConstraints gbc, String key, String labelText) {
        JLabel label = new JLabel(labelText + ":");
        JTextField field = new JTextField(5);
        weightFields.put(key, field);
        
        mainPanel.add(label, gbc);
        gbc.gridx = 1;
        mainPanel.add(field, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    private static void addMultipleSelection(GridBagConstraints gbc, String title, 
            String[] items, String key, int maxSelections) {
        JLabel label = new JLabel("Select " + title + " (up to " + maxSelections + "):");
        mainPanel.add(label, gbc);
        gbc.gridy++;

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        List<String> selectedItems = new ArrayList<>();
        selections.put(key, selectedItems);

        for (String item : items) {
            JCheckBox checkBox = new JCheckBox(item);
            checkBox.addActionListener(createCheckBoxListener(item, selectedItems, maxSelections));
            checkBoxPanel.add(checkBox);
        }

        JScrollPane scrollPane = new JScrollPane(checkBoxPanel);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        gbc.gridwidth = 2;
        mainPanel.add(scrollPane, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;
    }

    private static ActionListener createCheckBoxListener(String item, List<String> selectedItems, int maxSelections) {
        return e -> {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            if (checkBox.isSelected()) {
                if (selectedItems.size() < maxSelections) {
                    selectedItems.add(item);
                } else {
                    checkBox.setSelected(false);
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Maximum " + maxSelections + " selections allowed");
                }
            } else {
                selectedItems.remove(item);
            }
        };
    }

    private void addSubmitButton(GridBagConstraints gbc) {
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> handleSubmission());
        
        gbc.gridwidth = 2;
        gbc.gridy++;
        mainPanel.add(submitButton, gbc);
    }

    private void handleSubmission() {
        if (validateWeights()) {
            try {
                // Save weights as double array
                double[] weights = new double[WEIGHT_CRITERIA.length];
                for (int i = 0; i < WEIGHT_CRITERIA.length; i++) {
                    String weightText = weightFields.get(WEIGHT_CRITERIA[i]).getText().trim();
                    weights[i] = Double.parseDouble(weightText);
                }
                handler.insertArray("weights", weights, weights.length);
                
                // Save bachelor universities as string array
                List<String> bachelorUniList = selections.getOrDefault("bachelor_universities", new ArrayList<>());
                String[] bachelorUniversities = bachelorUniList.toArray(new String[0]);
                handler.insertArray("bachelorUniversities", bachelorUniversities, bachelorUniversities.length);
                
                // Save work experience as string array
                List<String> workExpList = selections.getOrDefault("work_experience", new ArrayList<>());
                String[] workExperience = workExpList.toArray(new String[0]);
                handler.insertArray("workExperience", workExperience, workExperience.length);
                
                // Save department selections as string arrays
                // Bachelor departments
                List<String> bachelorDeptList = selections.getOrDefault("bachelor_departments", new ArrayList<>());
                String[] bachelorDept = bachelorDeptList.toArray(new String[0]);
                handler.insertArray("bachelorDepartments", bachelorDept, bachelorDept.length);
                
                // Master departments
                List<String> masterDeptList = selections.getOrDefault("master_departments", new ArrayList<>());
                String[] masterDept = masterDeptList.toArray(new String[0]);
                handler.insertArray("masterDepartments", masterDept, masterDept.length);
                
                // PhD departments
                List<String> phdDeptList = selections.getOrDefault("phd_departments", new ArrayList<>());
                String[] phdDept = phdDeptList.toArray(new String[0]);
                handler.insertArray("phdDepartments", phdDept, phdDept.length);
                
                Map<String, Object> degrees = new LinkedHashMap<>();
                for (Map.Entry<String, String> entry : degreeSelections.entrySet()) {
                    degrees.put(entry.getKey(), entry.getValue() != null ? entry.getValue() : "-");
                }
                handler.insertJsonAsMap("degrees", degrees);
                
                displayAllSavedData();
                mainFrame.dispose();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Error saving to database: " + e.getMessage());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainFrame,
                    "Error converting weights to numbers: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, 
                "Please ensure all weights are valid numbers and sum to 100");
        }
    }
    

private static boolean validateWeights() {
    try {
        int sum = 0;
        for (String criteria : WEIGHT_CRITERIA) {
            JTextField field = weightFields.get(criteria);
            if (field == null) {
                System.err.println("Missing weight field for criteria: " + criteria);
                continue;
            }
            sum += Integer.parseInt(field.getText().trim());
        }
        return sum == 100;
    } catch (NumberFormatException e) {
        return false;
    }
}
private static void saveSelections(Map<String, Integer> weights) {
    // Store weights and selections for processing
    System.out.println("Weights saved: " + weights);
    System.out.println("Selections saved: " + selections);
    System.out.println("Degree selections saved: " + degreeSelections);
}

    private static void finalizeFrame() {
        mainFrame.add(new JScrollPane(mainPanel));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    // Helper methods to provide data
    private static String[] getUniversityList() {
        return new String[]{"National and Kapodistrian University of Athens", "Aristotle University of Thessaloniki", "University of Patras", "University of Ioannina",
        "University of Crete", "University of Thessaly", "University of Macedonia", "University of Piraeus", "Athens University of Economics and Business",
        "National Technical University of Athens", "Technical University of Crete", "University of the Aegean", "International Hellenic University",
        "University of Western Macedonia", "University of the Peloponnese", "University of Western Attica", "Hellenic Mediterranean University", "Harokopio University",
        "Athens School of Fine Arts", "Agricultural University of Athens", "Hellenic Open University", "Harvard University (USA)", "Stanford University (USA)",
        "Massachusetts Institute of Technology (MIT, USA)", "University of Cambridge (United Kingdom)", "University of Oxford (United Kingdom)",
        "California Institute of Technology (Caltech, USA)", "Princeton University (USA)", "University of Chicago (USA)", "Columbia University (USA)",
        "Yale University (USA)", "Imperial College London (United Kingdom)", "University of Pennsylvania (USA)", "ETH Zurich (Switzerland)",
        "University of Toronto (Canada)", "University of Melbourne (Australia)", "Peking University (China)", "Tsinghua University (China)",
        "National University of Singapore (Singapore)", "University of Tokyo (Japan)", "Ludwig Maximilian University of Munich (LMU, Germany)"};
    }

    private static String[] getDepartmentList() {
        return new String[]{
            "Medicine", "Law", "Philosophy", "Mathematics", "Computer Science and Engineering",
            "Physics", "Civil Engineering", "Mechanical Engineering", "Chemistry", "Biology",
            "Business Administration", "Communication and Media Studies", "Marketing",
            "Informatics", "Accounting and Finance", "Electrical Engineering", "Architecture",
            "Agriculture", "History", "Political Science", "Tourism Studies", "Naval Studies",
            "Food Technology", "Nutrition and Dietetics", "Painting", "Sculpture",
            "Management Science and Technology"
        };
    }

    private static String[] getTechnicalSkillsList() {
        return new String[]{"Java", "Java Script", "C", "C++","JavaScript", "HTML", "CSS",
        "Assembly","PowerShell", "Matlab", "SQL", "AutoCad", "SolidWorks", "PhotoShop", 
        "Premiere Pro", "Illustrator", "SPSS", "Stata", "Docker", "Google Workspace","Softone", "Epsilon", "Atlantis"};
    }

    private void displayAllSavedData() {
        try {
            // Display weights
            Map<String, Object> savedWeights = handler.fetchJsonAsMap("weight");
            System.out.println("\nSaved weights:");
            for (String criterion : WEIGHT_CRITERIA) {
                System.out.println(criterion + ": " + savedWeights.get(criterion));
            }

            // Display universities
            System.out.println("\nBachelor Universities:");
            System.out.println(handler.fetchJsonAsMap("bachelorUni").get("universities"));
            
            System.out.println("\nMaster Universities:");
            System.out.println(handler.fetchJsonAsMap("masterUni").get("universities"));
            
            System.out.println("\nPhD Universities:");
            System.out.println(handler.fetchJsonAsMap("phdUni").get("universities"));

            // Display departments
            System.out.println("\nBachelor Departments:");
            System.out.println(handler.fetchJsonAsMap("bachelorDept").get("departments"));
            
            System.out.println("\nMaster Departments:");
            System.out.println(handler.fetchJsonAsMap("masterDept").get("departments"));
            
            System.out.println("\nPhD Departments:");
            System.out.println(handler.fetchJsonAsMap("phdDept").get("departments"));

            // Display degree requirements
            System.out.println("\nDegree Requirements:");
            Map<String, Object> degrees = handler.fetchJsonAsMap("degrees");
            for (Map.Entry<String, Object> entry : degrees.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            // Display work experience
            System.out.println("\nWork Experience:");
            System.out.println(handler.fetchJsonAsMap("workExperience").get("experience"));

        } catch (SQLException e) {
            System.err.println("Error fetching data: " + e.getMessage());
        }
    }
}