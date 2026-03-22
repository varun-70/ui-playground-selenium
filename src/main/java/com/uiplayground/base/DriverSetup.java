package com.uiplayground.base;

import com.uiplayground.config.MobileConfig;
import com.uiplayground.constants.Browser;
import com.uiplayground.constants.Platform;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class DriverSetup {

    // ── Web ──────────────────────────────────────────────────────────────────

    public WebDriver setupWebDriver(Browser browser, String testName) {
        DriverManager dm = DriverManager.getInstance();
        dm.setWebDriver(browser, testName);

        WebDriver wd = dm.getWebDriver();
        wd.manage().window().setSize(new Dimension(1920, 1080));
        wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return wd;
    }

    // ── Mobile ───────────────────────────────────────────────────────────────

    public AppiumDriver setupMobileDriver(Platform platform) {
        MobileConfig config = buildMobileConfig();
        DriverManager dm = DriverManager.getInstance();
        dm.setMobileDriver(platform, config);
        return dm.getMobileDriver();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Reads mobile config from system properties so values can be supplied
     * via Maven/CLI: -Dappium.url=... -Ddevice.name=... etc.
     */
    private MobileConfig buildMobileConfig() {
        return new MobileConfig(
                System.getProperty("appium.url",        "http://localhost:4723"),
                System.getProperty("device.name",       "emulator-5554"),
                System.getProperty("platform.version",  "13.0"),
                System.getProperty("app.path",          "")
        );
    }

    /** Convenience: resolve Browser from the -Dbrowser system property. */
    public static Browser resolveBrowser() {
        return Browser.valueOf(
                System.getProperty("browser", "CHROME").toUpperCase());
    }

    /** Convenience: resolve Platform from the -Dplatform system property. */
    public static Platform resolvePlatform() {
        return Platform.valueOf(
                System.getProperty("platform", "ANDROID").toUpperCase());
    }
}