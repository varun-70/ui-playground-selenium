package com.uiplayground.base;

import com.uiplayground.constants.Browser;
import com.uiplayground.listeners.RetryAnalyzer;
import com.uiplayground.utils.VideoRecorderUtils;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.nio.file.Files;

public class BaseTest {
    public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static ThreadLocal<VideoRecorderUtils> recorder = new ThreadLocal<>();

    @BeforeSuite (alwaysRun = true)
    public void setUp() {

    }

    @BeforeMethod (description = "Launch Driver")
    public void launchDriver(ITestResult result) {
        driver.set(new DriverSetup().setupDriver(Browser.CHROME, result.getTestName()));

        // Logic: Only start recording if this is a RETRY (retry count > 0)
        RetryAnalyzer analyzer = (RetryAnalyzer) result.getMethod().getRetryAnalyzer(result);
        if (analyzer != null && analyzer.getCount() > 0) {
            VideoRecorderUtils vru = VideoRecorderUtils.createInstance(result.getName() + "_Retry_" + analyzer.getCount());
            recorder.set(vru);
            try {
                vru.start();
            } catch (Exception e) {
                System.err.println("Could not start recording: " + e.getMessage());
            }
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        VideoRecorderUtils vru = recorder.get();

        if (vru != null) {
            try {
                vru.stop();
                File video = vru.getMovieFile();

                // Attach to Allure only if failed
                if (result.getStatus() == ITestResult.FAILURE && video != null && video.exists()) {
                    Allure.addAttachment("Retry Video: " + result.getName(), "video/mp4",
                            Files.newInputStream(video.toPath()), ".mp4");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                recorder.remove(); // Clean up thread
            }
        }

        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    @AfterSuite
    public void globalTearDown() {

    }
}
