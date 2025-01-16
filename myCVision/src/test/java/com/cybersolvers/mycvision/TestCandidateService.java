package com.cybersolvers.mycvision;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestCandidateService {

    protected CandidateService candidateService;
    @Mock
    protected SQLiteHandler mockHandler;
    @Mock
    protected Txtreader mockReader;
    Map<String, Object> cand4;
    Map<String, Object> cand3;
    Map<String, Object> cand1;
    Map<String, Object> cand2;
    Map<String, Map<String, Object>> candidates;
    Map<String, Map<String, Integer>> numbers;
    String[][] id;
    double[] weight;

     @BeforeAll
    void setUp() throws SQLException {
        candidateService = new CandidateService();
        mockHandler = Mockito.mock(SQLiteHandler.class);
        mockReader = Mockito.mock(Txtreader.class);

        candidates = new LinkedHashMap<>();
        // Candidate 1
        cand1 = new LinkedHashMap<>();
        {
        cand1.put("fullName", "John Doe");
        cand1.put("undergraduateUniversity", "Assoe");
        cand1.put("undergraduateDepartment", "Computer Science");
        cand1.put("undergraduateGrade", 9.8);
        cand1.put("masterUniversity", "Assoe");
        cand1.put("masterDepartment", "AI");
        cand1.put("masterGrade", 8.0);
        cand1.put("phdUniversity", "Stanford");
        cand1.put("phdDepartment", "AI Research");
        cand1.put("phdGrade", 7.5);
        cand1.put("englishLevel", "Fluent");
        cand1.put("frenchLevel", "Intermediate");
        cand1.put("germanLevel", "Basic");
        cand1.put("spanishLevel", "Intermediate");
        cand1.put("chineseLevel", "Basic");
        cand1.put("otherLanguageLevel", "None");
        cand1.put("workExperienceYears", "5");
        cand1.put("officeSkills", "Advanced");
        cand1.put("programmingLanguage", "yes");
        }

        // Candidate 2
        cand2 = new LinkedHashMap<>();
        {
        cand2.put("fullName", "Jane Smith");
        cand2.put("undergraduateUniversity", "Oxford");
        cand2.put("undergraduateDepartment", "Physics");
        cand2.put("undergraduateGrade", 7.7);
        cand2.put("masterUniversity", "Cambridge");
        cand2.put("masterDepartment", "Quantum Mechanics");
        cand2.put("masterGrade", 8.2);
        cand2.put("phdUniversity", "Harvard");
        cand2.put("phdDepartment", "Physics Research");
        cand2.put("phdGrade", 6.3);
        cand2.put("englishLevel", "Fluent");
        cand2.put("frenchLevel", "Fluent");
        cand2.put("germanLevel", "Intermediate");
        cand2.put("spanishLevel", "Basic");
        cand2.put("chineseLevel", "Intermediate");
        cand2.put("otherLanguageLevel", "Advanced");
        cand2.put("workExperienceYears", "3");
        cand2.put("officeSkills", "Intermediate");
        cand2.put("programmingLanguage", "no");
        }

        //Candidate 3 wrong
        cand3 = new LinkedHashMap<>();
        {
        cand3.put("fullName", "123");
        cand3.put("undergraduateUniversity", "456");
        cand3.put("undergraduateDepartment", "789");
        cand3.put("undergraduateGrade", 0.0);
        cand3.put("masterUniversity", "11");
        cand3.put("masterDepartment", "12");
        cand3.put("masterGrade", 0.2);
        cand3.put("phdUniversity", "14");
        cand3.put("phdDepartment", "15");
        cand3.put("phdGrade", 4.0);
        cand3.put("englishLevel", "17");
        cand3.put("frenchLevel", "18");
        cand3.put("germanLevel", "19");
        cand3.put("spanishLevel", "20");
        cand3.put("chineseLevel", "21");
        cand3.put("otherLanguageLevel", "22");
        cand3.put("workExperienceYears", "23");
        cand3.put("officeSkills", "24");
        cand3.put("programmingLanguage", "25");

        }

        //Candidate 4 wrong
       cand4 = new LinkedHashMap<>();
    {
        cand4.put("fullName", "Alice Johnson");
        cand4.put("ty", "Oxford");
        cand4.put("undergraduateDepartment", "Physics");
        cand4.put("undergraduateGrade", 3.7);
        cand4.put("masterUniversity", "Cambridge");
        cand4.put("masterDepartment", "Quantum Mechanics");
        cand4.put("masterGrade", 6.2);
        cand4.put("phdUniversity", "Harvard");
        cand4.put("phdDepartment", "Physics Research");
        cand4.put("phdGrade", 4.3);
        cand4.put("englishLevel", "Fluent");
        cand4.put("frenchLevel", "Fluent");
        cand4.put("germanLevel", "Intermediate");
        cand4.put("s", "Basic");
        cand4.put("chineseLevel", "Intermediate");
        cand4.put("otherLanguageLevel", "Advanced");
        cand4.put("workExperienceYears", "3");
        cand4.put("officeSkills", "Intermediate");
        cand4.put("programmingLanguage", "no");
    }

        candidates.put("cand1", cand1);
        candidates.put("cand2", cand2);
        candidates.put("cand3", cand3);
        candidates.put("cand4", cand4);
        Mockito.when(mockReader.toMap()).thenReturn(candidates);

        numbers = new LinkedHashMap<>();
        numbers.put("universities", Map.of(
                "Harvard", 10,
                "MIT", 9,
                "Stanford", 8,
                "Oxford", 7,
                "Cambridge", 6
        ));
        numbers.put("bachelorDept", Map.of(
                "Computer Science", 10,
                "Physics", 8
        ));
        numbers.put("masterDept", Map.of(
                "AI", 9,
                "Quantum Mechanics", 7
        ));
        numbers.put("phDDept", Map.of(
                "AI Research", 10,
                "Physics Research", 8
        ));
        numbers.put("levels", Map.of(
                "Fluent", 10,
                "Intermediate", 7,
                "Basic", 4,
                "None", 0,
                "Advanced", 9
        ));
        numbers.put("yesNo", Map.of(
                "yes", 10,
                "no", 0
        ));
        numbers.put("workExperience", Map.of(
                "1", 1,
                "2", 2,
                "3", 3,
                "4", 4,
                "5", 5,
                "6++", 6
        ));

        Mockito.when(mockHandler.fetchMapFromDatabase("allTablesData")).thenReturn(numbers);

                id = new String[][]{
                {"John Doe", "1"},
                {"Jane Smith", "2"},
                {"Alice Johnson", "4"}
        };
        Mockito.when(mockHandler.fetchStringArray("ID")).thenReturn(id);
        
        
        weight = new double[]{
                0.08, 0.07, 0.06, 0.05, 0.10, 0.08, 0.06, 0.07, 
                0.05, 0.09, 0.10, 0.04, 0.08, 0.06, 0.05, 0.05, 
                0.04, 0.07
        };

        Mockito.when(mockHandler.fetchDouble1DArray("Weight")).thenReturn(weight);
    }

    @Test
    void testContructor() {
        assertNotNull(candidateService);
        assertNotNull(candidateService.handler);
        assertNotNull(candidateService.reader);
    }

    @Test
    void testReviewCandidates() throws Exception {
        double[][] reviewedCandidates = candidateService.reviewCandidates();
    

        // checks the length of the final table
        assertEquals(reviewedCandidates.length, candidateService.numberOfCandidates);
    
        // checks if the candidates are sorted by score descending
        for (int i = 1; i < reviewedCandidates.length; i++) {
           assertTrue(reviewedCandidates[i - 1][1] >= reviewedCandidates[i][1]);
        }
    }

    @Test
    void testCalculateScore() {
        double calculatedScore = candidateService.calculateScore(0);
        double expectedScore = 8.383;
        assertEquals(expectedScore, calculatedScore, 0.001);
    
        calculatedScore = candidateService.calculateScore(2);
        expectedScore = 0.44600000000000006;
        assertEquals(expectedScore, calculatedScore, 0.001);
    
        calculatedScore = candidateService.calculateScore(3);
        expectedScore = 6.9110000000000005;
        assertEquals(expectedScore, calculatedScore, 0.001);
        }

        public void testCreatePoints() {
            double[][] points = candidateService.createPoints();
    
            // Assert that the points array has the expected size
            assertEquals(points.length, 4);  // cand1-cand4
            assertEquals(points[0].length, 18);  // Expecting 18 criteria + fullName (first column 0)
            assertEquals(1.0, points[0][0]);  
            assertEquals(3.0, points[1][0]);  
        }

        @Test
        public void testCompareCandidateWithNumbers() {
            
            double[] result = candidateService.compareCandidateWithNumbers(cand1, numbers, 1.0);
    
            assertEquals(1.0, result[0], 0.001);
            assertEquals(1.0, result[1], 0.001);
            assertEquals(10.0, result[2], 0.001);
            assertEquals(9.8, result[3], 0.001);
            assertEquals(10.0, result[10], 0.001);
            assertEquals(5.0, result[16], 0.001);
            assertEquals(9.0, result[17], 0.001);
            assertEquals(10.0, result[18], 0.001);
    
            result = candidateService.compareCandidateWithNumbers(cand3, numbers, 3.0);
    
            assertEquals(3.0, result[0]);
            assertEquals(0.0, result[4], 0.001);
            assertEquals(0.0, result[5], 0.001);
            assertEquals(0.2, result[6], 0.001);
            assertEquals(0.0, result[7], 0.001);
            assertEquals(0.0, result[8], 0.001);
            assertEquals(4.0, result[9], 0.001);
            assertEquals(0.0, result[10], 0.001);
            assertEquals(0.0, result[16], 0.001);
            assertEquals(0.0, result[17], 0.001);
            assertEquals(0.0, result[18], 0.001);
    
            result = candidateService.compareCandidateWithNumbers(cand4, numbers, 3.0);
            assertEquals(0.0, result[13], 0.001);
            assertEquals(0.0, result[1], 0.001);
        }

        @AfterAll
        public void tearDown() {
            candidateService = null;
        }
    
}