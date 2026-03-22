package com.uiplayground.base;

import com.uiplayground.constants.Browser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;

public abstract class WebBaseTest extends BaseTest {

    /** Direct field access for web-only tests: driver.findElement(…) */
    protected WebDriver driver;

    @Override
    protected void initDrivers(ITestResult result) {
        Browser browser = DriverSetup.resolveBrowser();
        driver = new DriverSetup().setupWebDriver(browser, result.getTestName());
    }

    @Override
    protected void quitDrivers() {
        DriverManager.getInstance().removeWebDriver();
        driver = null;
    }

    /** Grab session ID before the socket is closed by quit(). */
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