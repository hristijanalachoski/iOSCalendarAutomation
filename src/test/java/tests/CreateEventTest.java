package tests;

import data.CalendarTestData;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.testng.Assert;
import org.testng.annotations.Test;

import util.DriverSetup;

// Generated from a flow recorded with appium-mcp on iPhone 17 Pro / iOS 26.3
@Epic("Calendar App")
@Feature("Event creation")
public class CreateEventTest extends DriverSetup {

    @Test(testName = "Create and save event", dataProviderClass = CalendarTestData.class, dataProvider = "createEventData")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create an event on a timeslot, save it, verify it is listed, then delete it")
    public void createAndSaveEvent(String timeSlot, String title) {

        Assert.assertTrue(calendarHomeScreen.calendarHomeScreenLoaded(), "Calendar home screen is not loaded");

        calendarHomeScreen.createEventOnTimeslot(timeSlot);
        Assert.assertTrue(newEventScreen.newEventScreenLoaded(), "New event screen is not loaded");

        newEventScreen.enterEventTitle(title);
        newEventScreen.tapAddButton();

        calendarHomeScreen.switchToListView();
        try {
            Assert.assertTrue(calendarHomeScreen.isEventDisplayedInList(title), "Event '" + title + "' is not shown in the list view");

            calendarHomeScreen.openEvent(title);
            eventDetailsScreen.deleteEvent();
            Assert.assertTrue(calendarHomeScreen.isEventRemovedFromList(title), "Event '" + title + "' was not deleted");
        } finally {
            calendarHomeScreen.switchToDayView();
        }
    }
}
