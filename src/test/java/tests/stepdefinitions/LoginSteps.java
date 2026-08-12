package tests.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import framework.config.ConfigReader;
import framework.pages.LoginPage;
import framework.pages.HomePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.*;

public class LoginSteps {
    private static final Logger logger = LoggerFactory.getLogger(LoginSteps.class);
    private LoginPage loginPage;
    private HomePage homePage;

    @Given("User navigates to the login page")
    public void userNavigatesToLoginPage() {
        logger.info("User navigating to login page");
        loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
    }

    @When("User enters username as {string}")
    public void userEntersUsername(String username) {
        logger.info("User entering username: {}", username);
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        loginPage.enterUsername(username);
    }

    @When("User enters password as {string}")
    public void userEntersPassword(String password) {
        logger.info("User entering password");
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        loginPage.enterPassword(password);
    }

    @When("User clicks the login button")
    public void userClicksLoginButton() {
        logger.info("User clicking login button");
        if (loginPage == null) {
            throw new IllegalStateException("LoginPage not initialized. Execute 'User navigates to the login page' step first.");
        }
        loginPage.clickLoginButton();
    }

    @When("User login with credentials {string} and {string}")
    public void userLoginWithCredentials(String username, String password) {
        logger.info("User logging in with credentials");
        loginPage = new LoginPage();
        loginPage.loginWithCredentials(username, password);
    }

    @When("User logs in with demo credentials")
    public void userLogsInWithDemoCredentials() {
        logger.info("User logging in with demo credentials from config");
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        loginPage.loginWithCredentials(ConfigReader.getUiUsername(), ConfigReader.getUiPassword());
    }

    @Then("User should see an error message")
    public void userShouldSeeErrorMessage() {
        logger.info("Validating error message is displayed");
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
    }

    @Then("Error message should contain {string}")
    public void errorMessageShouldContain(String expectedMessage) {
        logger.info("Validating error message contains: {}", expectedMessage);
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        String actualMessage = loginPage.getErrorMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Error message should contain: " + expectedMessage);
    }

    @Then("User should be successfully logged in")
    public void userShouldBeSuccessfullyLoggedIn() {
        logger.info("Validating successful login");
        homePage = new HomePage();
        assertTrue(homePage.isOnProductsPage(), "User should land on the products page after login");
    }

    @Then("User should remain on login page")
    public void userShouldRemainOnLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        assertTrue(loginPage.getCurrentUrl().contains("/login"), "User should remain on login page");
    }

    @Then("User should be redirected from login page")
    public void userShouldBeRedirectedFromLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        assertFalse(loginPage.getCurrentUrl().contains("/login"), "User should be redirected away from login page");
    }

    @Then("Email validation message should be {string}")
    public void emailValidationMessageShouldBe(String expectedMessage) {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        assertEquals(expectedMessage, loginPage.getEmailValidationMessage(), "Email validation message mismatch");
    }

    @Then("Password validation message should be {string}")
    public void passwordValidationMessageShouldBe(String expectedMessage) {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        assertEquals(expectedMessage, loginPage.getPasswordValidationMessage(), "Password validation message mismatch");
    }
}
