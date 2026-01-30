package ua.tqs.opportunity4ua.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ShopPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By pointsBalance = By.cssSelector("[data-testid='point-balance']");
    private final By success = By.cssSelector("[data-testid='reward-success']");
    private final By error = By.cssSelector("[data-testid='reward-error']");
    private final By anyRedeemButton = By.cssSelector("[data-testid^='nav-reward-'][data-testid$='-redeem']");

    public ShopPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/shop");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(pointsBalance),
                ExpectedConditions.presenceOfElementLocated(anyRedeemButton)
        ));
    }

    public String getPointsText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pointsBalance)).getText();
    }

    public void redeemItem(long itemId) {
        By redeemBtn = By.cssSelector("[data-testid='nav-reward-" + itemId + "-redeem']");
        wait.until(ExpectedConditions.elementToBeClickable(redeemBtn)).click();
    }

    public String waitSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(success)).getText();
    }

    public String waitErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(error)).getText();
    }
}
