package com.practicetestautomation.tests.table;

import com.practicetestautomation.pageobjects.TablePage;
import com.practicetestautomation.pageobjects.TablePage.CourseRow;
import com.practicetestautomation.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class TableTests extends BaseTest {

    @Test(description = "Test case 1: Language filter -> Java")
    public void languageFilterJavaTest() {
        logger.info("Open the table page and select Language = Java");
        TablePage tablePage = openTablePage();
        tablePage.selectLanguage("Java");

        List<CourseRow> visibleRows = tablePage.getVisibleCourseRows();
        logger.info("Verify only Java courses are visible");
        Assert.assertEquals(courseNames(visibleRows), List.of(
                "Selenium Framework",
                "Selenium with Java",
                "Advanced Selenium",
                "REST Assured",
                "Selenium Grid",
                "Java for Testers"
        ), "Language filter did not leave only the Java courses");
        for (CourseRow row : visibleRows) {
            Assert.assertEquals(row.language(), "Java", row.courseName() + " is visible but is not a Java course");
        }
    }

    @Test(description = "Test case 2: Level filter -> Beginner only")
    public void levelFilterBeginnerOnlyTest() {
        logger.info("Open the table page and uncheck Intermediate and Advanced");
        TablePage tablePage = openTablePage();
        tablePage.setLevelSelected("Intermediate", false);
        tablePage.setLevelSelected("Advanced", false);

        List<CourseRow> visibleRows = tablePage.getVisibleCourseRows();
        logger.info("Verify only Beginner courses are visible");
        Assert.assertEquals(courseNames(visibleRows), List.of(
                "Selenium with Java",
                "XPath Locators",
                "Java for Testers",
                "Selenium with Python",
                "Python for Testers"
        ), "Level filter did not leave only the Beginner courses");
        for (CourseRow row : visibleRows) {
            Assert.assertEquals(row.level(), "Beginner", row.courseName() + " is visible but is not Beginner");
        }
        Assert.assertFalse(tablePage.isLevelSelected("Intermediate"), "Intermediate stayed checked");
        Assert.assertFalse(tablePage.isLevelSelected("Advanced"), "Advanced stayed checked");
        Assert.assertTrue(tablePage.isLevelSelected("Beginner"), "Beginner was unchecked");
    }

    @Test(description = "Test case 3: Min enrollments -> 10,000+")
    public void minEnrollmentsFilterTest() {
        logger.info("Open the table page and set Min enrollments = 10,000+");
        TablePage tablePage = openTablePage();
        tablePage.selectMinEnrollments("10000");

        Assert.assertEquals(tablePage.getMinEnrollmentsLabel(), "10,000+", "Min enrollments label was not updated");
        List<CourseRow> visibleRows = tablePage.getVisibleCourseRows();
        logger.info("Verify every visible row has at least 10,000 enrollments");
        Assert.assertEquals(courseNames(visibleRows), List.of(
                "Selenium with Java",
                "XPath Locators",
                "Advanced Selenium",
                "Selenium with Python"
        ), "Min enrollments filter did not keep only courses with 10,000 or more enrollments");
        for (CourseRow row : visibleRows) {
            Assert.assertTrue(row.enrollments() >= 10000,
                    row.courseName() + " shows " + row.enrollments() + " enrollments, which is below 10,000");
        }
    }

    @Test(description = "Test case 4: Combined filters -> Python + Beginner + 10,000+")
    public void combinedFiltersTest() {
        logger.info("Open the table page and apply Python, Beginner, and 10,000+ filters");
        TablePage tablePage = openTablePage();
        tablePage.selectLanguage("Python");
        tablePage.setLevelSelected("Intermediate", false);
        tablePage.setLevelSelected("Advanced", false);
        tablePage.selectMinEnrollments("10000");

        List<CourseRow> visibleRows = tablePage.getVisibleCourseRows();
        logger.info("Verify only Python Beginner courses with at least 10,000 enrollments are visible");
        Assert.assertEquals(visibleRows.size(), 1, "Expected one course to match the combined filters");
        CourseRow course = visibleRows.get(0);
        Assert.assertEquals(course.courseName(), "Selenium with Python");
        Assert.assertEquals(course.language(), "Python");
        Assert.assertEquals(course.level(), "Beginner");
        Assert.assertTrue(course.enrollments() >= 10000,
                "Selenium with Python shows " + course.enrollments() + " enrollments");
    }

    @Test(description = "Test case 5: No results state")
    public void noResultsStateTest() {
        logger.info("Open the table page and filter to Python Advanced, which has no courses");
        TablePage tablePage = openTablePage();
        tablePage.selectLanguage("Python");
        tablePage.setLevelSelected("Beginner", false);
        tablePage.setLevelSelected("Intermediate", false);

        logger.info("Verify the no matching courses message is shown");
        Assert.assertTrue(tablePage.isNoMatchingCoursesDisplayed(), "No matching courses message is not displayed");
        Assert.assertEquals(tablePage.getNoMatchingCoursesText(), "No matching courses.");
        Assert.assertFalse(tablePage.isCoursesTableDisplayed(), "Courses table stayed visible when nothing matches");
        Assert.assertTrue(tablePage.getVisibleCourseRows().isEmpty(), "Rows stayed visible when nothing matches");

        logger.info("Clear the language filter and verify the empty state goes away");
        tablePage.selectLanguage("Any");
        Assert.assertFalse(tablePage.isNoMatchingCoursesDisplayed(), "No matching courses message stayed visible after matches returned");
        Assert.assertTrue(tablePage.isCoursesTableDisplayed(), "Courses table stayed hidden after matches returned");
        Assert.assertEquals(courseNames(tablePage.getVisibleCourseRows()), List.of(
                "Selenium Framework",
                "Selenium Grid"
        ), "Empty state did not toggle back to the Advanced courses");
    }

    @Test(description = "Test case 6: Reset button visibility and behavior")
    public void resetButtonTest() {
        logger.info("Open the table page and confirm Reset is hidden on the defaults");
        TablePage tablePage = openTablePage();
        Assert.assertFalse(tablePage.isResetButtonDisplayed(), "Reset button is visible before any filter changes");

        logger.info("Change the language filter and verify Reset becomes visible");
        tablePage.selectLanguage("Java");
        Assert.assertTrue(tablePage.isResetButtonDisplayed(), "Reset button stayed hidden after a filter change");

        logger.info("Click Reset and verify the default filters and all rows return");
        tablePage.clickReset();
        Assert.assertEquals(tablePage.getSelectedLanguage(), "Any", "Language was not reset to Any");
        Assert.assertTrue(tablePage.isLevelSelected("Beginner"), "Beginner was not checked after reset");
        Assert.assertTrue(tablePage.isLevelSelected("Intermediate"), "Intermediate was not checked after reset");
        Assert.assertTrue(tablePage.isLevelSelected("Advanced"), "Advanced was not checked after reset");
        Assert.assertEquals(tablePage.getMinEnrollmentsLabel(), "Any", "Min enrollments label was not reset to Any");
        Assert.assertEquals(tablePage.getMinEnrollmentsValue(), "any", "Min enrollments value was not reset to any");
        Assert.assertFalse(tablePage.isResetButtonDisplayed(), "Reset button stayed visible after reset");
        Assert.assertEquals(courseNames(tablePage.getVisibleCourseRows()), List.of(
                "Selenium Framework",
                "Selenium with Java",
                "XPath Locators",
                "Advanced Selenium",
                "REST Assured",
                "Selenium Grid",
                "Java for Testers",
                "Selenium with Python",
                "Python for Testers"
        ), "Reset did not show every course");
    }

    @Test(description = "Test case 7: Sort by Enrollments (ascending, numeric)")
    public void sortByEnrollmentsTest() {
        logger.info("Open the table page and sort by Enrollments");
        TablePage tablePage = openTablePage();
        tablePage.selectSortBy("Enrollments");

        List<CourseRow> visibleRows = tablePage.getVisibleCourseRows();
        logger.info("Verify rows are ordered by enrollment number, smallest to largest");
        Assert.assertEquals(courseNames(visibleRows), List.of(
                "Python for Testers",
                "Selenium Grid",
                "Selenium Framework",
                "Java for Testers",
                "REST Assured",
                "Selenium with Python",
                "XPath Locators",
                "Advanced Selenium",
                "Selenium with Java"
        ), "Courses are not in numeric enrollment order");
        List<Integer> enrollments = new ArrayList<>();
        for (CourseRow row : visibleRows) {
            enrollments.add(row.enrollments());
        }
        Assert.assertEquals(enrollments, List.of(1365, 2207, 2667, 6678, 8254, 10705, 14762, 16452, 64284),
                "Enrollment numbers are not ascending. A lexicographic sort puts 10705 before 2207.");
    }

    @Test(description = "Test case 8: Sort by Course Name (alphabetical)")
    public void sortByCourseNameTest() {
        logger.info("Open the table page and sort by Course Name");
        TablePage tablePage = openTablePage();
        tablePage.selectSortBy("Course Name");

        logger.info("Verify visible rows are ordered A to Z by course name");
        Assert.assertEquals(courseNames(tablePage.getVisibleCourseRows()), List.of(
                "Advanced Selenium",
                "Java for Testers",
                "Python for Testers",
                "REST Assured",
                "Selenium Framework",
                "Selenium Grid",
                "Selenium with Java",
                "Selenium with Python",
                "XPath Locators"
        ), "Courses are not in alphabetical order");

        logger.info("Filter to Java and verify the visible rows stay in alphabetical order");
        tablePage.selectLanguage("Java");
        Assert.assertEquals(courseNames(tablePage.getVisibleCourseRows()), List.of(
                "Advanced Selenium",
                "Java for Testers",
                "REST Assured",
                "Selenium Framework",
                "Selenium Grid",
                "Selenium with Java"
        ), "Alphabetical order was not reapplied after the language filter changed");
    }

    private TablePage openTablePage() {
        TablePage tablePage = new TablePage(driver);
        tablePage.visit();
        return tablePage;
    }

    private static List<String> courseNames(List<CourseRow> rows) {
        List<String> names = new ArrayList<>();
        for (CourseRow row : rows) {
            names.add(row.courseName());
        }
        return names;
    }
}
