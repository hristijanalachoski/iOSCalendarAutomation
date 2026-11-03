package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.support.PageFactory;

import io.qameta.allure.Step;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;

import util.GlobalVariables;
import util.Helpers;

public class CalendarHomeScreen extends Helpers {

    protected IOSDriver driver;

    @iOSXCUITFindBy(iOSNsPredicate = "name == \"Calendar\"")
    private WebElement calendarHomeScreenContainer;

    @iOSXCUITFindBy(accessibility = "toggle-day-list-view")
    private WebElement viewModeButton;

    @iOSXCUITFindBy(accessibility = "list-view")
    private WebElement listViewOption;

    @iOSXCUITFindBy(accessibility = "single-day")
    private WebElement singleDayOption;

    public CalendarHomeScreen(IOSDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @Step("Calendar home screen is loaded")
    public boolean calendarHomeScreenLoaded() {
        return new WebDriverWait(driver, GlobalVariables.globalTimeout).until(ExpectedConditions.visibilityOf(calendarHomeScreenContainer)).isDisplayed();
    }

    @Step("Event is created on timeslot: {timeSlot}")
    public void createEventOnTimeslot(String timeSlot) {
        WebElement element = driver.findElement(AppiumBy.accessibilityId(timeSlot));
        scrollTo(driver, Directions.DOWN, element, 5);
        longPress(driver, element);
    }

    @Step("Switching calendar to list view")
    public void switchToListView() {
        selectViewMode(listViewOption);
    }

    @Step("Switching calendar to single-day view")
    public void switchToDayView() {
        selectViewMode(singleDayOption);
    }

    // Events in the day timeline are not exposed in the accessibility tree; in list view each event is a cell
    // labelled "<title>, from <start> to <end>, <calendar>".
    @Step("Event '{title}' is shown in the list view")
    public boolean isEventDisplayedInList(String title) {
        return waitForElementVisibility(driver, eventCell(title));
    }

    @Step("Opening event '{title}'")
    public void openEvent(String title) {
        driver.findElement(eventCell(title)).click();
    }

    @Step("Event '{title}' is no longer listed")
    public boolean isEventRemovedFromList(String title) {
        return new WebDriverWait(driver, GlobalVariables.globalTimeout).until(ExpectedConditions.invisibilityOfElementLocated(eventCell(title)));
    }

    private By eventCell(String title) {
        return AppiumBy.iOSNsPredicateString(
                "type == 'XCUIElementTypeCell' AND label BEGINSWITH '" + title + ", from '");
    }

    private void selectViewMode(WebElement option) {
        WebDriverWait wait = new WebDriverWait(driver, GlobalVariables.globalTimeout);
        wait.until(ExpectedConditions.elementToBeClickable(viewModeButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
    }
}
