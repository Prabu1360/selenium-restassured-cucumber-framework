package framework.config;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_PATH = "src/test/resources/config/config.properties";
    private static final String LOCAL_CONFIG_PATH = "src/test/resources/config/config.local.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            properties.load(fis);
            loadLocalOverridesIfPresent();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties from: " + CONFIG_PATH, e);
        }
    }

    private static void loadLocalOverridesIfPresent() {
        File localConfigFile = new File(LOCAL_CONFIG_PATH);
        if (!localConfigFile.exists()) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(localConfigFile)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load local config overrides from: " + LOCAL_CONFIG_PATH, e);
        }
    }

    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getUiUsername() {
        return getSensitiveProperty("ui.username", "EVENTHUB_UI_EMAIL", "UI_USERNAME");
    }

    public static String getUiPassword() {
        return getSensitiveProperty("ui.password", "EVENTHUB_UI_PASSWORD", "UI_PASSWORD");
    }

    public static String getApiBaseUrl() {
        return getProperty("api.base.url");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait", "10"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait", "15"));
    }

    public static boolean isHeadlessMode() {
        return Boolean.parseBoolean(getProperty("headless.mode", "false"));
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property not found: " + key);
        }
        return value;
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    private static String getSensitiveProperty(String propertyKey, String primaryEnvVar, String fallbackEnvVar) {
        String envValue = getEnvironmentValue(primaryEnvVar);
        if (envValue != null) {
            return envValue;
        }

        envValue = getEnvironmentValue(fallbackEnvVar);
        if (envValue != null) {
            return envValue;
        }

        return getProperty(propertyKey);
    }

    private static String getEnvironmentValue(String envVarName) {
        String value = System.getenv(envVarName);
        if (value == null || value.isBlank()) {
            return null;
        }
        return value;
    }
}
