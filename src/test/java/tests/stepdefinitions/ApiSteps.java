package tests.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import framework.api.ApiClient;
import framework.config.ConfigReader;
import framework.utils.JsonUtils;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class ApiSteps {
    private static final Logger logger = LoggerFactory.getLogger(ApiSteps.class);
    private ApiClient apiClient;
    private Response response;
    private String jwtToken;

    @Given("API client is initialized")
    public void initializeApiClient() {
        logger.info("Initializing API client");
        apiClient = new ApiClient();
    }

    @Given("Add authorization header with token {string}")
    public void addAuthorizationHeader(String token) {
        logger.info("Adding authorization header with bearer token");
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        apiClient.withBearerToken(token);
    }

    @When("Send GET request to endpoint {string}")
    public void sendGetRequest(String endpoint) {
        logger.info("Sending GET request to endpoint: {}", endpoint);
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        response = apiClient.sendGetRequest(endpoint);
    }

    @When("Send POST request to endpoint {string} with body:")
    public void sendPostRequestWithBody(String endpoint, String body) {
        logger.info("Sending POST request to endpoint: {}", endpoint);
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        response = apiClient.withBody(body).sendPostRequest(endpoint);
    }

    @When("Send PUT request to endpoint {string} with body:")
    public void sendPutRequestWithBody(String endpoint, String body) {
        logger.info("Sending PUT request to endpoint: {}", endpoint);
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        response = apiClient.withBody(body).sendPutRequest(endpoint);
    }

    @When("Send DELETE request to endpoint {string}")
    public void sendDeleteRequest(String endpoint) {
        logger.info("Sending DELETE request to endpoint: {}", endpoint);
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        response = apiClient.sendDeleteRequest(endpoint);
    }

    @Then("Response status code should be {int}")
    public void validateStatusCode(int expectedStatusCode) {
        logger.info("Validating response status code: {}", expectedStatusCode);
        if (response == null) {
            throw new IllegalStateException("Response is null. No API request has been sent yet.");
        }
        assertEquals(expectedStatusCode, response.getStatusCode(), "Status code mismatch");
    }

    @Then("Response body should contain {string}")
    public void validateResponseBodyContains(String expectedText) {
        logger.info("Validating response body contains: {}", expectedText);
        if (response == null) {
            throw new IllegalStateException("Response is null. No API request has been sent yet.");
        }
        String responseBody = response.getBody().asString();
        assertTrue(responseBody.contains(expectedText), "Response body should contain: " + expectedText);
    }

    @Then("Response body should have json path {string} with value {string}")
    public void validateJsonPath(String jsonPath, String expectedValue) {
        logger.info("Validating json path {} equals {}", jsonPath, expectedValue);
        apiClient.validateResponseBodyContains(response, jsonPath, expectedValue);
    }

    @Then("Print response body")
    public void printResponseBody() {
        if (response == null) {
            logger.error("Response is null. No API request has been sent yet.");
            return;
        }
        String responseBody = response.getBody().asString();
        logger.info("Response Body: {}", JsonUtils.prettifyJson(responseBody));
    }

    @When("User logs in to EventHub API with valid configured credentials")
    public void userLogsInToEventHubApiWithValidConfiguredCredentials() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        response = apiClient.login(ConfigReader.getUiUsername(), ConfigReader.getUiPassword());
    }

    @When("User logs in to EventHub API with invalid credentials")
    public void userLogsInToEventHubApiWithInvalidCredentials() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        response = apiClient.login("invalid.user@example.com", "wrongpassword");
    }

    @When("User logs in to EventHub API with empty email")
    public void userLogsInToEventHubApiWithEmptyEmail() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        response = apiClient.login("", "Password123");
    }

    @When("User logs in to EventHub API with empty password")
    public void userLogsInToEventHubApiWithEmptyPassword() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        response = apiClient.login("test.user@example.com", "");
    }

    @Then("Response success flag should be {string}")
    public void responseSuccessFlagShouldBe(String expectedSuccessFlag) {
        boolean expected = Boolean.parseBoolean(expectedSuccessFlag);
        Boolean actual = response.jsonPath().getBoolean("success");
        assertNotNull(actual, "Response success flag should be present");
        assertEquals(expected, actual, "Response success flag mismatch");
    }

    @Then("Response error message should be {string}")
    public void responseErrorMessageShouldBe(String expectedErrorMessage) {
        String actualError = response.jsonPath().getString("error");
        assertEquals(expectedErrorMessage, actualError, "Response error message mismatch");
    }

    @Then("JWT token should be present in login response")
    public void jwtTokenShouldBePresentInLoginResponse() {
        jwtToken = apiClient.extractToken(response);
        assertNotNull(jwtToken, "JWT token should be present");
        assertFalse(jwtToken.isBlank(), "JWT token should not be blank");
    }

    @Then("JWT claims should be valid")
    public void jwtClaimsShouldBeValid() {
        if (jwtToken == null || jwtToken.isBlank()) {
            jwtToken = apiClient.extractToken(response);
        }

        String[] tokenParts = jwtToken.split("\\.");
        assertEquals(3, tokenParts.length, "JWT should have exactly 3 sections");

        Map<String, Object> claims = apiClient.extractJwtClaims(jwtToken);
        assertNotNull(claims.get("userId"), "JWT userId claim should exist");
        assertNotNull(claims.get("email"), "JWT email claim should exist");
        assertNotNull(claims.get("iat"), "JWT iat claim should exist");
        assertNotNull(claims.get("exp"), "JWT exp claim should exist");

        long iat = toLongClaim(claims.get("iat"), "iat");
        long exp = toLongClaim(claims.get("exp"), "exp");
        long currentEpoch = Instant.now().getEpochSecond();

        assertTrue(exp > iat, "JWT exp should be greater than iat");
        assertTrue(exp > currentEpoch, "JWT exp should be greater than current timestamp");
    }

    @When("User accesses bookings endpoint with bearer token")
    public void userAccessesBookingsEndpointWithBearerToken() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        if (jwtToken == null || jwtToken.isBlank()) {
            throw new IllegalStateException("JWT token is not available. Execute login and token extraction steps first.");
        }
        response = apiClient.withBearerToken(jwtToken).getBookings();
    }

    @When("User accesses bookings endpoint without token")
    public void userAccessesBookingsEndpointWithoutToken() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        response = apiClient.getBookings();
    }

    @Then("Protected bookings response should be successful")
    public void protectedBookingsResponseShouldBeSuccessful() {
        Boolean success = response.jsonPath().getBoolean("success");
        assertEquals(Boolean.TRUE, success, "Bookings success flag should be true");
        assertNotNull(response.jsonPath().get("data"), "Bookings data should be present");
    }

    private long toLongClaim(Object claimValue, String claimName) {
        if (claimValue instanceof Number number) {
            return number.longValue();
        }
        try {
            return Long.parseLong(String.valueOf(claimValue));
        } catch (NumberFormatException exception) {
            throw new AssertionError("JWT claim '" + claimName + "' is not a valid numeric value: " + claimValue);
        }
    }
}
