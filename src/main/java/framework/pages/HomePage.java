package framework.pages;

import org.openqa.selenium.By;

public class HomePage extends BasePage {
    private static final By MY_BOOKINGS_LINK = By.xpath("//a[normalize-space()='My Bookings']");

    public HomePage() {
        super();
    }

    public boolean isOnProductsPage() {
        waitForUrlToNotContain("/login");
        return waitForElementToBeVisible(MY_BOOKINGS_LINK).isDisplayed();
    }
}
