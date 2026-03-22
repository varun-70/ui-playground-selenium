package com.uiplayground.base;

import com.uiplayground.constants.Platform;
import io.appium.java_client.AppiumDriver;
import org.testng.ITestResult;

public abstract class MobileBaseTest extends BaseTest {

    /** Direct field access for mobile-only tests. */
    protected AppiumDriver mobileDriver;

    @Override
    protected void initDrivers(ITestResult result) {
        Platform platform = DriverSetup.resolvePlatform();
        mobileDriver = new DriverSetup().setupMobileDriver(platform);
    }

    @Override
    protected void quitDrivers() {
        DriverManager.getInstance().removeMobileDriver();
        mobileDriver = null;
    }

    // captureWebSessionId() returns null (inherited default) — correct, no web driver here.
}