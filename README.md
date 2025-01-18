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

## Οδηγίες Μεταγλώττισης
Για να εγκαταστήσετε τις εξαρτήσεις και να μεταγλωττίσετε το έργο:
```bash
cd myCVision/src/test/java/com/cybersolvers/mycvision
mvn clean install
```

---

## Οδηγίες Εκτέλεσης
### Εκτέλεση της κύριας εφαρμογής:
```bash
mvn exec:java -Dexec.mainClass="com.cybersolvers.mycvision.App"
```

### Εκτέλεση δοκιμών:
```bash
cd myCVision/src/test/java/com/cybersolvers/mycvision
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
  - Χρήση πινάκων και λιστών για την αποθήκευση κριτηρίων και υποψηφίων(LinkedHashMap, Array Tables, List, JSON &Gson).
  - Βάση δεδομένων SQLite για την αποθήκευση και ανάκτηση πληροφοριών.
- **Αλγόριθμοι**:
  - Υπολογισμός βαρών και βαθμολογιών μέσω γραμμικών συναρτήσεων.
  - Ταξινόμηση υποψηφίων με βάση τα σκορ.
  - Δημιουργία και αποστολή email με την ταξινομημένη λίστα υποψηφίων.

---
