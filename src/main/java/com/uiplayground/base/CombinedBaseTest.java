package com.uiplayground.base;

import com.uiplayground.constants.Browser;
import com.uiplayground.constants.Platform;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;

public abstract class CombinedBaseTest extends BaseTest {

    /** Both drivers available side-by-side in combined tests. */
    protected WebDriver    driver;
    protected AppiumDriver mobileDriver;

    @Override
    protected void initDrivers(ITestResult result) {
        DriverSetup setup = new DriverSetup();

        Browser  browser  = DriverSetup.resolveBrowser();
        Platform platform = DriverSetup.resolvePlatform();

        driver       = setup.setupWebDriver(browser, result.getTestName());
        mobileDriver = setup.setupMobileDriver(platform);
    }

    @Override
    protected void quitDrivers() {
        DriverManager.getInstance().removeAllDrivers();
        driver       = null;
        mobileDriver = null;
    }

    @Override
    protected String captureWebSessionId() {
        boolean isGrid = System.getProperty("execution", "LOCAL")
                .equalsIgnoreCase("GRID");
        if (isGrid && driver instanceof RemoteWebDriver rwd) {
            return rwd.getSessionId().toString();
        }
        return null;
    }
}