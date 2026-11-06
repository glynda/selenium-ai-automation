package com.practicetestautomation.tests.exceptions;
import com.practicetestautomation.pageobjects.ExceptionsPage;
import com.practicetestautomation.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;

public class ExceptionsTests extends BaseTest {

    @Test
    public void noSuchElementExceptionsTest(){
        ExceptionsPage exceptionsPage = new ExceptionsPage(driver);
        exceptionsPage.visit();
        exceptionsPage.clickAddButton();
        Assert.assertTrue(exceptionsPage.isRowTwoDisplayedAfterWait(), "Row 2 input field is not displayed");
        Assert.assertEquals(exceptionsPage.getSuccessMessage(),"Row 2 was added");

    }
    @Test
    public void timeOutExceptionTest(){
        ExceptionsPage exceptionsPage = new ExceptionsPage(driver);
        exceptionsPage.visit();
        exceptionsPage.clickAddButton();
        Assert.assertTrue(exceptionsPage.isRowTwoDisplayedAfterWait(), "Row 2 input field is not displayed");
        Assert.assertEquals(exceptionsPage.getSuccessMessage(),"Row 2 was added");
    }

    @Test
    public void elementNotInteractableExceptionTest(){
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
        ExceptionsPage exceptionsPage = new ExceptionsPage(driver);
        exceptionsPage.visit();
        Assert.assertTrue(exceptionsPage.isInstructionsDisplayed(), "Instructions text is not displayed");
        exceptionsPage.clickAddButton();
        Assert.assertTrue(exceptionsPage.isInstructionsNoLongerDisplayedAfterWait(), "Instructions text still displayed");
    }

}
