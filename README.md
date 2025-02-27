# **myCVision**

## ΟΜΑΔΑ: CYBER SOLVERS

### ΜΕΛΗ:
- Ευθαλία Φακορέλλη: t8230156
- Δέσποινα Σταυροπούλου: t8230142
- Ειρήνη Λιανού: t8230077
- Ιωάννης Βασιλόπουλος: t8220014
- Ειρήνη Στριφτόμπολα: t8230145
- Παντελής Λιακόπουλος: t8230075
- Αθανασίου Θεμιστοκλής: t8230006
- Νικόλας Κλάδης: t8230217

## Περιγραφή
Το **myCVision** είναι ένα λογισμικό που στοχεύει στη βελτιστοποίηση της διαδικασίας πρόσληψης προσωπικού για εταιρείες. Παρέχει αυτοματοποιημένη ανάλυση βιογραφικών, ταξινόμηση βάσει προκαθορισμένων κριτηρίων, και αποστολή αποτελεσμάτων με σκοπό τη μείωση διακρίσεων και την εξοικονόμηση χρόνου για τα τμήματα HR.

---


---
ΣΗΜΑΝΤΙΚΗ ΠΑΡΑΤΗΡΗΣΗ.

Κάναμε public το repository μας 18/02/2025 και αντιμετωπίσαμε αυτό το πρόβλημα, 
Your Twilio SendGrid API key has been deleted
We have detected that an API key belonging to Twilio SendGrid Account EFTHALIA FAKORELLI is publicly listed online.
To prevent unauthorized access and modification of your account, this key has been deleted.

The following API key(s) have been deleted;

Οπότε για να λειτουργήσει το email θα πρέπει να αντικαταστήσετε το παλιό API KEY με το καινούριο, που δεν μπορούμε να κάνουμε δημόσια για λόγους ασφαλείας.
Το νέο API KEY θα σας έχει αποσταλλεί στο email σας απο το t8230156@aueb.gr .
Κλάση MailerService γραμμή 20

Ευχαριστούμε για τη κατανόηση!

---

---
## Οδηγίες Μεταγλώττισης
Για να εγκαταστήσετε τις εξαρτήσεις και να μεταγλωττίσετε το έργο:
```bash
cd myCVision
mvn clean install
```

## Οδηγίες Εκτέλεσης
### Εκτέλεση της κύριας εφαρμογής:
```bash
mvn exec:java -Dexec.mainClass="com.cybersolvers.mycvision.App"
```

### Εκτέλεση δοκιμών:
```bash
cd myCVision
mvn test
```

### Δημιουργία αρχείου `.jar`:
```bash
mvn package
```

### Εκτέλεση του αρχείου `.jar`:
```bash
java -jar target/myCVision-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## Οδηγίες Χρήσης
1. Εισάγετε τα κριτήρια πρόσληψης μέσω του γραφικού περιβάλλοντος.
2. Καταθέστε τα βιογραφικά σε προαπαιτούμενη μορφή.
3. Το λογισμικό επεξεργάζεται τα δεδομένα και εμφανίζει ταξινομημένη λίστα με τα **ID** και τα **score** των υποψηφίων.
4. Εισάγετε το email σας για να λάβετε τη λίστα με **ID** και **score**.
5. Προαιρετικά, εισάγετε ένα **ID** για να δείτε:
   - Ονοματεπώνυμο
   - Κατάταξη
   - Τελικό σκορ

---

## Δομή Περιεχομένων Αποθετηρίου
- **src/main/java/com/cybersolvers/mycvision**: Κώδικας της κύριας εφαρμογής.
- **src/test/java/com/cybersolvers/mycvision**: Μονάδες δοκιμών.
- **target/**: Περιέχει το παραγόμενο αρχείο `.jar` μετά τη μεταγλώττιση.
- **resources/**: Πρόσθετοι πόροι (εικονίδια, δείγματα δεδομένων κ.λπ.).

---

## Διάγραμμα UML
```mermaid
classDiagram
    class CustomCriteriaAppdone {
        +createAndShowGUI()
        +initializeFrame()
        +loadLogo()
        +initializeMainPanel()
        +addComponents()
        +createGridBagConstraints()
        +addDegreeSelections()
        +addUniversitySection()
        +addLanguageSection()
        +addWorkExperienceSection()
        +addTechnicalSkillsSection()
        +addWeightField()
        +addMultipleSelection()
        +createCheckBoxListener()
        +addSubmitButton()
        +handleSubmission()
        +validateWeights()
        +saveSelections()
        +finalizeFrame()
        +getUniversityList()
        +getDepartmentList()
        +getTechnicalSkillsList()
        +displayAllSavedData()
    }

    class ResumeService {
        +evaluateCriteria()
        +evaluateMultipleTablesToJson()
    }

    class CandidateService {
        +reviewCandidates()
        +calculateScore()
        +createPoints()
        +compareCandidateWithNumbers()
    }

    class CodeSearchFilter {
        +requestCode()
    }

    class CVSubmissionApp2 {
        +saveCVsToFolder()
    }

    class MailerService {
        +sendMail(candidates)
    }

    class SQLiteHandler {
        +insertStringArray()
        +fetchDoubleArray()
        +fetchDouble1DArray()
        +fetchStringArray()
        +fetchMapFromDatabase()
        +insertDoubleArray()
        +close()
    }

    class Filter {
        +processCandidates()
        +searchByCode()
        +generateRandomCode()
    }

    class Txtreader {
        +processFiles()
        +clearData()
        +toMap()
    }

    class App {
        +main(String[] args)
    }

    CustomCriteriaAppdone --> ResumeService : passes criteria to
    CustomCriteriaAppdone --> CandidateService : passes weights to
    CandidateService --> Txtreader : gets candidates from
    MailerService --> SQLiteHandler : interacts with
    CandidateService --> SQLiteHandler : interacts with
    Filter --> SQLiteHandler : interacts with
    ResumeService --> SQLiteHandler : interacts with
    App --> CandidateService : gets the final result from
    App --> MailerService : sends mail with results
```

---

## Επισκόπηση Δομών Δεδομένων και Αλγορίθμων
- **Δομές Δεδομένων**:
  - Χρήση πινάκων και λιστών για την αποθήκευση κριτηρίων και υποψηφίων(LinkedHashMap & Map & Nested Map, Array Tables, List & DefaultListModel, JSON &Gson, Set).
  - Βάση δεδομένων SQLite για την αποθήκευση και ανάκτηση πληροφοριών.
- **Αλγόριθμοι**:
  - Μετατροπή των κριτηρίων σε μονάδες με βάση τις προτιμήσεις της επιχείρησης.
  - Υπολογισμός βαθμολογιών (score) υποψηφίων μέσω γραμμικών συναρτήσεων.
  - Ταξινόμηση υποψηφίων με βάση τα σκορ.
  - Δημιουργία και αποστολή email με την ταξινομημένη λίστα υποψηφίων.
  - Φίλτρο για την αναζήτηση των υποψηφίων με το μοναδικό τους κωδικό.

---

---
Το πρόγραμμα μας δέχεται τα βιογραφικά μόνο σε αυτή τη μορφή txt.

Full Name:

University (Name/No):

Department:

Undergraduate Grade:

Masters University (Name/No):

Masters Department:

Masters Grade:

PhD University (Name/No):

PhD Department:

PhD Grade:

English (Excellent/Very Good/Good/No):

French (Excellent/Very Good/Good/No):

German (Excellent/Very Good/Good/No):

Spanish (Excellent/Very Good/Good/No):

Chinese (Excellent/Very Good/Good/No):

Other Language Level (Excellent/Very Good/Good/No):

Years of Experience:

Office Skills (Excellent/Very Good/Good/No):

Programming Skills (Yes/No):



παράδειγμα συμπληρωμένου txt:

Full Name: Johny Markus

University (Name/No): Massachusetts Institute of Technology

Department: Computer Science

Undergraduate Grade: 3.8

Masters University (Name/No): Stanford University

Masters Department: Artificial Intelligence

Masters Grade: 3.9

PhD University (Name/No): AUEB

PhD Department: DET

PhD Grade: 5.5

English (Excellent/Very Good/Good/No): Good

French (Excellent/Very Good/Good/No): Good

German (Excellent/Very Good/Good/No): Very Good

Spanish (Excellent/Very Good/Good/No): No

Chinese (Excellent/Very Good/Good/No): No
 
Other Language Level (Excellent/Very Good/Good/No): No

Years of Experience: 5

Office Skills (Excellent/Very Good/Good/No): Excellent

Programming Skills (Yes/No): No


---

---
ΣΧΕΤΙΚΑ ΜΕ ΤΟ ΠΡΟΒΛΗΜΑ ΠΟΥ ΑΝΤΙΜΕΤΩΠΙΣΑΜΕ ΜΕ ΤΗ ΒΑΣΗ ΔΕΔΟΜΕΝΩΝ:
Λόγω έλλειψης χρόνου, δεν καταφέραμε να λύσουμε ολοκληρωτικά το πρόβλημα που είχαμε με τη βάση δεδομένων, οπότε οι πίνακες που αφορούν τα κριτήρια της εταιρίας - πελάτη συνεχίζουν να διαγράφονται, μιας και δεν χρησιμεύουν κάπου πλέον στην εκτέλεση του προγράμματος, αφού κάθε χρήστης εισάγει τα δικά του κριτήρια, αλλά όλοι οι υπόλοιποι πίνακες που δημιουργούνται κατά την διάρκεια του προγράμματος διατηρούνται στη βάση μας κανονικά. Άρα, η βάση δεδομένων συνεχίζει να μας είναι χρήσιμη!
