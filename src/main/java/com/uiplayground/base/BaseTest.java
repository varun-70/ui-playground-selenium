package com.uiplayground.base;

import com.uiplayground.listeners.RetryAnalyzer;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class BaseTest {

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @BeforeSuite(alwaysRun = true)
    public void setUp() { /* global pre-suite hooks */ }

    @BeforeMethod(alwaysRun = true, description = "Launch drivers")
    public final void launchDrivers(ITestResult result) {
        initDrivers(result);
    }

    @AfterMethod(alwaysRun = true)
    public final void tearDown(ITestResult result) {
        // Capture the Grid session ID BEFORE quit() invalidates it.
        // Subclasses that own a web driver override this hook.
        String sessionId = captureWebSessionId();

        quitDrivers();

        attachVideoIfNeeded(result, sessionId);
    }

    @AfterSuite(alwaysRun = true)
    public void globalTearDown() { /* global post-suite hooks */ }

    // ── Abstract hooks ────────────────────────────────────────────────────────

    /** Called in @BeforeMethod — subclasses create the driver(s) they need. */
    protected abstract void initDrivers(ITestResult result);

    /** Called in @AfterMethod — subclasses quit the driver(s) they own. */
    protected abstract void quitDrivers();

    /**
     * Override in web-aware subclasses to return the RemoteWebDriver session ID
     * BEFORE quit() is called. Return {@code null} if there is no web driver.
     */
    protected String captureWebSessionId() { return null; }

    // ── Video attachment ──────────────────────────────────────────────────────

    private void attachVideoIfNeeded(ITestResult result, String sessionId) {
        boolean isGrid = System.getProperty("execution", "LOCAL")
                .equalsIgnoreCase("GRID");
        if (!isGrid || sessionId == null) return;

        RetryAnalyzer analyzer =
                (RetryAnalyzer) result.getMethod().getRetryAnalyzer(result);
        boolean wasRetried = analyzer != null && analyzer.getCount() > 0;

        if (result.getStatus() == ITestResult.FAILURE && wasRetried) {
            attachGridVideo(sessionId, result.getName());
        }
    }

    private void attachGridVideo(String sessionId, String testName) {
        File video = new File("./videos/" + sessionId + ".mp4");

        // Poll up to 15 s for the sidecar's ffmpeg to finalise the file.
        long deadline = System.currentTimeMillis() + 15_000;
        while (!video.exists() && System.currentTimeMillis() < deadline) {
            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
        }

        if (!video.exists()) {
            System.err.printf("[Video] File not found after 15 s for session: %s%n", sessionId);
            return;
        }

        try {
            Allure.addAttachment(
                    "Retry Video: " + testName,
                    "video/mp4",
                    Files.newInputStream(video.toPath()),
                    ".mp4");
        } catch (IOException e) {
            System.err.printf("[Video] Failed to attach video for session %s: %s%n",
                    sessionId, e.getMessage());
        }
    }
}