package com.practicetestautomation.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class TablePage extends BasePage {

    private By coursesTableLocator = By.id("courses_table");
    private By courseRowLocator = By.cssSelector("#courses_table tbody tr");
    private By noDataLocator = By.id("noData");
    private By resetButtonLocator = By.id("resetFilters");
    private By sortByLocator = By.id("sortBy");
    private By enrollDropdownLocator = By.id("enrollDropdown");
    private By enrollDropdownButtonLocator = By.cssSelector("#enrollDropdown .dropdown-button");
    private By enrollDropdownLabelLocator = By.cssSelector("#enrollDropdown .dropdown-label");

    public TablePage(WebDriver driver) {
        super(driver);
    }

    public void visit() {
        super.visit("https://practicetestautomation.com/practice-test-table/");
        waitForElement(coursesTableLocator);
    }

    public void selectLanguage(String language) {
        waitForElementToBeClickable(languageRadio(language)).click();
    }

    public String getSelectedLanguage() {
        return driver.findElement(By.cssSelector("input[name='lang']:checked")).getAttribute("value");
    }

    public void setLevelSelected(String level, boolean selected) {
        WebElement checkbox = waitForElementToBeClickable(levelCheckbox(level));
        if (checkbox.isSelected() != selected) {
            checkbox.click();
        }
    }

    public boolean isLevelSelected(String level) {
        return driver.findElement(levelCheckbox(level)).isSelected();
    }

    public void selectMinEnrollments(String dataValue) {
        waitForElementToBeClickable(enrollDropdownButtonLocator).click();
        By option = By.cssSelector("#enrollDropdown li[role='option'][data-value='" + dataValue + "']");
        waitForElementToBeClickable(option).click();
    }

    public String getMinEnrollmentsLabel() {
        return waitForElement(enrollDropdownLabelLocator).getText().trim();
    }

    public String getMinEnrollmentsValue() {
        return driver.findElement(enrollDropdownLocator).getAttribute("data-value");
    }

    public void selectSortBy(String visibleText) {
        new Select(waitForElement(sortByLocator)).selectByVisibleText(visibleText);
    }

    public boolean isResetButtonDisplayed() {
        return isDisplayed(resetButtonLocator);
    }

    public void clickReset() {
        waitForElementToBeClickable(resetButtonLocator).click();
    }

    public boolean isNoMatchingCoursesDisplayed() {
        return isDisplayed(noDataLocator);
    }

    public String getNoMatchingCoursesText() {
        return waitForElement(noDataLocator).getText().trim();
    }

    public boolean isCoursesTableDisplayed() {
        return isDisplayed(coursesTableLocator);
    }

    public List<CourseRow> getVisibleCourseRows() {
        List<CourseRow> courses = new ArrayList<>();
        for (WebElement row : driver.findElements(courseRowLocator)) {
            if (!row.isDisplayed()) {
                continue;
            }
            courses.add(new CourseRow(
                    cellText(row, "id"),
                    cellText(row, "course"),
                    cellText(row, "language"),
                    cellText(row, "level"),
                    parseEnrollments(cellText(row, "enrollments"))
            ));
        }
        return courses;
    }

    private By languageRadio(String language) {
        return By.cssSelector("input[name='lang'][value='" + language + "']");
    }

    private By levelCheckbox(String level) {
        return By.cssSelector("input[name='level'][value='" + level + "']");
    }

    private String cellText(WebElement row, String dataCol) {
        return row.findElement(By.cssSelector("td[data-col='" + dataCol + "']")).getText().trim();
    }

    private int parseEnrollments(String text) {
        String digits = text.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) {
            throw new IllegalStateException("Enrollment cell is not numeric: " + text);
        }
        return Integer.parseInt(digits);
    }

    public record CourseRow(String id, String courseName, String language, String level, int enrollments) {
    }
}
