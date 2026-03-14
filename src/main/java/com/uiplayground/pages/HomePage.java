package com.uiplayground.pages;

import com.uiplayground.pages.component.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {
    private final By dynamicIdButton = By.xpath("//a[.='Dynamic ID']");
    private final By ajaxDataButton = By.xpath("//a[.='AJAX Data']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Navigate to Home Page")
    public HomePage navigateToHome() {
        driver.get("http://uitestingplayground.com/");
        return this;
    }

    @Step("Click on Dynamic ID button")
    public HomePage clickOnDynamicIdButton() {
        pageElement.click(dynamicIdButton);
        return this;
    }

    @Step("Click on AJAX Data button")
    public HomePage clickOnAjaxDataButton() {
        pageElement.click(ajaxDataButton);
        return this;
    }
}
