package com.uiplayground.base;

import com.uiplayground.constants.Browser;
import com.uiplayground.constants.Execution;
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
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static synchronized DriverManager getInstance() {
        if(instance == null) {
            instance = new DriverManager();
        }

        return instance;
    }

    public void setDriver(Browser browser, String testName) {
        String executionEnv = System.getProperty("execution", "LOCAL");
        WebDriver webDriver;

        if (executionEnv.equalsIgnoreCase(Execution.GRID.toString())) {
            webDriver = createRemoteDriver(browser, testName);
        } else {
            webDriver = createLocalDriver(browser);
        }

        driver.set(webDriver);
    }

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
            default -> throw new IllegalArgumentException("Browser Not Supported locally: " + browser);
        };
    }

    private WebDriver createRemoteDriver(Browser browser, String testName) {
        Capabilities capabilities;

        if (browser == Browser.CHROME) {
            ChromeOptions options = new ChromeOptions();
            // GRID VIDEO CAPABILITIES
            options.setCapability("se:recordVideo", true);
            options.setCapability("se:videoName", testName + ".mp4");
            capabilities = options;
        } else if (browser == Browser.FIREFOX) {
            FirefoxOptions options = new FirefoxOptions();
            options.setCapability("se:recordVideo", true);
            options.setCapability("se:videoName", testName + ".mp4");
            capabilities = options;
        } else {
            throw new IllegalArgumentException("Browser Not Supported on Grid: " + browser);
        }

        try {
            // Replace with your actual Docker Grid URL
            return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
            // Grid 4 still accepts /wd/hub for backwards compatibility, so this works fine.
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Grid URL", e);
        }
    }

    public WebDriver getDriver() {
        return driver.get();
    }

    public void removeDriver() {
        driver.get().quit();
        driver.remove();
    }
}
