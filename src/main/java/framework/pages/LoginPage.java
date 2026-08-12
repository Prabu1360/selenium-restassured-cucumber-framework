package framework.pages;

import org.openqa.selenium.By;
import framework.config.ConfigReader;

public class LoginPage extends BasePage {
    private static final By USERNAME_INPUT = By.xpath("//label[normalize-space()='Email']/following::input[1]");
    private static final By PASSWORD_INPUT = By.xpath("//label[normalize-space()='Password']/following::input[1]");
    private static final By LOGIN_BUTTON = By.xpath("//button[normalize-space()='Sign In']");
    private static final By INVALID_CREDENTIALS_MESSAGE = By.xpath("//*[contains(text(), 'Invalid email or password')]");
    private static final By EMAIL_VALIDATION_MESSAGE = By.xpath("//*[contains(text(), 'Enter a valid email')]");
    private static final By PASSWORD_VALIDATION_MESSAGE = By.xpath("//*[contains(text(), 'Password must be at least 6 characters')]");
    private static final By ERROR_MESSAGE = By.xpath(
            "//*[contains(text(), 'Invalid email or password') or " +
            "contains(text(), 'Enter a valid email') or " +
            "contains(text(), 'Password must be at least 6 characters')]"
    );

    public LoginPage() {
        super();
    }

    public void navigateToLoginPage() {
        navigateTo(ConfigReader.getBaseUrl());
    }

    public void enterUsername(String username) {
        type(USERNAME_INPUT, username);
    }

    public void enterPassword(String password) {
        type(PASSWORD_INPUT, password);
    }

    public void clickLoginButton() {
        click(LOGIN_BUTTON);
    }

    public void loginWithCredentials(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public String getErrorMessage() {
        waitForElementToBeVisible(ERROR_MESSAGE);
        return getText(ERROR_MESSAGE);
    }

    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(ERROR_MESSAGE);
    }

    public String getInvalidCredentialsMessage() {
        waitForElementToBeVisible(INVALID_CREDENTIALS_MESSAGE);
        return getText(INVALID_CREDENTIALS_MESSAGE);
    }

    public String getEmailValidationMessage() {
        waitForElementToBeVisible(EMAIL_VALIDATION_MESSAGE);
        return getText(EMAIL_VALIDATION_MESSAGE);
    }

    public String getPasswordValidationMessage() {
        waitForElementToBeVisible(PASSWORD_VALIDATION_MESSAGE);
        return getText(PASSWORD_VALIDATION_MESSAGE);
    }
}
