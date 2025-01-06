//package demo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

public class DROPBOX {
    private static final File selectedFolder = new File("D:\\MYCVISION"); // Αυτόματος φάκελος αποθήκευσης
    private static DefaultListModel<String> listModel; // Για την προβολή των επιλεγμένων βιογραφικών

    public static void main(String[] args) {
        // Ρυθμίσεις του παραθύρου
        JFrame frame = new JFrame("Κατάθεση βιογραφικών");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Δημιουργία του κύριου panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Τίτλος και Υπότιτλος
        JLabel title = new JLabel("Κατάθεση βιογραφικών", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);  // Κεντράρισμα του τίτλου
        panel.add(title);

        JLabel subtitle = new JLabel("Εισάγετε τα βιογραφικά των υποψηφίων", JLabel.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);  // Κεντράρισμα του υπότιτλου
        panel.add(subtitle);

        // Δημιουργία του drop box (κουτί για drag and drop)
        JPanel dropBoxPanel = new JPanel();
        dropBoxPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));  // Γκρί περίγραμμα
        dropBoxPanel.setPreferredSize(new Dimension(250, 200));  // Μικρότερο και τετράγωνο
        dropBoxPanel.setLayout(new BorderLayout());
        dropBoxPanel.setToolTipText("Σύρετε τα αρχεία εδώ για να τα προσθέσετε");

        // Προσθήκη drag and drop δυνατότητας
        addDragAndDropSupport(dropBoxPanel);

        // Λίστα με τα επιλεγμένα αρχεία
        listModel = new DefaultListModel<>();
        JList<String> fileList = new JList<>(listModel);
        JScrollPane fileListScroll = new JScrollPane(fileList);
        fileList.setVisibleRowCount(5);

        // Προσθήκη του drop box και της λίστας στο panel
        panel.add(dropBoxPanel);
        panel.add(fileListScroll);

        // Κουμπί "Τέλος"
        JButton finishButton = new JButton("Τέλος");
        finishButton.setFont(new Font("Arial", Font.PLAIN, 16));
        finishButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                finishProcess();
                System.exit(0); // Κλείσιμο της εφαρμογής όταν πατηθεί το "Τέλος"
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(finishButton);

        panel.add(buttonPanel);

        // Εμφάνιση του παραθύρου
        frame.add(panel);
        frame.setVisible(true);

        // Έλεγχος αν ο φάκελος υπάρχει
        if (!selectedFolder.exists()) {
            JOptionPane.showMessageDialog(null, "Ο φάκελος αποθήκευσης δεν υπάρχει: " + selectedFolder.getAbsolutePath(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private static void addDragAndDropSupport(JPanel dropBoxPanel) {
        // Δημιουργία της λειτουργίας drag and drop
        dropBoxPanel.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferHandler.TransferSupport support) {
                // Ελέγχουμε αν τα δεδομένα είναι αρχεία
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferHandler.TransferSupport support) {
                // Διαχείριση της πτώσης των αρχείων
                if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    return false;
                }
                try {
                    Transferable transferable = support.getTransferable();
                    List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : files) {
                        // Προσθήκη κάθε αρχείου στη λίστα
                        listModel.addElement(file.getAbsolutePath());
                    }
                    return true;
                } catch (UnsupportedFlavorException | IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
    }

    private static void finishProcess() {
        if (listModel.size() == 0) {
            JOptionPane.showMessageDialog(null, "Δεν έχετε εισάγει βιογραφικά.", "Προειδοποίηση", JOptionPane.WARNING_MESSAGE);
        } else {
            // Μεταφορά των αρχείων στον επιλεγμένο φάκελο
            for (int i = 0; i < listModel.size(); i++) {
                File sourceFile = new File(listModel.getElementAt(i));
                File destFile = new File(selectedFolder, sourceFile.getName());
                try {
                    // Αν το αρχείο υπάρχει ήδη στον φάκελο, το διαγράφουμε πρώτα
                    if (destFile.exists()) {
                        destFile.delete();
                    }
                    // Αντιγραφή του αρχείου στον φάκελο
                    Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Σφάλμα κατά την αποθήκευση των αρχείων.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Η κατάθεση έγινε επιτυχώς.", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
