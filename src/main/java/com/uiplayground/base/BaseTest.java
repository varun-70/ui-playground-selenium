package com.uiplayground.base;

import com.uiplayground.constants.Browser;
import com.uiplayground.listeners.RetryAnalyzer;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
        WebDriver wd = driver.get();

        // Grab session ID BEFORE quit() — afterwards the session is gone
        String sessionId = null;
        boolean isGrid = System.getProperty("execution", "LOCAL").equalsIgnoreCase("GRID");
        if (isGrid && wd instanceof RemoteWebDriver remoteDriver) {
            sessionId = remoteDriver.getSessionId().toString();
        }

        if (wd != null) {
            wd.quit();
            driver.remove();
        }

        // Attach Grid video only for retried tests that failed
        RetryAnalyzer analyzer = (RetryAnalyzer) result.getMethod().getRetryAnalyzer(result);
        boolean isRetry = analyzer != null && analyzer.getCount() > 0;

        if (result.getStatus() == ITestResult.FAILURE && isRetry && sessionId != null) {
            attachGridVideo(sessionId, result.getName());
        }
    }

    private void attachGridVideo(String sessionId, String testName) {
        File video = new File("./videos/" + sessionId + ".mp4");

        // The sidecar writes and finalises the file after driver.quit().
        // Poll for up to 15s to give ffmpeg time to flush and close the file.
        int maxWaitMs = 15_000;
        int elapsed   = 0;
        while (!video.exists() && elapsed < maxWaitMs) {
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            elapsed += 500;
        }

        if (!video.exists()) {
            System.err.println("[Video] File not found after " + maxWaitMs + "ms for session: " + sessionId);
            return;
        }

        try {
            Allure.addAttachment(
                    "Retry Video: " + testName,
                    "video/mp4",
                    Files.newInputStream(video.toPath()),
                    ".mp4"
            );
        } catch (IOException e) {
            System.err.println("[Video] Failed to attach video for session " + sessionId + ": " + e.getMessage());
        }
    }

    @AfterSuite
    public void globalTearDown() {
    }
}