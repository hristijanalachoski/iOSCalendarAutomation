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

@Epic("Calendar App")
@Feature("Event creation")
public class CalendarTest extends DriverSetup {

    @Test(testName = "Calendar test", dataProviderClass = CalendarTestData.class, dataProvider = "calendarData")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create a new event on a timeslot and enter its title")
    public void calendarTest(String timeSlot, String title) {

        Assert.assertTrue(calendarHomeScreen.calendarHomeScreenLoaded(), "Calendar home screen is not loaded");

        calendarHomeScreen.createEventOnTimeslot(timeSlot);
        Assert.assertTrue(newEventScreen.newEventScreenLoaded(), "New event screen is not loaded");

        newEventScreen.enterEventTitle(title);
    }
}
