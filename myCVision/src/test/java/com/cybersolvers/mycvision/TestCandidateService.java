package test.java.com.cybersolvers;

import java.util.LinkedHashMap;
import java.util.Map;

class TestCandidateService {

    private CandidateService candidateService;
    private SQLiteHandler mockHandler;
    private Txtreader mockReader;

    @Before
    public void setUp() {
        candidateService = new CandidateService();
        mockHandler = Mockito.mock(SQLiteHandler.class);
        mockReader = Mockito.mock(Txtreader.class);

        Map<String, Map<String, Object>> candidates = new LinkedHashMap<>();
        // Candidate 1
        Map<String, Object> cand1 = new LinkedHashMap<>();
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
        Map<String, Object> cand2 = new LinkedHashMap<>();
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
        Map<String, Object> cand3 = new LinkedHashMap<>();
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
    Map<String, Object> cand4 = new LinkedHashMap<>();
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

        Map<String, Map<String, Integer>> numbers = new LinkedHashMap<>();
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

        String[][] id = new String[][]{
                {"John Doe", "1"},
                {"Jane Smith", "2"},
                {"Alice Johnson", "4"}
        };
        Mockito.when(mockHandler.fetchStringArray("ID")).thenReturn(id);
        
        
        double[] weight = new double[]{
                0.08, 0.07, 0.06, 0.05, 0.10, 0.08, 0.06, 0.07, 
                0.05, 0.09, 0.10, 0.04, 0.08, 0.06, 0.05, 0.05, 
                0.04, 0.07
        };

        Mockito.when(mockHandler.fetchDouble1DArray("Weight")).thenReturn(weight);

        candidateService.initialize();
        doNothing().when(handler).insertDoubleArray("finalCandidates", finalCandidates);


    }

    public void testContructor() {
        Assert.assertNotNull("failure ‐ CandidateService not initialized", candidateService);
        Assert.assertNotNull("failure ‐ Handler not initialized", candidateService.handler);
        Assert.assertNotNull("failure ‐ Reader not initialized", candidateService.reader);
    }
    

    public void testReviewCandidates() {
        double[][] reviewedCandidates = candidateService.reviewCandidates();

        // checks the length of the final table
        Assert.assertEquals("failure ‐ wrong number of reviewed candidates", reviewedCandidates.length, candidateService.numberOfCandidates);
    
        // checks if the candidates are sorted by score descending
        for (int i = 1; i < reviewedCandidates.length; i++) {
            Assert.assertTrue("failure ‐ candidates not sorted by score", reviewedCandidates[i - 1][1] >= reviewedCandidates[i][1]);
        }
    }

    public void testCalculateScore() {
    double calculatedScore = candidateService.calculateScore(0);
    double expectedScore = 8.383;
    Assert.assertEquals("failure ‐ incorrect score calculation", expectedScore, calculatedScore, 0.001);

    calculatedScore = candidateService.calculateScore(2);
    expectedScore = 0.44600000000000006;
    Assert.assertEquals("failure ‐ incorrect score calculation", expectedScore, calculatedScore, 0.001);

    calculatedScore = candidateService.calculateScore(3);
    expectedScore = 6.9110000000000005;
    Assert.assertEquals("failure ‐ incorrect score calculation", expectedScore, calculatedScore, 0.001);
    }


    public void testCreatePoints() {
        double[][] points = candidateService.createPoints();

        // Assert that the points array has the expected size
        Assert.assertEquals("failure ‐ wrong points size", points.length, 4);  // cand1-cand4
        Assert.assertEquals("failure ‐ wrong number of criteria", points[0].length, 18);  // Expecting 18 criteria + fullName (first column 0)
        Assert.assertEquals("failure ‐ incorrect uniqueId for cand1", 1.0, points[0][0], 0.001);  
        Assert.assertEquals("failure ‐ incorrect uniqueId for cand3", 3.0, points[1][0], 0.001);  
    }


    public void testCompareCandidateWithNumbers() {
        double[] result = candidateService.compareCandidateWithNumbers(cand1, numbers, id);

        Assert.assertEquals("Failure - incorrect unique ID", 1.0, result[0], 0.001);
        Assert.assertEquals("Failure - incorrect value for undergraduateUniversity", 1.0, result[1], 0.001);
        Assert.assertEquals("Failure - incorrect value for undergraduateDepartment", 10.0, result[2], 0.001);
        Assert.assertEquals("Failure - incorrect value for undergraduateGrade", 9.8, result[3], 0.001);
        Assert.assertEquals("Failure - incorrect value for masterUniversity", 9.0, result[4], 0.001);
        Assert.assertEquals("Failure - incorrect value for masterDepartment", 9.0, result[5], 0.001);
        Assert.assertEquals("Failure - incorrect value for masterGrade", 8.0, result[6], 0.001);
        Assert.assertEquals("Failure - incorrect value for phdUniversity", 8.0, result[7], 0.001);
        Assert.assertEquals("Failure - incorrect value for phdDepartment", 10.0, result[8], 0.001);
        Assert.assertEquals("Failure - incorrect value for phdGrade", 7.5, result[9], 0.001);
        Assert.assertEquals("Failure - incorrect value for englishLevel", 10.0, result[10], 0.001);
        Assert.assertEquals("Failure - incorrect value for frenchLevel", 7.0, result[11], 0.001);
        Assert.assertEquals("Failure - incorrect value for germanLevel", 4.0, result[12], 0.001);
        Assert.assertEquals("Failure - incorrect value for spanishLevel", 7.0, result[13], 0.001);
        Assert.assertEquals("Failure - incorrect value for chineseLevel", 4.0, result[14], 0.001);
        Assert.assertEquals("Failure - incorrect value for otherLanguageLevel", 0.0, result[15], 0.001);
        Assert.assertEquals("Failure - incorrect value for workExperienceYears", 5.0, result[16], 0.001);
        Assert.assertEquals("Failure - incorrect value for officeSkills", 9.0, result[17], 0.001);
        Assert.assertEquals("Failure - incorrect value for programmingLanguage", 10.0, result[18], 0.001);

        result = candidateService.compareCandidateWithNumbers(cand3, numbers, id);

        Assert.assertEquals("Failure - incorrect unique ID", 3.0, result[0], 0.001);
        Assert.assertEquals("Failure - incorrect value for undergraduateUniversity", 0.0, result[1], 0.001);
        Assert.assertEquals("Failure - incorrect value for undergraduateDepartment", 0.0, result[2], 0.001);
        Assert.assertEquals("Failure - incorrect value for undergraduateGrade", 0.0, result[3], 0.001);
        Assert.assertEquals("Failure - incorrect value for masterUniversity", 0.0, result[4], 0.001);
        Assert.assertEquals("Failure - incorrect value for masterDepartment", 0.0, result[5], 0.001);
        Assert.assertEquals("Failure - incorrect value for masterGrade", 0.2, result[6], 0.001);
        Assert.assertEquals("Failure - incorrect value for phdUniversity", 0.0, result[7], 0.001);
        Assert.assertEquals("Failure - incorrect value for phdDepartment", 0.0, result[8], 0.001);
        Assert.assertEquals("Failure - incorrect value for phdGrade", 4.0, result[9], 0.001);
        Assert.assertEquals("Failure - incorrect value for englishLevel", 0.0, result[10], 0.001);
        Assert.assertEquals("Failure - incorrect value for frenchLevel", 0.0, result[11], 0.001);
        Assert.assertEquals("Failure - incorrect value for germanLevel", 0.0, result[12], 0.001);
        Assert.assertEquals("Failure - incorrect value for spanishLevel", 0.0, result[13], 0.001);
        Assert.assertEquals("Failure - incorrect value for chineseLevel", 0.0, result[14], 0.001);
        Assert.assertEquals("Failure - incorrect value for otherLanguageLevel", 0.0, result[15], 0.001);
        Assert.assertEquals("Failure - incorrect value for workExperienceYears", 0.0, result[16], 0.001);
        Assert.assertEquals("Failure - incorrect value for officeSkills", 0.0, result[17], 0.001);
        Assert.assertEquals("Failure - incorrect value for programmingLanguage", 0.0, result[18], 0.001);

        result = candidateService.compareCandidateWithNumbers(cand4, numbers, id);
        Assert.assertEquals("Failure - incorrect value for spanishLevel", 0.0, result[13], 0.001);
        Assert.assertEquals("Failure - incorrect value for undergraduateUniversity", 0.0, result[1], 0.001);



    }

    @After
    public void tearDown() {
        candidateService = null;
    }

}