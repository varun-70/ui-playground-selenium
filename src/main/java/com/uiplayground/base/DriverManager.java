package com.uiplayground.base;

import com.uiplayground.config.MobileConfig;
import com.uiplayground.constants.Browser;
import com.uiplayground.constants.Execution;
import com.uiplayground.constants.Platform;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverManager {

    public static DriverManager instance;

    // Separate slots — a thread can hold BOTH when running Combined tests
    private static final ThreadLocal<WebDriver>   webDriver    = new ThreadLocal<>();
    private static final ThreadLocal<AppiumDriver> mobileDriver = new ThreadLocal<>();

    public static synchronized DriverManager getInstance() {
        if (instance == null) {
            instance = new DriverManager();
        }
        return instance;
    }

    // ─────────────────────────────────────────────
    // WEB
    // ─────────────────────────────────────────────

    public void setWebDriver(Browser browser, String testName) {
        String executionEnv = System.getProperty("execution", "LOCAL");
        WebDriver wd = executionEnv.equalsIgnoreCase(Execution.GRID.toString())
                ? createRemoteDriver(browser, testName)
                : createLocalDriver(browser);
        webDriver.set(wd);
    }

    public WebDriver getWebDriver() {
        return webDriver.get();
    }

    public void removeWebDriver() {
        WebDriver wd = webDriver.get();
        if (wd != null) {
            wd.quit();
            webDriver.remove();
        }
    }

    // ─────────────────────────────────────────────
    // MOBILE
    // ─────────────────────────────────────────────

    public void setMobileDriver(Platform platform, MobileConfig config) {
        mobileDriver.set(createMobileDriver(platform, config));
    }

    public AppiumDriver getMobileDriver() {
        AppiumDriver md = mobileDriver.get();
        if (md == null) {
            throw new IllegalStateException(
                    "Mobile driver is null. Did you call setMobileDriver() in @BeforeMethod?");
        }
        return md;
    }

    public void removeMobileDriver() {
        AppiumDriver md = mobileDriver.get();
        if (md != null) {
            md.quit();
            mobileDriver.remove();
        }
    }

    // ─────────────────────────────────────────────
    // COMBINED — convenience: quit both at once
    // ─────────────────────────────────────────────

    public void removeAllDrivers() {
        removeWebDriver();
        removeMobileDriver();
    }

    // ─────────────────────────────────────────────
    // PRIVATE – Web factories
    // ─────────────────────────────────────────────

    private WebDriver createLocalDriver(Browser browser) {
        return switch (browser) {
            case CHROME -> {
                WebDriverManager.chromedriver().setup();
                yield new ChromeDriver();
            }
            case FIREFOX -> {
                WebDriverManager.firefoxdriver().setup();
                yield new FirefoxDriver();
            }
            default -> throw new IllegalArgumentException("Browser not supported locally: " + browser);
        };
    }

    private WebDriver createRemoteDriver(Browser browser, String testName) {
        Capabilities capabilities;
        if (browser == Browser.CHROME) {
            ChromeOptions options = new ChromeOptions();
            options.setCapability("se:recordVideo", true);
            options.setCapability("se:videoName", testName + ".mp4");
            capabilities = options;
        } else if (browser == Browser.FIREFOX) {
            FirefoxOptions options = new FirefoxOptions();
            options.setCapability("se:recordVideo", true);
            options.setCapability("se:videoName", testName + ".mp4");
            capabilities = options;
        } else {
            throw new IllegalArgumentException("Browser not supported on Grid: " + browser);
        }
        try {
            return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Grid URL", e);
        }
    }

    // ─────────────────────────────────────────────
    // PRIVATE – Mobile factory
    // ─────────────────────────────────────────────

    private AppiumDriver createMobileDriver(Platform platform, MobileConfig config) {
        try {
            URL serverUrl = new URL(config.appiumServerUrl());
            return switch (platform) {
                case ANDROID -> {
                    UiAutomator2Options opts = new UiAutomator2Options()
                            .setDeviceName(config.deviceName())
                            .setPlatformVersion(config.platformVersion())
                            .setApp(config.appPath())
                            .setNoReset(false);
                    yield new AndroidDriver(serverUrl, opts);
                }
                case IOS -> {
                    XCUITestOptions opts = new XCUITestOptions()
                            .setDeviceName(config.deviceName())
                            .setPlatformVersion(config.platformVersion())
                            .setApp(config.appPath())
                            .setNoReset(false);
                    yield new IOSDriver(serverUrl, opts);
                }
            };
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL: " + config.appiumServerUrl(), e);
        }
    }
}