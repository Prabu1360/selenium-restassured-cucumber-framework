package framework.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import framework.config.ConfigReader;
import framework.driver.DriverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

public class WaitUtils {
    private static final Logger logger = LoggerFactory.getLogger(WaitUtils.class);

    private WaitUtils() {
    }

    public static void implicitWait() {
        WebDriver driver = DriverFactory.getDriver();
        int waitTime = ConfigReader.getImplicitWait();
        logger.debug("Setting implicit wait to {} seconds", waitTime);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(waitTime));
    }

    public static WebDriverWait explicitWait() {
        int waitTime = ConfigReader.getExplicitWait();
        logger.debug("Creating explicit wait of {} seconds", waitTime);
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(waitTime));
    }

    public static void hardWait(long milliseconds) {
        logger.debug("Waiting for {} milliseconds", milliseconds);
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Hard wait interrupted", e);
        }
    }

    public static void hardWaitSeconds(int seconds) {
        hardWait(seconds * 1000L);
    }
}
