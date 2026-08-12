@API
Feature: User API Endpoints

  Background:
    Given API client is initialized

  @EventHubLoginAPI
  Scenario: API-001 Valid API login with configured credentials
    When User logs in to EventHub API with valid configured credentials
    Then Response status code should be 200
    And Response success flag should be "true"
    And JWT token should be present in login response
    And JWT claims should be valid

  @EventHubLoginAPI
  Scenario: NEG-API-001 Invalid API login with wrong credentials
    When User logs in to EventHub API with invalid credentials
    Then Response status code should be 400
    And Response success flag should be "false"
    And Response error message should be "Invalid email or password"

  @EventHubLoginAPI
  Scenario: FORM-API-001 Empty email validation for login API
    When User logs in to EventHub API with empty email
    Then Response status code should be 400
    And Response success flag should be "false"
    And Response error message should be "Validation failed"
    And Response body should contain "A valid email is required"

  @EventHubLoginAPI
  Scenario: FORM-API-002 Empty password validation for login API
    When User logs in to EventHub API with empty password
    Then Response status code should be 400
    And Response success flag should be "false"
    And Response error message should be "Validation failed"
    And Response body should contain "Password must be at least 6 characters"

  @EventHubLoginAPI
  Scenario: SESSION-API-001 Access bookings with JWT token
    When User logs in to EventHub API with valid configured credentials
    Then Response status code should be 200
    And JWT token should be present in login response
    And JWT claims should be valid
    When User accesses bookings endpoint with bearer token
    Then Response status code should be 200
    And Response success flag should be "true"
    And Protected bookings response should be successful

  @EventHubLoginAPI
  Scenario: SESSION-API-002 Access bookings without token
    When User accesses bookings endpoint without token
    Then Response status code should be 401
    And Response success flag should be "false"
    And Response error message should be "Unauthorized"
