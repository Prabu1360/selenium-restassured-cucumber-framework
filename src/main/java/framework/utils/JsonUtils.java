package framework.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private JsonUtils() {
    }

    public static <T> T readJsonFile(String filePath, Class<T> classType) {
        try (FileReader reader = new FileReader(filePath)) {
            logger.debug("Reading JSON file: {}", filePath);
            return gson.fromJson(reader, classType);
        } catch (IOException e) {
            logger.error("Failed to read JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    public static <T> T readJsonFile(String filePath, Type type) {
        try (FileReader reader = new FileReader(filePath)) {
            logger.debug("Reading JSON file: {}", filePath);
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            logger.error("Failed to read JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    public static String toJsonString(Object object) {
        logger.debug("Converting object to JSON string");
        return gson.toJson(object);
    }

    public static <T> T fromJsonString(String jsonString, Class<T> classType) {
        logger.debug("Converting JSON string to object");
        return gson.fromJson(jsonString, classType);
    }

    public static String prettifyJson(String jsonString) {
        try {
            logger.debug("Prettifying JSON");
            JsonElement jsonElement = JsonParser.parseString(jsonString);
            return gson.toJson(jsonElement);
        } catch (Exception e) {
            logger.error("Failed to prettify JSON", e);
            return jsonString;
        }
    }
}
