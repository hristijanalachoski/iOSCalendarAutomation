package pages;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.support.PageFactory;

import io.qameta.allure.Step;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import util.GlobalVariables;

public class EventDetailsScreen {

    protected IOSDriver driver;

    @iOSXCUITFindBy(accessibility = "delete-event-button")
    private WebElement deleteEventButton;

    // Confirmation sheet shown after tapping "Delete Event"; it has the same label as the button above
    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeButton' AND label == 'Delete Event' AND name != 'delete-event-button'")
    private WebElement confirmDeleteButton;

    public EventDetailsScreen(IOSDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @Step("Deleting the event")
    public void deleteEvent() {
        WebDriverWait wait = new WebDriverWait(driver, GlobalVariables.globalTimeout);
        wait.until(ExpectedConditions.elementToBeClickable(deleteEventButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButton)).click();
    }
}
