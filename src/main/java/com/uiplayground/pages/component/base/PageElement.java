package com.uiplayground.pages.component.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class PageElement {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger log = LoggerFactory.getLogger(PageElement.class);

    public PageElement(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void click(By locator) {
        log.info("Attempting to click: {}", locator);
        try {
            // Wait uses the locator to find the element internally
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            log.debug("Successfully clicked: {}", locator);
        } catch (Exception e) {
            log.error("Failed to click on {}. Error: {}", locator, e.getMessage());
            throw e;
        }
    }

    public void sendKeys(By locator, String text) {
        log.info("Sending text: {} to element: {}", text, locator);
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.sendKeys(text);
            log.debug("Successfully sent text: {} to element: {}", text, locator);
        } catch (Exception e) {
            log.error("Failed to send text: {} to element: {}", text, locator);
            throw e;
        }
    }

    public String getText(By locator) {
        log.info("Getting text from element: {}", locator);
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            String elementText = element.getText();
            log.debug("Text from element: {}", elementText);
            return elementText;
        } catch (Exception e) {
            log.error("Failed to get text from element: {}", locator);
            throw e;
        }
    }

    public void waitForVisibility(By locator) {
        log.info("Waiting for element to appear: {}", locator);
        try {
            WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            log.debug("Element appeared: {}", locator);
        } catch (Exception e) {
            log.error("Element still not visible: {}", locator);
            throw e;
        }
    }

    public void waitForInvisibility(By locator) {
        log.info("Waiting for element to disappear: {}", locator);
        try {
            Boolean elementInvisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            log.debug("Element disappeared: {}", locator);
        } catch (Exception e) {
            log.error("Element still visible: {}", locator);
            throw e;
        }

    }
}
