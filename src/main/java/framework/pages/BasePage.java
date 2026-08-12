package framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import framework.config.ConfigReader;
import framework.driver.DriverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(BasePage.class);

    public BasePage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public void navigateTo(String url) {
        logger.info("Navigating to URL: {}", url);
        driver.navigate().to(url);
    }

    public void click(By locator) {
        logger.debug("Clicking element: {}", locator);
        waitForElementToBeClickable(locator).click();
    }

    public void type(By locator, String text) {
        logger.debug("Typing text '{}' into element: {}", text, locator);
        WebElement element = waitForElementToBeVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(By locator) {
        logger.debug("Getting text from element: {}", locator);
        return waitForElementToBeVisible(locator).getText();
    }

    public boolean isElementDisplayed(By locator) {
        try {
            logger.debug("Checking if element is displayed: {}", locator);
            return waitForElementToBeVisible(locator).isDisplayed();
        } catch (Exception e) {
            logger.debug("Element not displayed: {}", locator);
            return false;
        }
    }

    public boolean isElementPresent(By locator) {
        try {
            logger.debug("Checking if element is present: {}", locator);
            return !driver.findElements(locator).isEmpty();
        } catch (Exception e) {
            logger.debug("Element not present: {}", locator);
            return false;
        }
    }

    public WebElement waitForElementToBeVisible(By locator) {
        logger.debug("Waiting for element to be visible: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementToBeClickable(By locator) {
        logger.debug("Waiting for element to be clickable: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForElementToBeInvisible(By locator) {
        logger.debug("Waiting for element to be invisible: {}", locator);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public String getAttributeValue(By locator, String attributeName) {
        logger.debug("Getting attribute '{}' from element: {}", attributeName, locator);
        return waitForElementToBeVisible(locator).getAttribute(attributeName);
    }

    public void selectDropdownByValue(By locator, String value) {
        logger.debug("Selecting dropdown value '{}' from element: {}", value, locator);
        WebElement dropdown = waitForElementToBeVisible(locator);
        dropdown.findElement(By.cssSelector("option[value='" + value + "']")).click();
    }

    public void selectDropdownByText(By locator, String text) {
        logger.debug("Selecting dropdown text '{}' from element: {}", text, locator);
        WebElement dropdown = waitForElementToBeVisible(locator);
        dropdown.findElement(By.xpath(".//option[normalize-space()='" + text + "']")).click();
    }

    public String getPageTitle() {
        logger.debug("Getting page title");
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        logger.debug("Getting current URL");
        return driver.getCurrentUrl();
    }

    public void waitForUrlToNotContain(String partialUrl) {
        logger.debug("Waiting for URL to not contain: {}", partialUrl);
        wait.until(webDriver -> !webDriver.getCurrentUrl().contains(partialUrl));
    }

    public void switchToFrame(By locator) {
        logger.debug("Switching to frame: {}", locator);
        driver.switchTo().frame(waitForElementToBeVisible(locator));
    }

    public void switchToDefaultContent() {
        logger.debug("Switching to default content");
        driver.switchTo().defaultContent();
    }
}
