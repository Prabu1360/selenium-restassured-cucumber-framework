package framework.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import framework.config.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ApiClient {
    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
    private final String baseUrl;
    private RequestSpecification requestSpec;

    public ApiClient() {
        this.baseUrl = ConfigReader.getApiBaseUrl();
        initializeRequestSpec();
    }

    private void initializeRequestSpec() {
        this.requestSpec = RestAssured.given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .accept("application/json");
        logger.info("RequestSpec initialized with base URL: {}", baseUrl);
    }

    public ApiClient withHeaders(Map<String, String> headers) {
        logger.debug("Adding headers to request: {}", headers.keySet());
        this.requestSpec = requestSpec.headers(headers);
        return this;
    }

    public ApiClient withHeader(String key, String value) {
        logger.debug("Adding header - {}: {}", key, value);
        this.requestSpec = requestSpec.header(key, value);
        return this;
    }

    public ApiClient withQueryParams(Map<String, String> params) {
        logger.debug("Adding query parameters: {}", params.keySet());
        this.requestSpec = requestSpec.queryParams(params);
        return this;
    }

    public ApiClient withQueryParam(String key, String value) {
        logger.debug("Adding query parameter - {}: {}", key, value);
        this.requestSpec = requestSpec.queryParam(key, value);
        return this;
    }

    public ApiClient withPathParam(String key, String value) {
        logger.debug("Adding path parameter - {}: {}", key, value);
        this.requestSpec = requestSpec.pathParam(key, value);
        return this;
    }

    public ApiClient withPathParams(Map<String, String> params) {
        logger.debug("Adding path parameters: {}", params.keySet());
        this.requestSpec = requestSpec.pathParams(params);
        return this;
    }

    public ApiClient withBody(Object body) {
        logger.debug("Adding request body");
        this.requestSpec = requestSpec.body(body);
        return this;
    }

    public ApiClient withBody(String jsonBody) {
        logger.debug("Adding JSON request body");
        this.requestSpec = requestSpec.body(jsonBody);
        return this;
    }

    public ApiClient withBearerToken(String token) {
        logger.debug("Adding Bearer token authorization");
        this.requestSpec = requestSpec.header("Authorization", "Bearer " + token);
        return this;
    }

    public ApiClient withBasicAuth(String username, String password) {
        logger.debug("Adding Basic authentication");
        this.requestSpec = requestSpec.auth().basic(username, password);
        return this;
    }

    public Response sendGetRequest(String endpoint) {
        logger.info("Sending GET request to: {}", endpoint);
        Response response = requestSpec.when().get(endpoint);
        logResponseDetails(response);
        resetRequestSpec();
        return response;
    }

    public Response sendPostRequest(String endpoint) {
        logger.info("Sending POST request to: {}", endpoint);
        Response response = requestSpec.when().post(endpoint);
        logResponseDetails(response);
        resetRequestSpec();
        return response;
    }

    public Response sendPutRequest(String endpoint) {
        logger.info("Sending PUT request to: {}", endpoint);
        Response response = requestSpec.when().put(endpoint);
        logResponseDetails(response);
        resetRequestSpec();
        return response;
    }

    public Response sendPatchRequest(String endpoint) {
        logger.info("Sending PATCH request to: {}", endpoint);
        Response response = requestSpec.when().patch(endpoint);
        logResponseDetails(response);
        resetRequestSpec();
        return response;
    }

    public Response sendDeleteRequest(String endpoint) {
        logger.info("Sending DELETE request to: {}", endpoint);
        Response response = requestSpec.when().delete(endpoint);
        logResponseDetails(response);
        resetRequestSpec();
        return response;
    }

    public Response login(String email, String password) {
        logger.info("Sending EventHub login request for email: {}", email);
        Map<String, String> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("password", password);
        return withBody(payload).sendPostRequest(ApiEndpoints.LOGIN_ENDPOINT);
    }

    public Response getBookings() {
        logger.info("Fetching bookings using endpoint: {}", ApiEndpoints.BOOKINGS_ENDPOINT);
        return sendGetRequest(ApiEndpoints.BOOKINGS_ENDPOINT);
    }

    public String extractToken(Response response) {
        String token = response.jsonPath().getString("token");
        if (token == null || token.isBlank()) {
            throw new AssertionError("JWT token is missing in response");
        }
        return token;
    }

    public Map<String, Object> extractJwtClaims(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token must not be null or blank");
        }
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new AssertionError("Invalid JWT token structure. Expected 3 sections but got: " + parts.length);
        }

        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        Map<String, Object> claims = io.restassured.path.json.JsonPath.from(payloadJson).getMap("$");
        if (claims == null || claims.isEmpty()) {
            throw new AssertionError("JWT payload claims are missing");
        }
        return claims;
    }

    private void logResponseDetails(Response response) {
        logger.debug("Response status code: {}", response.getStatusCode());
        logger.debug("Response content-type: {}", response.getContentType());
    }

    private void resetRequestSpec() {
        initializeRequestSpec();
    }

    public void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            logger.error("Status code mismatch. Expected: {}, Actual: {}", expectedStatusCode, actualStatusCode);
            throw new AssertionError("Expected status code: " + expectedStatusCode + ", but got: " + actualStatusCode);
        }
        logger.info("Status code validation passed: {}", expectedStatusCode);
    }

    public void validateResponseBodyContains(Response response, String jsonPath, String expectedValue) {
        String actualValue = response.jsonPath().getString(jsonPath);
        if (actualValue == null || !actualValue.equals(expectedValue)) {
            logger.error("Response body validation failed. JsonPath: {}, Expected: {}, Actual: {}", jsonPath, expectedValue, actualValue);
            throw new AssertionError("Response body validation failed for path: " + jsonPath);
        }
        logger.info("Response body validation passed for jsonPath: {}", jsonPath);
    }
}
