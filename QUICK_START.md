# Quick Start Guide

## Project Successfully Built ✅

This is a production-ready Java automation testing framework with full support for UI and API automation.

---

## 🚀 Getting Started in 5 Minutes

### 1. Verify the Setup
```bash
cd "c:\Users\prabh\OneDrive\Desktop\Prabu\Studies\Eclipse versions\eclipse 23\selenium-restassured-cucumber-framework"
mvn clean compile
```

### 2. Update Configuration (if needed)
Edit: `src/test/resources/config/config.properties`
```properties
browser=chrome              # chrome, firefox, edge
headless.mode=false        # true for headless execution
base.url=YOUR_APP_URL      # Your application URL
api.base.url=YOUR_API_URL  # Your API base URL
```

### 3. Run Tests
```bash
# All tests
mvn clean test

# Only UI tests
mvn clean test -Dcucumber.filter.tags="@UI"

# Only API tests  
mvn clean test -Dcucumber.filter.tags="@API"

# Specific feature
mvn clean test -Dcucumber.features="src/test/resources/features/ui/Login.feature"
```

### 4. View Reports
- HTML Report: `target/cucumber-report/cucumber.html`
- Screenshots (on failure): `target/screenshots/`
- Logs: `target/logs/automation.log`

---

## 📁 Key Files to Know

### Configuration
- `src/test/resources/config/config.properties` - Application settings

### Framework Core
- `src/main/java/framework/config/ConfigReader.java` - Config management
- `src/main/java/framework/driver/DriverFactory.java` - Browser initialization
- `src/main/java/framework/pages/BasePage.java` - Reusable Selenium methods

### Test Scenarios
- `src/test/resources/features/ui/Login.feature` - UI scenarios
- `src/test/resources/features/api/LoginApi.feature` - API scenarios

### Step Implementations
- `src/test/java/tests/stepdefinitions/LoginSteps.java` - UI steps
- `src/test/java/tests/stepdefinitions/ApiSteps.java` - API steps

---

## 🧪 Writing Your First Test

### Step 1: Create a Feature File
Create: `src/test/resources/features/ui/MyTest.feature`
```gherkin
@UI
Feature: My Test

  Scenario: Test something
    Given User navigates to the login page
    When User enters username as "student"
    Then User should see something
```

### Step 2: Implement Steps
Add to: `src/test/java/tests/stepdefinitions/LoginSteps.java`
```java
@Then("User should see something")
public void userShouldSeeSomething() {
    // Your assertion here
}
```

### Step 3: Create Page Object (if needed)
Create: `src/main/java/framework/pages/MyPage.java`
```java
public class MyPage extends BasePage {
    private static final By MY_ELEMENT = By.id("element-id");
    
    public void doSomething() {
        click(MY_ELEMENT);
    }
}
```

---

## 🔑 Key Framework Concepts

### ConfigReader - Access Configuration
```java
String browser = ConfigReader.getBrowser();
String baseUrl = ConfigReader.getBaseUrl();
int timeout = ConfigReader.getExplicitWait();
```

### BasePage - Reusable Selenium Methods
```java
click(By locator);                          // Click element
type(By locator, String text);              // Type text
String text = getText(By locator);          // Get text
boolean isDisplayed = isElementDisplayed(); // Check visibility
waitForElementToBeVisible(By locator);      // Explicit wait
```

### ApiClient - Make API Calls
```java
ApiClient apiClient = new ApiClient();
Response response = apiClient
    .withHeader("Authorization", "Bearer token")
    .withQueryParam("page", "1")
    .sendGetRequest("/users");

apiClient.validateStatusCode(response, 200);
```

### DriverFactory - Browser Management
```java
// Automatically handled by Hooks, but available if needed
WebDriver driver = DriverFactory.initializeDriver();
driver = DriverFactory.getDriver();
DriverFactory.quitDriver();
```

---

## 📊 Project Structure

```
src/
├── main/java/framework/
│   ├── config/          → Configuration management
│   ├── driver/          → WebDriver management
│   ├── pages/           → Page Object Model classes
│   ├── api/             → API client and endpoints
│   └── utils/           → Utility functions
│
└── test/
    ├── java/tests/
    │   ├── runners/     → Test runner (TestRunner.java)
    │   ├── stepdefinitions/  → Step implementations
    │   └── hooks/       → Setup/teardown (Hooks.java)
    │
    └── resources/
        ├── features/    → Gherkin feature files
        ├── config/      → Properties file
        ├── testdata/    → JSON test data
        └── logback.xml  → Logging configuration
```

---

## 🛠️ Common Tasks

### Add New Browser Support
Edit `src/main/java/framework/driver/DriverFactory.java`
```java
case "safari" -> initializeSafari();
```

### Change Default Browser
Edit `src/test/resources/config/config.properties`
```properties
browser=firefox
```

### Run in Headless Mode
Edit `src/test/resources/config/config.properties`
```properties
headless.mode=true
```

### Add Custom Assertion
Add method to `src/test/java/tests/stepdefinitions/LoginSteps.java`
```java
@Then("Custom assertion")
public void customAssertion() {
    // Your code
}
```

---

## 📝 Feature File Tags

- `@UI` - UI automation tests (run with: `mvn test -Dcucumber.filter.tags="@UI"`)
- `@API` - API automation tests (run with: `mvn test -Dcucumber.filter.tags="@API"`)
- Create custom tags as needed

---

## 🔍 Troubleshooting

### Tests not running?
- Check feature file is in `src/test/resources/features/`
- Verify step definition methods match exactly
- Ensure TestRunner has correct GLUE path

### Locators not found?
- Wait methods are built-in (no hardcoding needed)
- Check locator in browser dev tools
- Use explicit waits via BasePage methods

### WebDriver issues?
- WebDriverManager handles driver downloads automatically
- Check internet connection
- Delete `.m2/repository` and rebuild if needed

### Screenshots not saving?
- Ensure `target/screenshots/` directory is writable
- Check ScreenshotUtils.java has correct path

---

## 📚 Documentation

- **README.md** - Complete framework documentation
- **FRAMEWORK_SUMMARY.md** - Detailed architecture overview
- **pom.xml** - Maven dependencies and plugins

---

## 🎯 Next Steps

1. Update `config.properties` with your application details
2. Create your feature files in `src/test/resources/features/`
3. Implement step definitions
4. Create page objects for your pages
5. Run tests and view reports

---

Happy Testing! 🚀
