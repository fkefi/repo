package com.cybersolvers.mycvision;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class CVSubmissionApp2 {

    public static Path cvFolder;
    
    public static void startCVSubmissionApp() {
        // Δημιουργία του κύριου frame
        JFrame frame = new JFrame("Κατάθεση Βιογραφικών");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Δημιουργία custom panel με background εικόνα
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            private Image backgroundImage = new ImageIcon("E:\\myCVision\\mycv\\src\\resources\\logo.PNG").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                g2d.dispose();
            }
        };
        mainPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Προσθήκη τίτλου
        JLabel titleLabel = new JLabel("Κατάθεση Βιογραφικών", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Προσθήκη οδηγίας
        JLabel instructionLabel = new JLabel("Εισάγετε τα βιογραφικά των υποψηφίων:", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        mainPanel.add(instructionLabel, gbc);

        // Λίστα για την εμφάνιση των ονομάτων των αρχείων που εισάγονται
        DefaultListModel<String> cvListModel = new DefaultListModel<>();
        JList<String> cvList = new JList<>(cvListModel);
        JScrollPane cvListScrollPane = new JScrollPane(cvList);
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        mainPanel.add(cvListScrollPane, gbc);

        // Ενεργοποίηση Drag and Drop στη λίστα
        new DropTarget(cvList, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {}

            @Override
            public void dragOver(DropTargetDragEvent dtde) {}

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {}

            @Override
            public void dragExit(DropTargetEvent dte) {}

            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);
                        List<File> droppedFiles = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        for (File file : droppedFiles) {
                            cvListModel.addElement(file.getAbsolutePath());
                        }
                        dtde.dropComplete(true);
                    } else {
                        dtde.rejectDrop();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Κουμπί Προσθήκης Βιογραφικών
        JButton addCVButton = new JButton("Προσθήκη Βιογραφικού");
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(addCVButton, gbc);

        addCVButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                for (File file : fileChooser.getSelectedFiles()) {
                    cvListModel.addElement(file.getAbsolutePath());
                }
            }
        });

        // Κουμπί Τέλος
        JButton finishButton = new JButton("Τέλος");
        gbc.gridx = 1;
        mainPanel.add(finishButton, gbc);

        finishButton.addActionListener(e -> {
            if (cvListModel.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Δεν έχετε εισάγει βιογραφικά.", "Προσοχή", JOptionPane.WARNING_MESSAGE);
            } else {
                // Αποθήκευση βιογραφικών στον φάκελο
                saveCVsToFolder(cvListModel);
                JOptionPane.showMessageDialog(frame, "Τα βιογραφικά κατατέθηκαν επιτυχώς!", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
                cvListModel.clear();
            }
        });

        frame.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void saveCVsToFolder(DefaultListModel<String> cvListModel) {
        String userDesktop = System.getProperty("user.home") + File.separator + "Desktop";
        Path cvFolder = Paths.get(userDesktop, "CV");

        try {
            // Έλεγχος αν υπάρχει ο φάκελος Desktop
            if (!Files.exists(cvFolder.getParent())) {
                throw new IOException("Δεν βρέθηκε η επιφάνεια εργασίας.");
            }

            // Δημιουργία του φακέλου CV
            Files.createDirectories(cvFolder);

            // Επιβεβαίωση δημιουργίας φακέλου
            if (!Files.isDirectory(cvFolder)) {
                throw new IOException("Αποτυχία δημιουργίας του φακέλου CV.");
            }

            // Εκτύπωση του μονοπατιού
            System.out.println("Αποθήκευση αρχείων στον φάκελο: " + cvFolder);

            // Αντιγραφή αρχείων
            for (int i = 0; i < cvListModel.getSize(); i++) {
                Path source = Paths.get(cvListModel.getElementAt(i));
                Path target = cvFolder.resolve(source.getFileName());
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Αντιγραφή: " + source + " -> " + target);
            }

            System.out.println("Τα βιογραφικά αποθηκεύτηκαν στον φάκελο: " + cvFolder);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την αποθήκευση των αρχείων: " + e.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Path getCVPath() {
        return cvFolder;
    }
}
