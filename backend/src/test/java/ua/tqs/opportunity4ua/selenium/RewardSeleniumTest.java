package ua.tqs.opportunity4ua.selenium;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ua.tqs.opportunity4ua.selenium.pages.LoginPage;
import ua.tqs.opportunity4ua.selenium.pages.ShopPage;

class RewardRedeemSeleniumTest extends BaseUiTest {

    @Test
    void redeemReward_showsSuccessMessage() {

        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.open(BASE_URL);

        loginPage.login("test@example.com", "password");

        ShopPage shopPage = new ShopPage(driver, wait);
        shopPage.open(BASE_URL);

        shopPage.redeemItem(3L);

        String msg = shopPage.waitSuccessMessage();
        assertTrue(msg.toLowerCase().contains("successfully") || msg.contains("🎉"));
    }

    @Test
    void redeemReward_withoutEnoughPoints_showsErrorMessage() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.open(BASE_URL);

        loginPage.login("lowpoints@example.com", "password");

        ShopPage shopPage = new ShopPage(driver, wait);
        shopPage.open(BASE_URL);

        shopPage.redeemItem(3L);

        String err = shopPage.waitErrorMessage();
        assertTrue(err.toLowerCase().contains("failed"));
    }
}
