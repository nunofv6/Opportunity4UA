package ua.tqs.opportunity4ua.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By email = By.cssSelector("[data-testid='login-email']");
    private final By password = By.cssSelector("[data-testid='login-password']");
    private final By submit = By.cssSelector("[data-testid='login-submit']");
    private final By error = By.cssSelector("[data-testid='login-error']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(submit));
    }

    public void login(String emailText, String passwordText) {
        driver.findElement(email).clear();
        driver.findElement(email).sendKeys(emailText);

        driver.findElement(password).clear();
        driver.findElement(password).sendKeys(passwordText);

        driver.findElement(submit).click();
    }

    public String getErrorText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(error)).getText();
    }
}
