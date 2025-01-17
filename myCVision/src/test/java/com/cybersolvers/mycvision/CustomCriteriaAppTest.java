package com.cybersolvers.mycvision;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.List;
import java.lang.reflect.Method;

public class CustomCriteriaAppTest {
    private CustomCriteriaApp app;

    @Before
    public void setUp() throws SQLException,  ClassNotFoundException {
        app = new CustomCriteriaApp();
    }

    @Test
    public void testGetUniversityList() throws Exception {
        Method method = CustomCriteriaApp.class.getDeclaredMethod("getUniversityList");
        method.setAccessible(true);
        String[] universities = (String[]) method.invoke(null);
        
        assertNotNull("University list should not be null", universities);
        assertTrue("University list should not be empty", universities.length > 0);
        
        // Test for specific universities that we know should be in the list
        boolean hasHarvard = false;
        boolean hasAthens = false;
        
        for (String university : universities) {
            if (university.contains("Harvard")) hasHarvard = true;
            if (university.contains("Athens")) hasAthens = true;
        }
        
        assertTrue("University list should contain Harvard", hasHarvard);
        assertTrue("University list should contain at least one Athens university", hasAthens);
    }

    @Test
    public void testGetDepartmentList() throws Exception {
        Method method = CustomCriteriaApp.class.getDeclaredMethod("getDepartmentList");
        method.setAccessible(true);
        String[] departments = (String[]) method.invoke(null);
        
        assertNotNull("Department list should not be null", departments);
        assertTrue("Department list should not be empty", departments.length > 0);
        
        // Test for specific departments that we know should be in the list
        boolean hasCS = false;
        boolean hasMedicine = false;
        
        for (String department : departments) {
            if (department.contains("Computer Science")) hasCS = true;
            if (department.contains("Medicine")) hasMedicine = true;
        }
        
        assertTrue("Department list should contain Computer Science", hasCS);
        assertTrue("Department list should contain Medicine", hasMedicine);
    }

    @Test
    public void testGetTechnicalSkillsList() throws Exception {
        Method method = CustomCriteriaApp.class.getDeclaredMethod("getTechnicalSkillsList");
        method.setAccessible(true);
        String[] skills = (String[]) method.invoke(null);
        
        assertNotNull("Technical skills list should not be null", skills);
        assertTrue("Technical skills list should not be empty", skills.length > 0);
        
        // Test for specific skills that we know should be in the list
        boolean hasJava = false;
        boolean hasSQL = false;
        
        for (String skill : skills) {
            if (skill.equals("Java")) hasJava = true;
            if (skill.equals("SQL")) hasSQL = true;
        }
        
        assertTrue("Technical skills list should contain Java", hasJava);
        assertTrue("Technical skills list should contain SQL", hasSQL);
    }

    @Test
    public void testWeightCriteriaConstants() throws Exception {
        // Test that all required weight criteria are present
        String[] expectedCriteria = {
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
        
        // Get the WEIGHT_CRITERIA field using reflection
        java.lang.reflect.Field field = CustomCriteriaApp.class.getDeclaredField("WEIGHT_CRITERIA");
        field.setAccessible(true);
        String[] actualCriteria = (String[]) field.get(null);
        
        assertNotNull("Weight criteria should not be null", actualCriteria);
        assertEquals("Weight criteria should have correct number of elements", 
                    expectedCriteria.length, actualCriteria.length);
        
        // Check that all expected criteria are present
        for (int i = 0; i < expectedCriteria.length; i++) {
            assertEquals("Weight criteria at index " + i + " should match", 
                        expectedCriteria[i], actualCriteria[i]);
        }
    }

    @Test
    public void testWorkExperienceOptions() throws Exception {
        java.lang.reflect.Field field = CustomCriteriaApp.class.getDeclaredField("WORK_EXPERIENCE_OPTIONS");
        field.setAccessible(true);
        String[] workExperienceOptions = (String[]) field.get(null);
        
        assertNotNull("Work experience options should not be null", workExperienceOptions);
        assertTrue("Work experience options should not be empty", workExperienceOptions.length > 0);
        
        // Test for specific options that we know should be in the list
        boolean has1Year = false;
        boolean has6PlusYears = false;
        
        for (String option : workExperienceOptions) {
            if (option.equals("1 year")) has1Year = true;
            if (option.equals("6+ years")) has6PlusYears = true;
        }
        
        assertTrue("Work experience options should contain '1 year'", has1Year);
        assertTrue("Work experience options should contain '6+ years'", has6PlusYears);
    }
}