package com.uiplayground.listeners;

import com.uiplayground.base.BaseTest;
import com.uiplayground.utils.ScreenshotUtil;
import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class TestFailedListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        ITestListener.super.onTestFailure(result);

        ScreenshotUtil screenshotUtil = new ScreenshotUtil(BaseTest.driver.get());
        Allure.addAttachment("Screenshot on Failure", new ByteArrayInputStream(screenshotUtil.takeScreenshot()));
    }
}
