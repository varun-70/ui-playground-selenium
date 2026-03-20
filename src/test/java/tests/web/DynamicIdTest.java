package tests.web;

import com.uiplayground.base.BaseTest;
import com.uiplayground.pages.mobile.HomePage;
import com.uiplayground.pages.mobile.PlaygroundExamplesPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DynamicIdTest extends BaseTest {
    @Test (description = "Verify Dynamic ID Buttons")
    public void verifyDynamicIdButton() {
        HomePage homePage = new HomePage(driver.get());
        PlaygroundExamplesPage playgroundExamplesPage = new PlaygroundExamplesPage(driver.get());

        homePage.navigateToHome()
                .clickOnDynamicIdButton();
        playgroundExamplesPage.clickOnDynamicIdButton();
        driver.get().navigate().back();
    }

    @Test (description = "Failing Test")
    public void verifyFailedTest() {
        HomePage homePage = new HomePage(driver.get());
        homePage.navigateToHome();
 
        Assert.assertEquals(1, 2);
    }

    @Test (description = "Verify AJEX data")
    public void verifyAjexData(){
        HomePage homePage = new HomePage(driver.get());
        PlaygroundExamplesPage playgroundExamplesPage = new PlaygroundExamplesPage(driver.get());

        homePage.navigateToHome()
                .clickOnAjaxDataButton();
        playgroundExamplesPage.clickOnAjaxRequestButton()
                .waitUntilLoadingIndicatorDisappears();

        homePage.clickOnAjaxDataButton();

        Assert.assertEquals(playgroundExamplesPage.getDataLoadedText(), "Data loaded with AJAX get request.");
    }
}
