package com.uiplayground.pages.web.pages;

import com.uiplayground.pages.web.component.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PlaygroundExamplesPage extends BasePage {
    private final By dynamicIdButton = By.xpath("//button[.=\"Button with Dynamic ID\"]");
    private final By ajaxRequestButton = By.id("ajaxButton");
    private final By loadingIndicator = By.id("spinner");
    private final By dataLoadedText = By.xpath("(//div[@id='content']/p)[1]");

    public PlaygroundExamplesPage(WebDriver driver) {
        super(driver);
    }

    public PlaygroundExamplesPage clickOnDynamicIdButton() {
        pageElement.click(dynamicIdButton);
        return this;
    }

    @Step("Click on AJAX Request button")
    public PlaygroundExamplesPage clickOnAjaxRequestButton() {
        pageElement.click(ajaxRequestButton);
        return this;
    }

    @Step("Wait until loading indicator disappears")
    public PlaygroundExamplesPage waitUntilLoadingIndicatorDisappears() {
        pageElement.waitForInvisibility(loadingIndicator);
        return this;
    }

    @Step("Get data loaded text")
    public String getDataLoadedText() {
        return pageElement.getText(dataLoadedText);
    }
}
