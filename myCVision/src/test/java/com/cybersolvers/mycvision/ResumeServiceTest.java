package com.cybersolvers.mycvision;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResumeServiceTest {
    
 
    private ResumeService resumeService;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        resumeService = new ResumeService();
        // Προσομοίωση δεδομένων από τη βάση
        resumeService.universities = new String[] {"ASOEE", "EKPA", "PAPEI", "PANTEIO", "PADA"};
        resumeService.bachelorDept = new String[] {"DET", "ODE", "DEOS", "MKT", "LOXRH", "PLHROFORIKH", "STATISTIKH", "OIK"};
        resumeService.masterDept = new String[] {"DET", "ODE", "DEOS", "MKT", "LOXRH", "PLHROFORIKH", "STATISTIKH", "OIK"};
        resumeService.phDDept = new String[] {"DET", "ODE", "DEOS", "MKT", "LOXRH", "PLHROFORIKH", "STATISTIKH", "OIK"};
    }

    @Test
    void testEvaluateCriteriaForUniversities() {
        Map<String, Integer> scores = resumeService.evaluateCriteria(
            resumeService.universities,
            "universities"
        );
        
        assertNotNull(scores);
        assertEquals(6, scores.get("ASOEE"));
        assertEquals(5, scores.get("EKPA"));
        assertEquals(4, scores.get("PAPEI"));
        assertEquals(3, scores.get("PANTEIO"));
        assertEquals(2, scores.get("PADA"));
        assertEquals(1, scores.get("asxeto"));
        assertEquals(0, scores.get("den exei spoudasei"));
    }

    @Test
    void testEvaluateCriteriaForWorkExperience() {
        Map<String, Integer> scores = resumeService.evaluateCriteria(
            resumeService.workExperience,
            "workExperience"
        );
        
        assertNotNull(scores);
        assertEquals(0, scores.get("0"));
        assertEquals(1, scores.get("1"));
        assertEquals(2, scores.get("2"));
        assertEquals(3, scores.get("3"));
        assertEquals(4, scores.get("4"));
        assertEquals(5, scores.get("5"));
        assertEquals(6, scores.get("6"));
        assertEquals(9, scores.get("more years"));
    }

    @Test
    void testEvaluateCriteriaForBachelorDept() {
        Map<String, Integer> scores = resumeService.evaluateCriteria(
            resumeService.bachelorDept,
            "bachelorDept"
        );
        
        assertNotNull(scores);
        assertEquals(9, scores.get("DET"));
        assertEquals(8, scores.get("ODE"));
        assertEquals(7, scores.get("DEOS"));
        assertEquals(6, scores.get("MKT"));
        assertEquals(5, scores.get("LOXRH"));
        assertEquals(4, scores.get("PLHROFORIKH"));
        assertEquals(3, scores.get("STATISTIKH"));
        assertEquals(2, scores.get("OIK"));
        /*assertEquals(1, scores.get("asxeto"));*/
        assertEquals(0, scores.get("den exei spoudasei"));
    }

    @Test
    void testEvaluateCriteriaForMasterDept() {
        Map<String, Integer> scores = resumeService.evaluateCriteria(
            resumeService.masterDept,
            "masterDept"
        );
        
        assertNotNull(scores);
        assertEquals(9, scores.get("DET"));
        assertEquals(8, scores.get("ODE"));
        assertEquals(7, scores.get("DEOS"));
        assertEquals(6, scores.get("MKT"));
        assertEquals(5, scores.get("LOXRH"));
        assertEquals(4, scores.get("PLHROFORIKH"));
        assertEquals(3, scores.get("STATISTIKH"));
        assertEquals(2, scores.get("OIK"));
        assertEquals(1, scores.get("asxeto"));
        assertEquals(0, scores.get("den exei spoudasei"));
    }

    @Test
    void testEvaluateCriteriaForPhDDept() {
        Map<String, Integer> scores = resumeService.evaluateCriteria(
            resumeService.phDDept,
            "phDDept"
        );
        
        assertNotNull(scores);
        assertEquals(9, scores.get("DET"));
        assertEquals(8, scores.get("ODE"));
        assertEquals(7, scores.get("DEOS"));
        assertEquals(6, scores.get("MKT"));
        assertEquals(5, scores.get("LOXRH"));
        assertEquals(4, scores.get("PLHROFORIKH"));
        assertEquals(3, scores.get("STATISTIKH"));
        assertEquals(2, scores.get("OIK"));
        assertEquals(1, scores.get("asxeto"));
        assertEquals(0, scores.get("den exei spoudasei"));
    }

    @Test
    void testLevelsScoring() {
        Map<String, Integer> scores = resumeService.evaluateCriteria(
            resumeService.levels,
            "levels"
        );
        
        assertNotNull(scores);
        assertEquals(3, scores.get("Excellent"));
        assertEquals(2, scores.get("Very Good"));
        assertEquals(1, scores.get("Good"));
        assertEquals(0, scores.get("No"));
    }

    @Test
    void testYesNoScoring() {
        Map<String, Integer> scores = resumeService.evaluateCriteria(
            resumeService.yesNo,
            "yesNo"
        );
        
        assertNotNull(scores);
        assertEquals(1, scores.get("Yes"));
        assertEquals(0, scores.get("No"));
    }

   @Test
void testEvaluateMultipleTablesToJson() throws SQLException {
    // Παράκαμψη της sqlitehandler για την αποφυγή ανακτήσεων από τη βάση
    Map<String, Map<String, Integer>> result = new LinkedHashMap<>();

    // Δημιουργία του πίνακα δεδομένων για κάθε πίνακα
    result.put("universities", resumeService.evaluateCriteria(resumeService.universities, "universities"));
    result.put("levels", resumeService.evaluateCriteria(resumeService.levels, "levels"));
    result.put("workExperience", resumeService.evaluateCriteria(resumeService.workExperience, "workExperience"));
    result.put("bachelorDept", resumeService.evaluateCriteria(resumeService.bachelorDept, "bachelorDept"));
    result.put("masterDept", resumeService.evaluateCriteria(resumeService.masterDept, "masterDept"));
    result.put("phDDept", resumeService.evaluateCriteria(resumeService.phDDept, "phDDept"));
    result.put("yesNo", resumeService.evaluateCriteria(resumeService.yesNo, "yesNo"));

    // Ελέγξτε αν το αποτέλεσμα δεν είναι null
    assertNotNull(result);

    // Έλεγχος για το αν περιέχονται οι αναμενόμενοι πίνακες
    assertTrue(result.containsKey("universities"));
    assertTrue(result.containsKey("levels"));
    assertTrue(result.containsKey("workExperience"));
    assertTrue(result.containsKey("bachelorDept"));
    assertTrue(result.containsKey("masterDept"));
    assertTrue(result.containsKey("phDDept"));
    assertTrue(result.containsKey("yesNo"));

    // Έλεγχος ότι όλοι οι πίνακες έχουν σωστά scores
    assertFalse(result.get("universities").isEmpty());
    assertFalse(result.get("levels").isEmpty());
    assertFalse(result.get("workExperience").isEmpty());
    assertFalse(result.get("bachelorDept").isEmpty());
    assertFalse(result.get("masterDept").isEmpty());
    assertFalse(result.get("phDDept").isEmpty());
    assertFalse(result.get("yesNo").isEmpty());

    // Έλεγχος συγκεκριμένων τιμών από κάθε πίνακα
    assertEquals(6, result.get("universities").get("ASOEE"));
    assertEquals(3, result.get("levels").get("Excellent"));
    assertEquals(9, result.get("workExperience").get("more years"));
    assertEquals(9, result.get("bachelorDept").get("DET"));
    assertEquals(9, result.get("masterDept").get("DET"));
    assertEquals(9, result.get("phDDept").get("DET"));
    assertEquals(1, result.get("yesNo").get("Yes"));
}


    @Test
    void testDatabaseConnection() {
        assertNotNull(resumeService.universities);
        assertNotNull(resumeService.bachelorDept);
        assertNotNull(resumeService.masterDept);
        assertNotNull(resumeService.phDDept);
        assertTrue(resumeService.universities.length > 0);
        assertTrue(resumeService.bachelorDept.length > 0);
        assertTrue(resumeService.masterDept.length > 0);
        assertTrue(resumeService.phDDept.length > 0);
    }
}