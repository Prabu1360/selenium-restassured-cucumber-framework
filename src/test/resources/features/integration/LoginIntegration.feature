@UI @API @EventHubLoginIntegration
Feature: EventHub Login Integration Flow

  Scenario: INT-001 API JWT + /bookings + UI valid login flow
    Given API client is initialized
    When User logs in to EventHub API with valid configured credentials
    Then Response status code should be 200
    And Response success flag should be "true"
    And JWT token should be present in login response
    And JWT claims should be valid
    When User accesses bookings endpoint with bearer token
    Then Response status code should be 200
    And Response success flag should be "true"
    And Protected bookings response should be successful
    When User navigates to the login page
    And User logs in with demo credentials
    Then User should be successfully logged in
    And User should be redirected from login page

  Scenario: INT-002 /bookings unauthorized + UI invalid login flow
    Given API client is initialized
    When User accesses bookings endpoint without token
    Then Response status code should be 401
    And Response success flag should be "false"
    And Response error message should be "Unauthorized"
    When User navigates to the login page
    And User enters email as "invalid.user@example.com"
    And User enters password as "wrongpassword"
    And User clicks the login button
    Then User should see an error message
    And Error message should contain "Invalid email or password"
    And User should remain on login page
