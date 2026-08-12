# Selenium + REST Assured + Cucumber Automation Framework

A production-ready Java automation testing framework supporting both UI and API automation using Selenium WebDriver, REST Assured, and Cucumber BDD.

## Features

- **UI Automation**: Selenium WebDriver with Page Object Model (POM)
- **API Automation**: REST Assured for API testing
- **BDD Framework**: Cucumber with Gherkin syntax
- **Test Management**: JUnit 5 with Cucumber integration
- **Browser Support**: Chrome, Firefox, Edge (automatic driver management via WebDriverManager)
- **Logging**: SLF4J + Logback with file and console appenders
- **Screenshots**: Automatic screenshot capture on test failure
- **Reporting**: Cucumber HTML and JSON reports
- **Configuration**: Externalized config.properties for easy management
- **Java Version**: Java 17+

## Project Structure

```
src/main/java/framework/
├── config/
│   └── ConfigReader.java              # Configuration management
├── driver/
│   └── DriverFactory.java             # WebDriver lifecycle management
├── pages/
│   ├── BasePage.java                  # Base class with reusable Selenium methods
│   ├── LoginPage.java                 # Login page object
│   └── HomePage.java                  # Home page object
├── api/
│   ├── ApiClient.java                 # REST Assured API client
│   └── ApiEndpoints.java              # API endpoint constants
└── utils/
    ├── WaitUtils.java                 # Wait utilities
    ├── ScreenshotUtils.java           # Screenshot capture
    └── JsonUtils.java                 # JSON processing utilities

src/test/java/tests/
├── runners/
│   └── TestRunner.java                # JUnit 5 test runner with Cucumber
├── stepdefinitions/
│   ├── LoginSteps.java                # UI login step definitions
│   └── ApiSteps.java                  # API step definitions
└── hooks/
    └── Hooks.java                     # Cucumber hooks for setup/teardown

src/test/resources/
├── features/
│   ├── ui/
│   │   └── Login.feature              # UI test scenarios
│   └── api/
│       └── LoginApi.feature           # API test scenarios
├── config/
│   └── config.properties              # Configuration file
├── testdata/
│   └── testdata.json                  # Test data
└── logback.xml                        # Logging configuration
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Git

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/selenium-restassured-cucumber-framework.git
   cd selenium-restassured-cucumber-framework
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

## Configuration

Edit `src/test/resources/config/config.properties` to set:

```properties
# Browser Configuration
browser=chrome                    # chrome, firefox, edge
headless.mode=false             # Run in headless mode

# UI Application
base.url=https://app.example.com

# API Configuration
api.base.url=https://api.example.com/v1

# Wait Configuration (in seconds)
implicit.wait=10
explicit.wait=15
```

## Running Tests

### Run all tests
```bash
mvn clean test
```

### Run only UI tests (with @UI tag)
```bash
mvn clean test -Dcucumber.filter.tags="@UI"
```

### Run only API tests (with @API tag)
```bash
mvn clean test -Dcucumber.filter.tags="@API"
```

### Run specific feature file
```bash
mvn clean test -Dcucumber.features="src/test/resources/features/ui/Login.feature"
```

### Run with specific browser
```bash
mvn clean test -Dbrowser=firefox
```

### Run in headless mode
```bash
mvn clean test -Dheadless.mode=true
```

## Test Reports

After test execution, reports are generated:

- **HTML Report**: `target/cucumber-report/cucumber.html`
- **JSON Report**: `target/cucumber-report/cucumber.json`
- **Screenshots**: `target/screenshots/` (on test failure)
- **Logs**: `target/logs/automation.log`

## Page Object Model Usage

### Example: Creating a new page class

```java
public class ProductPage extends BasePage {
    private static final By PRODUCT_TITLE = By.id("product-title");
    private static final By ADD_TO_CART = By.xpath("//button[@class='add-cart']");

    public String getProductTitle() {
        return getText(PRODUCT_TITLE);
    }

    public void addProductToCart() {
        click(ADD_TO_CART);
    }
}
```

## Step Definitions Usage

### Example: Creating new step definitions

```java
public class ProductSteps {
    private ProductPage productPage;

    @When("User views product details")
    public void userViewsProductDetails() {
        productPage = new ProductPage();
        String title = productPage.getProductTitle();
    }

    @And("User adds product to cart")
    public void userAddsProductToCart() {
        productPage.addProductToCart();
    }
}
```

## API Client Usage

### Example: Making API calls

```java
ApiClient apiClient = new ApiClient();

// GET request
Response response = apiClient
    .withBearerToken("your-token")
    .sendGetRequest("/users");

// POST request
Response response = apiClient
    .withBody("{\"name\": \"John\"}")
    .sendPostRequest("/users");

// Validate response
apiClient.validateStatusCode(response, 200);
```

## Feature File Writing

### Example: Feature file with UI and API scenarios

```gherkin
@UI
Feature: Login Functionality

  Scenario: Successful login
    Given User navigates to the login page
    When User enters username as "student"
    And User enters password as "Password123"
    And User clicks the login button
    Then User should be successfully logged in

@API
Feature: User API

  Scenario: Get users
    Given API client is initialized
    When Send GET request to endpoint "/users"
    Then Response status code should be 200
```

## Best Practices

1. **Locators**: Store all locators in page classes using `By` objects
2. **No Hardcoding**: Use ConfigReader for URLs, timeouts, and credentials
3. **Logging**: Use SLF4J logger for all important actions
4. **Reusability**: Write common methods in BasePage
5. **DRY**: Don't Repeat Yourself - use utility classes
6. **Tags**: Use @UI and @API tags to categorize scenarios
7. **Data**: Keep test data in testdata.json or databases, not in code
8. **Screenshots**: Automatically captured on failure via Hooks

## Continuous Integration

The framework is CI/CD ready. Example GitHub Actions workflow:

```yaml
name: Automated Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '17'
      - run: mvn clean test
      - uses: actions/upload-artifact@v2
        if: always()
        with:
          name: test-reports
          path: target/cucumber-reports
```

## Troubleshooting

### WebDriver not found
- Ensure Java 17+ is installed
- WebDriverManager will automatically download the appropriate driver
- Check internet connection for driver download

### Config file not found
- Ensure file path in ConfigReader matches your project structure
- Run tests from project root directory

### Tests not running
- Verify feature files are in `src/test/resources/features/`
- Ensure step definitions package matches GLUE configuration in TestRunner
- Check for step definition mismatches with feature file steps

### Screenshots not captured
- Ensure `target/screenshots/` directory has write permissions
- Verify ScreenshotUtils is called in Hooks.java

## Contributing

1. Create a new feature branch
2. Make your changes
3. Write tests for new features
4. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For issues or questions, please contact the development team or create an issue in the repository.

---

**Happy Testing! 🚀**
