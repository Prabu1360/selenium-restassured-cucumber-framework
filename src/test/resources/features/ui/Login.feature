@UI
Feature: User Login Functionality

  Background:
    Given User navigates to the login page

  @EventHubLoginUI
  Scenario: POS-UI-001 Valid login to EventHub
    When User logs in with demo credentials
    Then User should be successfully logged in
    And User should be redirected from login page

  @EventHubLoginUI
  Scenario: NEG-UI-001 Invalid login credentials
    When User enters email as "invalid.user@example.com"
    And User enters password as "wrongpassword"
    And User clicks the login button
    Then User should see an error message
    And Error message should contain "Invalid email or password"
    And User should remain on login page

  @EventHubLoginUI
  Scenario: FORM-UI-001 Empty email validation on login form
    When User enters email as ""
    And User enters password as "Password123"
    And User clicks the login button
    Then Email validation message should be "Enter a valid email"
    And User should remain on login page

  @EventHubLoginUI
  Scenario: FORM-UI-002 Empty password validation on login form
    When User enters email as "test.user@example.com"
    And User enters password as ""
    And User clicks the login button
    Then Password validation message should be "Password must be at least 6 characters"
    And User should remain on login page
