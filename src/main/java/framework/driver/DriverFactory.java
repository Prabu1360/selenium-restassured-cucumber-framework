package framework.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import framework.config.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver initializeDriver() {
        String browser = ConfigReader.getBrowser().toLowerCase();
        logger.info("Initializing WebDriver for browser: {}", browser);

        WebDriver webDriver = switch (browser) {
            case "chrome" -> initializeChrome();
            case "firefox" -> initializeFirefox();
            case "edge" -> initializeEdge();
            default -> throw new IllegalArgumentException("Browser not supported: " + browser);
        };

        driver.set(webDriver);
        webDriver.manage().window().maximize();
        logger.info("WebDriver initialized and window maximized");
        return webDriver;
    }

    private static WebDriver initializeChrome() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (ConfigReader.isHeadlessMode()) {
            options.addArguments("--headless=new");
            logger.debug("Chrome launched in headless mode");
        }

        // For Docker/CI environments - use Chromium
        String chromiumPath = "/usr/bin/chromium-browser";
        java.io.File chromiumFile = new java.io.File(chromiumPath);
        if (chromiumFile.exists()) {
            options.setBinary(chromiumPath);
            logger.debug("Using Chromium from: {}", chromiumPath);
        }

        options.addArguments(
                "--disable-blink-features=AutomationControlled",
                "--disable-web-resources-blocking",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu"
        );

        return new ChromeDriver(options);
    }

    private static WebDriver initializeFirefox() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (ConfigReader.isHeadlessMode()) {
            options.addArguments("--headless");
            logger.debug("Firefox launched in headless mode");
        }

        return new FirefoxDriver(options);
    }

    private static WebDriver initializeEdge() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();

        if (ConfigReader.isHeadlessMode()) {
            options.addArguments("--headless=new");
            logger.debug("Edge launched in headless mode");
        }

        return new EdgeDriver(options);
    }

    public static WebDriver getDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver == null) {
            throw new RuntimeException("WebDriver not initialized. Call initializeDriver() first.");
        }
        return webDriver;
    }

    public static void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            webDriver.quit();
            driver.remove();
            logger.info("WebDriver closed and ThreadLocal cleared");
        }
    }
}
