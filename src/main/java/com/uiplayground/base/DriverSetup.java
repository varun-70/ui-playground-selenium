package com.uiplayground.base;

import com.uiplayground.constants.Browser;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class DriverSetup {
    WebDriver setupDriver(Browser browser) {
        DriverManager driverManager = DriverManager.getInstance();
        driverManager.setDriver(browser);
        WebDriver driver = driverManager.getDriver();
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        return driver;
    }

}
