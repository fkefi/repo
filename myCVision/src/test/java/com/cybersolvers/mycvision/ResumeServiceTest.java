package com.cybersolvers.mycvision;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResumeServiceTest {

    @Mock
    private SQLiteHandler sqliteHandler;

    private ResumeService resumeService;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        
        // Mock τις απαντήσεις του SQLiteHandler
        when(sqliteHandler.fetchTable("universities")).thenReturn("ASOEE","EKPA", "PAPEI","PANTEIO","PADA");
        when(sqliteHandler.fetchTable("workExperience")).thenReturn("1", "2", "3", "4", "5", "6", "6+");
        when(sqliteHandler.fetchTable("bachelorDept")).thenReturn("DET", "ODE", "DEOS", "MKT", "LOXRH", "PLHROFORIKH", "STATISTIKH", "OIK");
        when(sqliteHandler.fetchTable("masterDept")).thenReturn("DET", "ODE", "DEOS", "MKT", "LOXRH", "PLHROFORIKH", "STATISTIKH", "OIK");
        when(sqliteHandler.fetchTable("phDDept")).thenReturn("DET", "ODE", "DEOS", "MKT", "LOXRH", "PLHROFORIKH", "STATISTIKH", "OIK");
        
        resumeService = new ResumeService();
    }

    @Test
    void testEvaluateCriteriaForUniversities() {
        Map<String, Integer> scores = resumeService.evaluateCriteria(
            resumeService.arrays.get(0),  // Απευθείας πρόσβαση στο universities array
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
            resumeService.arrays.get(1),  // Απευθείας πρόσβαση στο workExperience array
            "workExperience"
        );
        
        assertNotNull(scores);
        assertEquals(1, scores.get("1"));
        assertEquals(2, scores.get("2"));
        assertEquals(3, scores.get("3"));
        assertEquals(4, scores.get("4"));
        assertEquals(3, scores.get("5"));
        assertEquals(4, scores.get("6"));
        assertEquals(8, scores.get("more years"));
    }

    @Test
    void testEvaluateCriteriaForBachelorDept() {
        Map<String, Integer> scores = resumeService.evaluateCriteria(
            resumeService.arrays.get(3),  // Απευθείας πρόσβαση στο bachelorDept array
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
        assertEquals(1, scores.get("asxeto"));
        assertEquals(0, scores.get("den exei spoudasei"));
    }

    @Test
    void testEvaluateCriteriaFormasterDept() {
        Map<String, Integer> scores = resumeService.evaluateCriteria(
            resumeService.arrays.get(4),  // Απευθείας πρόσβαση στο masterDept array
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
    void testEvaluateCriteriaForphDDept() {
        Map<String, Integer> scores = resumeService.evaluateCriteria(
            resumeService.arrays.get(5),  // Απευθείας πρόσβαση στο phDDept array
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
            resumeService.levels,  // Απευθείας πρόσβαση στο levels array
            "levels"
        );
        
        assertNotNull(scores);
        assertEquals(3, scores.get("TELEIA"));
        assertEquals(2, scores.get("POLY KALA"));
        assertEquals(1, scores.get("KALA"));
        assertEquals(0, scores.get("OXI"));
    }

    @Test
    void testEvaluateMultipleTablesToJson() throws SQLException {
        Map<String, Map<String, Integer>> result = resumeService.evaluateMultipleTablesToJson();
        
        assertNotNull(result);
        assertTrue(result.containsKey("universities"));
        assertTrue(result.containsKey("levels"));
        assertTrue(result.containsKey("workExperience"));
        assertTrue(result.containsKey("bachelorDept"));
        assertTrue(result.containsKey("masterDept"));
        assertTrue(result.containsKey("phDDept"));
        
        // Έλεγχος ότι όλοι οι πίνακες έχουν σωστά scores
        assertFalse(result.get("universities").isEmpty());
        assertFalse(result.get("levels").isEmpty());
        assertFalse(result.get("workExperience").isEmpty());
        assertFalse(result.get("bachelorDept").isEmpty());
        assertFalse(result.get("masterDept").isEmpty());
        assertFalse(result.get("phDDept").isEmpty());
        
        verify(sqliteHandler, times(1)).insertNestedIntegerData(eq("allTablesData"), any());
    }

    @Test
    void testConstructorThrowsSQLException() {
        when(sqliteHandler.fetchTable(any())).thenThrow(new SQLException("Database error"));
        
        assertThrows(SQLException.class, () -> {
            new ResumeService();
        });
    }
}
