package com.practicetestautomation.pageobjects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ExceptionsPage extends BasePage {

    public ExceptionsPage(WebDriver driver){
        super(driver);
    }
    public void visit(){
        super.visit("https://practicetestautomation.com/practice-test-exceptions/");
    }

    private By addButtonLocator = By.id("add_btn");
    private By saveButtonRow2Locator = By.xpath("//div[@id='row2']//button[@id='save_btn']");
    private By editButtonLocator = By.xpath("//button[@id='edit_btn']");
    private By instructionsTextLocator = By.id("instructions");
    private By row2InputLocator = By.xpath("//div[@id='row2']//input[@type='text']");
    private By confirmationLocator = By.id("confirmation");
    private By row1InputLocator = By.xpath("//div[@id='row1']//input[@type='text']");
    private By saveButtonLoc = By.xpath("//button[@id='save_btn']");


    public void clickAddButton(){
        driver.findElement(addButtonLocator).click();
    }

    public boolean isRowTwoDisplayedAfterWait(){
        return super.waitForIsDisplayed(row2InputLocator);
    }
    public boolean isRowOneDisplayedAfterWait(){
        return super.waitForIsDisplayed(row1InputLocator);
    }

    public void clickSaveButtonRow2(){
        driver.findElement(saveButtonRow2Locator).click();
    }
    public String getSuccessMessage(){
        return waitForElement(confirmationLocator).getText();
    }
    public void clickEditButton(){
        waitForElementToBeClickable(editButtonLocator).click();
    }
    public void enterTextInRowOne(String str){
        WebElement row1Input =  driver.findElement(row1InputLocator);
        row1Input.clear();
        row1Input.sendKeys(str);
    }
    public void enterTextInRowTwo(String str){
        WebElement row2Input =  driver.findElement(row2InputLocator);
        row2Input.clear();
        row2Input.sendKeys(str);

    }
    public void clickSaveButton(){
        driver.findElement(saveButtonLoc).click();
    }
    public boolean isInstructionsDisplayed(){
        return isDisplayed(instructionsTextLocator);
    }

    public boolean isInstructionsNoLongerDisplayedAfterWait(){
        return super.waitForInvisibilityOfElement(editButtonLocator);
    }






}
