package com.practicetestautomation.tests;
import com.practicetestautomation.tests.login.LoginTests;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;


import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseTest {
    protected WebDriver driver;
    protected Logger logger;


    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setup(@Optional("chrome") String browser){

        logger = Logger.getLogger(LoginTests.class.getName());
        logger.setLevel(Level.INFO);

        logger.info("Running test in " + browser);
        switch (browser.toLowerCase()){
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            default:
                logger.warning("Configuration for " + browser + " is missing so, running tests in chrome by default");
                driver = new ChromeDriver();
                break;
        }
    }
    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        driver.quit();
        logger.info("Browser is closed.");
    }
}
