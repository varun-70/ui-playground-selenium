package com.uiplayground.base;

import com.uiplayground.constants.Browser;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class BaseTest {
    public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @BeforeSuite(alwaysRun = true)
    public void setUp() {
    }

    @BeforeMethod(description = "Launch Driver")
    public void launchDriver(ITestResult result) {
        // Video recording on Grid is handled automatically by the video sidecar
        // container via the se:recordVideo capability set in DriverManager.
        // No Java-side recorder needed here.
        driver.set(new DriverSetup().setupDriver(Browser.CHROME, result.getTestName()));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    @AfterSuite
    public void globalTearDown() {
    }
}