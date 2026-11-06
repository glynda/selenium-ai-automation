package com.practicetestautomation.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TablePage extends BasePage {

    private static final String PAGE_URL = "https://practicetestautomation.com/practice-test-table/";
    private static final Pattern ENROLLMENT_SEPARATORS = Pattern.compile("[,\\s]");

    private static final By COURSES_TABLE_LOCATOR = By.id("courses_table");
    private static final By COURSE_ROW_LOCATOR = By.cssSelector("#courses_table > tbody > tr");
    private static final By NO_DATA_LOCATOR = By.id("noData");
    private static final By RESET_BUTTON_LOCATOR = By.id("resetFilters");
    private static final By SORT_BY_LOCATOR = By.id("sortBy");
    private static final By SELECTED_LANGUAGE_LOCATOR = By.cssSelector("input[type='radio'][name='lang']:checked");
    private static final By ENROLL_DROPDOWN_LOCATOR = By.id("enrollDropdown");
    private static final By ENROLL_DROPDOWN_BUTTON_LOCATOR = By.cssSelector("#enrollDropdown [role='button']");
    private static final By ENROLL_DROPDOWN_LABEL_LOCATOR = By.cssSelector("#enrollDropdown .dropdown-label");

    public TablePage(WebDriver driver) {
        super(driver);
    }

    public void visit() {
        super.visit(PAGE_URL);
        waitForElement(COURSES_TABLE_LOCATOR);
    }

    public void selectLanguage(Language language) {
        WebElement radio = waitForElementToBeClickable(language.locator());
        if (!radio.isSelected()) {
            radio.click();
        }
        wait.until(ExpectedConditions.elementToBeSelected(language.locator()));
    }

    public Language getSelectedLanguage() {
        String value = waitForElement(SELECTED_LANGUAGE_LOCATOR).getDomProperty("value");
        return Language.fromValue(value);
    }

    public void setLevelSelected(Level level, boolean selected) {
        WebElement checkbox = waitForElementToBeClickable(level.locator());
        if (checkbox.isSelected() != selected) {
            checkbox.click();
        }
        wait.until(ExpectedConditions.elementSelectionStateToBe(level.locator(), selected));
    }

    public boolean isLevelSelected(Level level) {
        return waitForElement(level.locator()).isSelected();
    }

    public void selectMinEnrollments(MinEnrollments threshold) {
        if (threshold.dataValue().equals(readMinEnrollmentsValue())) {
            wait.until(ExpectedConditions.textToBe(ENROLL_DROPDOWN_LABEL_LOCATOR, threshold.label()));
            return;
        }
        if (!"true".equals(waitForElement(ENROLL_DROPDOWN_BUTTON_LOCATOR).getDomAttribute("aria-expanded"))) {
            waitForElementToBeClickable(ENROLL_DROPDOWN_BUTTON_LOCATOR).click();
            wait.until(ExpectedConditions.attributeToBe(ENROLL_DROPDOWN_BUTTON_LOCATOR, "aria-expanded", "true"));
        }
        waitForElementToBeClickable(threshold.optionLocator()).click();
        wait.until(ExpectedConditions.attributeToBe(threshold.optionLocator(), "aria-selected", "true"));
        wait.until(ExpectedConditions.attributeToBe(ENROLL_DROPDOWN_LOCATOR, "data-value", threshold.dataValue()));
        wait.until(ExpectedConditions.textToBe(ENROLL_DROPDOWN_LABEL_LOCATOR, threshold.label()));
    }

    public MinEnrollments getSelectedMinEnrollments() {
        return MinEnrollments.fromDataValue(readMinEnrollmentsValue());
    }

    public String getMinEnrollmentsLabel() {
        return waitForElement(ENROLL_DROPDOWN_LABEL_LOCATOR).getText().trim();
    }

    public void selectSortBy(SortBy sort) {
        Select select = new Select(waitForElement(SORT_BY_LOCATOR));
        if (!sort.optionValue().equals(select.getFirstSelectedOption().getDomAttribute("value"))) {
            select.selectByValue(sort.optionValue());
        }
        wait.until(ExpectedConditions.attributeToBe(SORT_BY_LOCATOR, "value", sort.optionValue()));
    }

    public boolean isResetButtonDisplayedAfterWait() {
        return waitForIsDisplayed(RESET_BUTTON_LOCATOR);
    }

    public boolean isResetButtonNoLongerDisplayedAfterWait() {
        return waitForInvisibilityOfElement(RESET_BUTTON_LOCATOR);
    }

    public void clickReset() {
        waitForElementToBeClickable(RESET_BUTTON_LOCATOR).click();
        wait.until(ExpectedConditions.elementToBeSelected(Language.ANY.locator()));
        for (Level level : Level.values()) {
            wait.until(ExpectedConditions.elementToBeSelected(level.locator()));
        }
        wait.until(ExpectedConditions.attributeToBe(ENROLL_DROPDOWN_LOCATOR, "data-value", MinEnrollments.ANY.dataValue()));
        wait.until(ExpectedConditions.textToBe(ENROLL_DROPDOWN_LABEL_LOCATOR, MinEnrollments.ANY.label()));
        if (!isResetButtonNoLongerDisplayedAfterWait()) {
            throw new IllegalStateException("Reset button stayed visible after reset");
        }
        if (!isCoursesTableDisplayedAfterWait()) {
            throw new IllegalStateException("Courses table stayed hidden after reset");
        }
    }

    public boolean isNoMatchingCoursesDisplayedAfterWait() {
        if (!waitForIsDisplayed(NO_DATA_LOCATOR)) {
            return false;
        }
        return waitForInvisibilityOfElement(COURSES_TABLE_LOCATOR);
    }

    public boolean isCoursesTableDisplayedAfterWait() {
        if (!waitForIsDisplayed(COURSES_TABLE_LOCATOR)) {
            return false;
        }
        return waitForInvisibilityOfElement(NO_DATA_LOCATOR);
    }

    public String getNoMatchingCoursesText() {
        return waitForElement(NO_DATA_LOCATOR).getText().trim();
    }

    public List<CourseRow> getVisibleCourseRows() {
        List<CourseRow> courses = new ArrayList<>();
        for (WebElement row : driver.findElements(COURSE_ROW_LOCATOR)) {
            if (!row.isDisplayed()) {
                continue;
            }
            courses.add(new CourseRow(
                    cellText(row, Column.ID),
                    cellText(row, Column.COURSE),
                    cellText(row, Column.LANGUAGE),
                    cellText(row, Column.LEVEL),
                    parseEnrollments(cellText(row, Column.ENROLLMENTS))
            ));
        }
        return List.copyOf(courses);
    }

    private String readMinEnrollmentsValue() {
        String value = waitForElement(ENROLL_DROPDOWN_LOCATOR).getDomAttribute("data-value");
        if (value == null) {
            throw new IllegalStateException("Min enrollments dropdown has no data-value");
        }
        return value;
    }

    private String cellText(WebElement row, Column column) {
        return row.findElement(column.cellLocator()).getText().trim();
    }

    private int parseEnrollments(String text) {
        String normalized = ENROLLMENT_SEPARATORS.matcher(text).replaceAll("");
        try {
            return Integer.parseInt(normalized);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Enrollment cell is not numeric: " + text, e);
        }
    }

    public enum Language {
        ANY("Any", By.xpath("//fieldset[legend[normalize-space()='Language']]//input[@type='radio'][@name='lang'][@value='Any']")),
        JAVA("Java", By.xpath("//fieldset[legend[normalize-space()='Language']]//input[@type='radio'][@name='lang'][@value='Java']")),
        PYTHON("Python", By.xpath("//fieldset[legend[normalize-space()='Language']]//input[@type='radio'][@name='lang'][@value='Python']"));

        private final String value;
        private final By locator;

        Language(String value, By locator) {
            this.value = value;
            this.locator = locator;
        }

        private By locator() {
            return locator;
        }

        private static Language fromValue(String value) {
            for (Language language : values()) {
                if (language.value.equals(value)) {
                    return language;
                }
            }
            throw new IllegalStateException("Unknown language value: " + value);
        }
    }

    public enum Level {
        BEGINNER(By.xpath("//fieldset[legend[normalize-space()='Level']]//input[@type='checkbox'][@name='level'][@value='Beginner']")),
        INTERMEDIATE(By.xpath("//fieldset[legend[normalize-space()='Level']]//input[@type='checkbox'][@name='level'][@value='Intermediate']")),
        ADVANCED(By.xpath("//fieldset[legend[normalize-space()='Level']]//input[@type='checkbox'][@name='level'][@value='Advanced']"));

        private final By locator;

        Level(By locator) {
            this.locator = locator;
        }

        private By locator() {
            return locator;
        }
    }

    public enum MinEnrollments {
        ANY("any", "Any", By.cssSelector("#enrollDropdown li[role='option'][data-value='any']")),
        AT_LEAST_5000("5000", "5,000+", By.cssSelector("#enrollDropdown li[role='option'][data-value='5000']")),
        AT_LEAST_10000("10000", "10,000+", By.cssSelector("#enrollDropdown li[role='option'][data-value='10000']")),
        AT_LEAST_50000("50000", "50,000+", By.cssSelector("#enrollDropdown li[role='option'][data-value='50000']"));

        private final String dataValue;
        private final String label;
        private final By optionLocator;

        MinEnrollments(String dataValue, String label, By optionLocator) {
            this.dataValue = dataValue;
            this.label = label;
            this.optionLocator = optionLocator;
        }

        private String dataValue() {
            return dataValue;
        }

        public String label() {
            return label;
        }

        private By optionLocator() {
            return optionLocator;
        }

        private static MinEnrollments fromDataValue(String dataValue) {
            for (MinEnrollments threshold : values()) {
                if (threshold.dataValue.equals(dataValue)) {
                    return threshold;
                }
            }
            throw new IllegalStateException("Unknown min enrollments value: " + dataValue);
        }
    }

    public enum SortBy {
        ID("col_id", "ID"),
        COURSE_NAME("col_course", "Course Name"),
        LANGUAGE("col_lang", "Language"),
        LEVEL("col_level", "Level"),
        ENROLLMENTS("col_enroll", "Enrollments");

        private final String optionValue;
        private final String label;

        SortBy(String optionValue, String label) {
            this.optionValue = optionValue;
            this.label = label;
        }

        private String optionValue() {
            return optionValue;
        }

        public String label() {
            return label;
        }
    }

    public enum Column {
        ID(By.cssSelector("td[data-col='id'][headers='col_id']")),
        COURSE(By.cssSelector("td[data-col='course'][headers='col_course']")),
        LANGUAGE(By.cssSelector("td[data-col='language'][headers='col_lang']")),
        LEVEL(By.cssSelector("td[data-col='level'][headers='col_level']")),
        ENROLLMENTS(By.cssSelector("td[data-col='enrollments'][headers='col_enroll']"));

        private final By cellLocator;

        Column(By cellLocator) {
            this.cellLocator = cellLocator;
        }

        private By cellLocator() {
            return cellLocator;
        }
    }

    public record CourseRow(String id, String courseName, String language, String level, int enrollments) {
    }
}
