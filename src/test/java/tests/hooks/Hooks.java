package tests.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import framework.driver.DriverFactory;
import framework.utils.ScreenshotUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    @Before("@UI")
    public void setUp(Scenario scenario) {
        logger.info("========== Starting Scenario: {} ==========", scenario.getName());
        DriverFactory.initializeDriver();
    }

    @After("@UI")
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.error("Scenario FAILED: {}", scenario.getName());
            String screenshotPath = ScreenshotUtils.takeScreenshot(scenario.getName());
            logger.info("Screenshot captured at: {}", screenshotPath);
        } else {
            logger.info("Scenario PASSED: {}", scenario.getName());
        }
        DriverFactory.quitDriver();
        logger.info("========== Scenario Completed: {} ==========", scenario.getName());
    }
}
