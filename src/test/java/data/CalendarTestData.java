package data;

import org.testng.annotations.DataProvider;

public class CalendarTestData {

    // Each row is {timeSlot, title}. Uncomment rows to run the same test once per row.
    @DataProvider(name = "calendarData")
    public Object[][] calendarData() {
        return new Object[][]{
                {"01:00", "Test"},
                {"14:00", "Test2"},
                {"05:00", "Test3"}
        };
    }

    // {timeSlot, title} for tests that save the event; titles should be unique so leftovers don't match
    @DataProvider(name = "createEventData")
    public Object[][] createEventData() {
        return new Object[][]{
                {"05:00", "Appium MCP Event"}
        };
    }
}
