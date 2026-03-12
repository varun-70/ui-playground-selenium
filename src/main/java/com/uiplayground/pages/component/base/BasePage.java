package com.uiplayground.pages.component.base;

import org.openqa.selenium.WebDriver;

public class BasePage {
    protected WebDriver driver;
    protected PageElement pageElement;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.pageElement = new PageElement(driver);
    }
}
