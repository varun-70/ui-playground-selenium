package com.uiplayground.base;

import com.uiplayground.constants.Browser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverManager {
    public static DriverManager instance;
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static synchronized DriverManager getInstance() {
        if(instance == null) {
            instance = new DriverManager();
        }

        return instance;
    }

    public void setDriver(Browser browser) {
        switch (browser) {
            case Browser.CHROME:
                WebDriverManager.chromedriver().setup();
                driver.set(new ChromeDriver());
                break;
            case Browser.FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                driver.set(new FirefoxDriver());
                break;
            default:
                throw new IllegalArgumentException("Browser Not Found " + browser);
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
