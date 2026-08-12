package framework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import framework.driver.DriverFactory;
import framework.config.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
    }

    private static String getScreenshotPath() {
        return ConfigReader.getProperty("screenshot.path", "target/screenshots");
    }

    public static String takeScreenshot(String screenshotName) {
        try {
            String screenshotPath = getScreenshotPath();
            Files.createDirectories(Paths.get(screenshotPath));

            TakesScreenshot takesScreenshot = (TakesScreenshot) DriverFactory.getDriver();
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = screenshotPath + File.separator + screenshotName + "_" + timestamp + ".png";

            Files.copy(sourceFile.toPath(), Paths.get(fileName));
            logger.info("Screenshot captured: {}", fileName);
            return fileName;
        } catch (IOException e) {
            logger.error("Failed to capture screenshot", e);
            return null;
        }
    }

    public static String takeScreenshot() {
        return takeScreenshot("screenshot");
    }

    public static byte[] getScreenshotBytes() {
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) DriverFactory.getDriver();
            return takesScreenshot.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Failed to get screenshot bytes", e);
            return null;
        }
    }
}
