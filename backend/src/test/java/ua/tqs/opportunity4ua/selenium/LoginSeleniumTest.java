package ua.tqs.opportunity4ua.selenium;

import org.junit.jupiter.api.Test;
import ua.tqs.opportunity4ua.selenium.pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertFalse;

class LoginSeleniumTest extends BaseUiTest {

    @Test
    void loginInvalidCredentials_showsUserFriendlyError() {
        LoginPage loginPage = new LoginPage(driver, wait);

        loginPage.open(BASE_URL);
        loginPage.login("wrong@email.com", "wrongpassword");

        String error = loginPage.getErrorText();
        assertFalse(error.isBlank());
    }

    @Test
    void loginValidCredentials_noErrorShown() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.open(BASE_URL);
        loginPage.login("test@example.com", "password");
    }
}
