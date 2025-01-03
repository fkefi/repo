package com.despoina.sqlitehandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Test {
    static List<String> selectedDegrees = new ArrayList<>();
    static String selectedMajors, selectedDoctorate, selectedMasters;
    static List<Integer> weights;
    static String universityWeight, MSSkillsWeight, codingWeight, englishWeight, germanWeight, frenchWeight, spanishWeight, chineseWeight, workWeight;
    static List<String> masterDept, phDDept, selectedUniversities, degreeDept;
    public static void Criteria() {
        // Δημιουργία του κύριου frame
        JFrame frame = new JFrame("myCVision");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new BorderLayout());

        // Φόρτωση λογοτύπου
        try {
            ImageIcon logoIcon = new ImageIcon("E:\\myCVision\\mycv\\src\\resources\\logo.PNG");
            frame.setIconImage(logoIcon.getImage());
        } catch (Exception e) {
            System.err.println("Δεν βρέθηκε το λογότυπο: " + e.getMessage());
        }
        

        // Δημιουργία του κύριου panel με GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Προσθήκη ερωτήσεων
        JLabel question1 = new JLabel("Do you want Major's Degree");
        JLabel question2 = new JLabel("Do you want Master's Degree");
        JLabel question3 = new JLabel("Do you want Doctorate Degree");

        JComboBox<String> answer1 = new JComboBox<>(new String[]{"-", "Ναι", "Όχι"});
        JComboBox<String> answer2 = new JComboBox<>(new String[]{"-", "Ναι", "Όχι"});
        JComboBox<String> answer3 = new JComboBox<>(new String[]{"-","Ναι", "Όχι"});

        // Ερώτηση 1
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(question1, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        mainPanel.add(answer1, gbc);
        answer1.addActionListener(e -> {
            selectedMajors = (String) answer1.getSelectedItem();
        });

        // Ερώτηση 2
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(question2, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(answer2, gbc);
        answer2.addActionListener(e -> {
            selectedMasters = (String) answer2.getSelectedItem();
        });

        // Ερώτηση 3
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(question3, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        mainPanel.add(answer3, gbc);
        answer3.addActionListener(e -> {
            selectedDoctorate = (String) answer3.getSelectedItem();
        });



        //Weight
        JLabel weight1 = new JLabel("University Significance");
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(weight1, gbc);
        JTextField weightField1 = new JTextField();
        gbc.gridx = 1; 
        gbc.gridy = 3;
        gbc.gridwidth = 5; 
        mainPanel.add(weightField1, gbc);
        universityWeight = weightField1.getText();


        // Προσθήκη πολλαπλής επιλογής πανεπιστημίων
        String[] universities ={"National and Kapodistrian University of Athens", "Aristotle University of Thessaloniki", "University of Patras", "University of Ioannina", "University of Crete", "University of Thessaly", "University of Macedonia", "University of Piraeus", "Athens University of Economics and Business", "National Technical University of Athens", "Technical University of Crete", "University of the Aegean", "International Hellenic University", "University of Western Macedonia", "University of the Peloponnese", "University of Western Attica", "Hellenic Mediterranean University", "Harokopio University", "Athens School of Fine Arts", "Agricultural University of Athens", "Hellenic Open University", "Harvard University (USA)", "Stanford University (USA)", "Massachusetts Institute of Technology (MIT, USA)", "University of Cambridge (United Kingdom)", "University of Oxford (United Kingdom)", "California Institute of Technology (Caltech, USA)", "Princeton University (USA)", "University of Chicago (USA)", "Columbia University (USA)", "Yale University (USA)", "Imperial College London (United Kingdom)", "University of Pennsylvania (USA)", "ETH Zurich (Switzerland)", "University of Toronto (Canada)", "University of Melbourne (Australia)", "Peking University (China)", "Tsinghua University (China)", "National University of Singapore (Singapore)", "University of Tokyo (Japan)", "Ludwig Maximilian University of Munich (LMU, Germany)"};

        // Ετικέτα για τα πανεπιστήμια
        JLabel question4 = new JLabel("Select Universities (up to 10):");
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2; // Χρησιμοποιούμε 2 στήλες για καλύτερη εμφάνιση
        mainPanel.add(question4, gbc);

        // Panel για τα πανεπιστήμια με JScrollPane
        JPanel dropdownPanel = new JPanel();
        dropdownPanel.setLayout(new BoxLayout(dropdownPanel, BoxLayout.Y_AXIS));
        selectedUniversities = new ArrayList<>();

        for (String university : universities) {
            JCheckBox checkBox = new JCheckBox(university);
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    if (selectedUniversities.size() < 10) {
                        selectedUniversities.add(university);
                    } else {
                        checkBox.setSelected(false);
                        JOptionPane.showMessageDialog(frame, "Choose up to 10");
                    }
                } else {
                    selectedUniversities.remove(university);
                }
            });
            dropdownPanel.add(checkBox);
        }

        JScrollPane scrollPane = new JScrollPane(dropdownPanel);
        scrollPane.setPreferredSize(new Dimension(400, 200)); // Ρύθμιση μεγέθους για κύλιση
                // Προσθήκη του scrollPane στο mainPanel
                gbc.gridx = 0; gbc.gridy = 5;
                gbc.gridwidth = 2; // Πλάτος 2 στηλών
                mainPanel.add(scrollPane, gbc);

// Panel για τα τμήματα με JScrollPane
JLabel question5 = new JLabel("Select Majors Department (up to 5):");
gbc.gridx = 0;
gbc.gridy = 6;
gbc.gridwidth = 2; // Χρησιμοποιούμε 2 στήλες για καλύτερη εμφάνιση
mainPanel.add(question5, gbc);

String[] lessons = {
    "Medicine", "Law", "Philosophy", "Mathematics", "Computer Science and Engineering",
    "Physics", "Civil Engineering", "Mechanical Engineering", "Chemistry", "Biology",
    "Business Administration", "Communication and Media Studies", "Marketing",
    "Informatics", "Accounting and Finance", "Electrical Engineering", "Architecture",
    "Agriculture", "History", "Political Science", "Tourism Studies", "Naval Studies",
    "Food Technology", "Nutrition and Dietetics", "Painting", "Sculpture",
    "Management Science and Technology"
};

        JPanel dropdownPane2 = new JPanel();
        dropdownPane2.setLayout(new BoxLayout(dropdownPane2, BoxLayout.Y_AXIS));
        degreeDept = new ArrayList<>();

        for (String lesson : lessons) {
            JCheckBox checkBox = new JCheckBox(lesson);
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    if (degreeDept.size() < 5) {
                        degreeDept.add(lesson);
                    } else {
                        checkBox.setSelected(false);
                        JOptionPane.showMessageDialog(frame, "Choose up to 5");
                    }
                } else {
                    degreeDept.remove(lesson);
                }
            });
            dropdownPane2.add(checkBox); // Προσθήκη στο σωστό panel
        }

        JScrollPane scrollPane2 = new JScrollPane(dropdownPane2);
        scrollPane2.setPreferredSize(new Dimension(400, 200)); // Ρύθμιση σωστού μεγέθους για scrollPane2
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2; // Πλάτος 2 στηλών
        mainPanel.add(scrollPane2, gbc);

        JLabel question6 = new JLabel("Select Masters Department (up to 7):");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2; // Χρησιμοποιούμε 2 στήλες για καλύτερη εμφάνιση
        mainPanel.add(question6, gbc);


        JPanel dropdownPane3 = new JPanel();
        dropdownPane3.setLayout(new BoxLayout(dropdownPane3, BoxLayout.Y_AXIS));
        masterDept = new ArrayList<>();

        for (String lesson : lessons) {
            JCheckBox checkBox = new JCheckBox(lesson);
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    if (masterDept.size() < 7) {
                        masterDept.add(lesson);
                    } else {
                        checkBox.setSelected(false);
                        JOptionPane.showMessageDialog(frame, "Choose up to 7");
                    }
                } else {
                    masterDept.remove(lesson);
                }
            });
            dropdownPane3.add(checkBox); 
        }

        JScrollPane scrollPane3 = new JScrollPane(dropdownPane3);
        scrollPane3.setPreferredSize(new Dimension(400, 200)); 
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2; 
        mainPanel.add(scrollPane3, gbc);

        JLabel question7 = new JLabel("Select Doctorate Department (up to 7):");
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2; 
        mainPanel.add(question7, gbc);


        JPanel dropdownPane4 = new JPanel();
        dropdownPane4.setLayout(new BoxLayout(dropdownPane4, BoxLayout.Y_AXIS));
        phDDept = new ArrayList<>();

        for (String lesson : lessons) {
            JCheckBox checkBox = new JCheckBox(lesson);
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    if (phDDept.size() < 7) {
                        phDDept.add(lesson);
                    } else {
                        checkBox.setSelected(false);
                        JOptionPane.showMessageDialog(frame, "Choose up to 7");
                    }
                } else {
                    phDDept.remove(lesson);
                }
            });
            dropdownPane4.add(checkBox); 
        }

        JScrollPane scrollPane4 = new JScrollPane(dropdownPane4);
        scrollPane4.setPreferredSize(new Dimension(400, 200)); 
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2; 
        mainPanel.add(scrollPane4, gbc);

        //Ξενες Γλωσσες
        JLabel weight2 = new JLabel("English Knowledge Significance");
        gbc.gridx = 0; gbc.gridy = 12;
        mainPanel.add(weight2, gbc);
        JTextField weightField2 = new JTextField();
        gbc.gridx = 1; 
        gbc.gridy = 12;
        gbc.gridwidth = 5; 
        mainPanel.add(weightField2, gbc);
        englishWeight = weightField2.getText();

        JLabel weight3 = new JLabel("German Knowledge Significance");
        gbc.gridx = 0; gbc.gridy = 13;
        mainPanel.add(weight3, gbc);
        JTextField weightField3 = new JTextField();
        gbc.gridx = 1; 
        gbc.gridy = 13;
        gbc.gridwidth = 5; 
        mainPanel.add(weightField3, gbc);
        germanWeight = weightField3.getText();

        JLabel weight4 = new JLabel("French Knowledge Significance");
        gbc.gridx = 0; gbc.gridy = 14;
        mainPanel.add(weight4, gbc);
        JTextField weightField4 = new JTextField();
        gbc.gridx = 1; 
        gbc.gridy = 14;
        gbc.gridwidth = 5; 
        mainPanel.add(weightField4, gbc);
        frenchWeight = weightField4.getText();

        JLabel weight5 = new JLabel("Spanish Knowledge Significance");
        gbc.gridx = 0; gbc.gridy = 15;
        mainPanel.add(weight5, gbc);
        JTextField weightField5 = new JTextField();
        gbc.gridx = 1; 
        gbc.gridy = 15;
        gbc.gridwidth = 5; 
        mainPanel.add(weightField5, gbc);
        spanishWeight = weightField5.getText();
       
        JLabel weight6 = new JLabel("Chinese Knowledge Significance");
        gbc.gridx = 0; gbc.gridy = 16;
        mainPanel.add(weight6, gbc);
        JTextField weightField6 = new JTextField();
        gbc.gridx = 1; 
        gbc.gridy = 16;
        gbc.gridwidth = 5; 
        mainPanel.add(weightField6, gbc);
        chineseWeight = weightField6.getText();

        JLabel weight7 = new JLabel("Work Experience Significance");
        gbc.gridx = 0; gbc.gridy = 17;
        mainPanel.add(weight7, gbc);
        JTextField weightField7 = new JTextField();
        gbc.gridx = 1; 
        gbc.gridy = 17;
        gbc.gridwidth = 5; 
        mainPanel.add(weightField7, gbc);
        workWeight = weightField7.getText();
       
        JLabel question11 = new JLabel("Related Work Experience");

        JComboBox<String> answer11 = new JComboBox<>(new String[]{"1 year", "2 year", "3 year", "4 year", "5 year", "6+ year"});
        gbc.gridx = 0; gbc.gridy = 18;
        mainPanel.add(question11, gbc);
        gbc.gridx = 1; gbc.gridy = 18;
        mainPanel.add(answer11, gbc);
        answer11.addActionListener(e -> {
            String selectedRelatedWorkExperience = (String) answer11.getSelectedItem();
        });

        //Weight
        JLabel weight8 = new JLabel("MS Office Skills Significance");
        gbc.gridx = 0; gbc.gridy = 19;
        mainPanel.add(weight8, gbc);
        JTextField weightField8 = new JTextField();
        gbc.gridx = 1; 
        gbc.gridy = 19;
        gbc.gridwidth = 5; 
        mainPanel.add(weightField8, gbc);
        MSSkillsWeight = weightField8.getText();

        String[] codingl ={"Java", "Java Script", "C", "C++","JavaScript", "HTML", "CSS", "Assembly", "PowerShell", "Matlab", "SQL", "AutoCad", "SolidWorks", "PhotoShop", "Premiere Pro", "Illustrator", "SPSS", "Stata", "Docker", "Google Workspace", "Softone", "Epsilon", "Atlantis"};

        // Ετικέτα για γλωσσες προγραμματισμου
        JLabel question13 = new JLabel("Select Coding Languages and other programms (up to 10):");
        gbc.gridx = 0; gbc.gridy = 21;
        gbc.gridwidth = 2; // Χρησιμοποιούμε 2 στήλες για καλύτερη εμφάνιση
        mainPanel.add(question13, gbc);

        // Panel για γλωσσες προγραμματισμου με JScrollPane
        JPanel dropdownPane5 = new JPanel();
        dropdownPane5.setLayout(new BoxLayout(dropdownPane5, BoxLayout.Y_AXIS));
        List<String> selectedCodingl = new ArrayList<>();

        for (String language : codingl) {
            JCheckBox checkBox = new JCheckBox(language);
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    if (selectedCodingl.size() < 10) {
                        selectedCodingl.add(language);
                    } else {
                        checkBox.setSelected(false);
                        JOptionPane.showMessageDialog(frame, "Choose up to 10");
                    }
                } else {
                    selectedCodingl.remove(language);
                }
            });
            dropdownPane5.add(checkBox);
        }

        JScrollPane scrollPane5 = new JScrollPane(dropdownPane5);
        scrollPane5.setPreferredSize(new Dimension(400, 200)); // Ρύθμιση μεγέθους για κύλιση
                // Προσθήκη του scrollPane στο mainPanel
                gbc.gridx = 0; gbc.gridy = 22;
                gbc.gridwidth = 2; // Πλάτος 2 στηλών
                mainPanel.add(scrollPane5, gbc);

        //Weight
        JLabel weight9 = new JLabel("Coding Skills Significance");
        gbc.gridx = 0; gbc.gridy = 20;
        mainPanel.add(weight9, gbc);
        JTextField weightField9 = new JTextField();
        gbc.gridx = 1; 
        gbc.gridy = 20;
        gbc.gridwidth = 5; 
        mainPanel.add(weightField9, gbc);
        codingWeight = weightField9.getText();
        
        weights = new ArrayList<>();
        // Ελέγξτε αν το πεδίο είναι κενό πριν το μετατρέψετε σε int
        // Αρχικοποιήστε τη λίστα πριν την χρησιμοποιήσετε
        
        // Κουμπί Υποβολής
        // Finalizing the submit button setup
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                if ((Integer.parseInt(weightField1.getText().trim()) +
                    Integer.parseInt(weightField2.getText().trim()) +
                    Integer.parseInt(weightField3.getText().trim()) +
                    Integer.parseInt(weightField4.getText().trim()) +
                    Integer.parseInt(weightField5.getText().trim()) +
                    Integer.parseInt(weightField6.getText().trim()) +
                    Integer.parseInt(weightField7.getText().trim()) +
                    Integer.parseInt(weightField8.getText().trim()) +
                    Integer.parseInt(weightField9.getText().trim())) == 100) {
                    weights.clear(); // Καθαρίζουμε τυχόν προηγούμενες τιμές
                    weights.add(Integer.parseInt(weightField1.getText().trim())); // University Weight
                    weights.add(Integer.parseInt(weightField2.getText().trim())); // English Weight
                    weights.add(Integer.parseInt(weightField3.getText().trim())); // German Weight
                    weights.add(Integer.parseInt(weightField4.getText().trim())); // French Weight
                    weights.add(Integer.parseInt(weightField5.getText().trim())); // Spanish Weight
                    weights.add(Integer.parseInt(weightField6.getText().trim())); // Chinese Weight
                    weights.add(Integer.parseInt(weightField7.getText().trim())); // Work Experience Weight
                    weights.add(Integer.parseInt(weightField8.getText().trim())); // MS Office Weight
                    weights.add(Integer.parseInt(weightField9.getText().trim())); // Coding Skills Weight
                    selectedDegrees.add(selectedMajors);
                    selectedDegrees.add(selectedMasters);
                    selectedDegrees.add(selectedDoctorate);
                    System.out.println(selectedUniversities);
                    System.out.println(weights);
                    System.out.println(masterDept);
                    System.out.println(phDDept);
                    System.out.println(degreeDept);
                    System.out.println(selectedDegrees);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Παρακαλώ εισάγετε έγκυρους αριθμούς σε όλα τα πεδία βαρύτητας.");
                return;
            }
            frame.dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 100;
        gbc.gridwidth = 2;
        mainPanel.add(submitButton, gbc);

        // Προσθήκη κύριου panel στο frame
        frame.add(new JScrollPane(mainPanel), BorderLayout.CENTER);

        // Εμφάνιση του frame
        frame.setVisible(true);


    }
    public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
            Criteria();
        });
    }
} 