package com.practicetestautomation.tests.exceptions;
import com.practicetestautomation.pageobjects.ExceptionsPage;
import com.practicetestautomation.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;

public class ExceptionsTests extends BaseTest {

    @Test
    public void noSuchElementExceptionsTest(){
//        wait = new WebDriverWait(driver,Duration.ofSeconds(6));
//        Click Add button
//        WebElement addButton =  driver.findElement(By.id("add_btn"));
//        logger.info("Click add button");
//        addButton.click();
//        By row2InputLoc = By.xpath("//div[@id='row2']//input[@type='text']");//
//        WebElement row2Input = wait.until(ExpectedConditions.visibilityOfElementLocated(row2InputLoc));
//        Verify Row 2 input field is displayed
//        String expectedConfirmationMessage = "Row 2 was added";
//        String actualConfirmationMessage = driver.findElement(By.id("confirmation")).getText();
//        logger.info("Verify Row 2 input field");
        ExceptionsPage exceptionsPage = new ExceptionsPage(driver);
        exceptionsPage.visit();
        exceptionsPage.clickAddButton();
        Assert.assertTrue(exceptionsPage.isRowTwoDisplayedAfterWait(), "Row 2 input field is not displayed");
        Assert.assertEquals(exceptionsPage.getSuccessMessage(),"Row 2 was added");

    }
    @Test
    public void timeOutExceptionTest(){
//        wait = new WebDriverWait(driver,Duration.ofSeconds(6));
//        Click Add button
//        WebElement addButton =  driver.findElement(By.id("add_btn"));
//        addButton.click();
//        By row2InputLoc = By.xpath("//div[@id='row2']//input[@type='text']");
//        logger.info("Wait until row2 input field is visible");
//        WebElement row2Input = wait.until(ExpectedConditions.visibilityOfElementLocated(row2InputLoc));
//        Verify Row 2 input field is displayed
//        WebElement confirmationMessage = driver.findElement(By.id("confirmation"));
//        String actualMessage = confirmationMessage.getText();
//        String expectedMessage = "Row 2 was added";
//        logger.info("Click add button");
//        logger.info("Verify Row 2 input field is displayed");
        ExceptionsPage exceptionsPage = new ExceptionsPage(driver);
        exceptionsPage.visit();
        exceptionsPage.clickAddButton();
        Assert.assertTrue(exceptionsPage.isRowTwoDisplayedAfterWait(), "Row 2 input field is not displayed");
        Assert.assertEquals(exceptionsPage.getSuccessMessage(),"Row 2 was added");
    }

    @Test
    public void elementNotInteractableExceptionTest(){
//        wait = new WebDriverWait(driver,Duration.ofSeconds(10));
//        Click Add button
//        WebElement addButton =  driver.findElement(By.id("add_btn"));
//        logger.info("Click add button");
//        addButton.click();
//        By row2InputLoc = By.xpath("//div[@id='row2']//input[@type='text']");
//        logger.info("Wait until row2 input field is visible");
//        WebElement row2Input = wait.until(ExpectedConditions.visibilityOfElementLocated(row2InputLoc));
//        logger.info("Verify Row 2 input field is displayed");
//        Type text into the second input field
//        By saveButtonLoc = By.xpath("//div[@id='row2']//button[@id='save_btn']");
//        logger.info("Type sampleText into row2 input field");
//        row2Input.sendKeys("sampleText");
//        Push Save button using locator By.name(“Save”)
//        WebElement saveButton = driver.findElement(saveButtonLoc);
//        Click Save button
//        logger.info("Click Save button");
//        saveButton.click();
//        Verify text saved
//        logger.info("Verify Row 2 was saved");
//        By confirmationLoc = By.id("confirmation");
//        WebElement confirmationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationLoc));
//        String actualMessage = confirmationMessage.getText();
//        String expectedMessage = "Row 2 was saved";
        ExceptionsPage exceptionsPage = new ExceptionsPage(driver);
        exceptionsPage.visit();
        exceptionsPage.clickAddButton();
        Assert.assertTrue(exceptionsPage.isRowTwoDisplayedAfterWait(), "Row 2 input field is not displayed");
        exceptionsPage.enterTextInRowTwo("sampleText");
        exceptionsPage.clickSaveButtonRow2();
        Assert.assertEquals(exceptionsPage.getSuccessMessage(),"Row 2 was saved", "message is not expected");

    }
    @Test
    public void invalidElementStateExceptionTest(){
//        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        Clear input field will not work because it is disabled
//        logger.info("Clear input field");
//        row1Input.clear();
//        By editButtonLoc = By.xpath("//button[@id='edit_btn']");
//        Push Save button using locator By.name(“Save”)
//        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(editButtonLoc));
//        Click Edit button
//        logger.info("Click Edit button");
//        editButton.click();
//        By row1InputLoc = By.xpath("//div[@id='row1']//input[@type='text']");
//        logger.info("Wait until row1 input field is visible");
//        WebElement row1Input = wait.until(ExpectedConditions.visibilityOfElementLocated(row1InputLoc));
//        logger.info("Verify Row 1 input field is displayed");
//        logger.info("Clear input field");
//        row1Input.clear();
//        logger.info("Type sampleText into input field");
//        row1Input.sendKeys("sampleText");
//        By saveButtonLoc = By.xpath("//button[@id='save_btn']");
//        WebElement saveButton = driver.findElement(saveButtonLoc);
//        Click Save button
//        logger.info("Click Save button");
//        saveButton.click();
//        Verify text changed
//        logger.info("Verify text changed");
//        By confirmationLoc = By.id("confirmation");
//        WebElement confirmationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationLoc));
//        String actualMessage = confirmationMessage.getText();
//        String expectedMessage = "Row 1 was saved";
        ExceptionsPage exceptionsPage = new ExceptionsPage(driver);
        exceptionsPage.visit();
        exceptionsPage.clickEditButton();
        Assert.assertTrue(exceptionsPage.isRowOneDisplayedAfterWait(), "Row 1 input field is not displayed");
        exceptionsPage.enterTextInRowOne("sampleText");
        exceptionsPage.clickSaveButton();
        Assert.assertEquals(exceptionsPage.getSuccessMessage(),"Row 1 was saved", "message is not expected");
    }
    @Test
    public void staleElementReferenceExceptionTest(){
//        wait = new WebDriverWait(driver,Duration.ofSeconds(10));
//        By instructionsTextLoc = By.id("instructions");
//        Find the instructions text element
//        WebElement instructionsText = driver.findElement(instructionsTextLoc);
//        logger.info("Find the instructions text element");
//        Click Add button
//        logger.info("Click add button");
//        WebElement addButton = driver.findElement(By.id("add_btn"));
//        addButton.click();
//        Verify instruction text element is no longer displayed
//        logger.info("Verify instruction text element is no longer displayed");
        ExceptionsPage exceptionsPage = new ExceptionsPage(driver);
        exceptionsPage.visit();
        Assert.assertTrue(exceptionsPage.isInstructionsDisplayed(), "Instructions text is not displayed");
        exceptionsPage.clickAddButton();
        Assert.assertTrue(exceptionsPage.isInstructionsNoLongerDisplayedAfterWait(), "Instructions text still displayed");
    }

}
