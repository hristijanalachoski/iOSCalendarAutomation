package util;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import io.qameta.allure.Step;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import pages.*;

@Listeners({ITestListenerUtility.class})
public class DriverSetup {

    protected IOSDriver driver;
    protected final AppiumServerManager appiumServerManager = new AppiumServerManager();

    protected RestAssuredUtility restAssuredUtility;

    protected CalendarHomeScreen calendarHomeScreen;
    protected NewEventScreen newEventScreen;
    protected EventDetailsScreen eventDetailsScreen;

    @BeforeSuite
    public void startAppiumServer() {
        appiumServerManager.startAppiumServerWithCustomFlags();
    }

    @BeforeMethod
    @Step("Set up iOS driver and page objects")
    public void setUp() {

        XCUITestOptions options = new XCUITestOptions();
        options.setDeviceName(ConfigReader.getProperty("deviceName"))
                .setPlatformVersion(ConfigReader.getProperty("platformVersion"))
                .setBundleId(ConfigReader.getProperty("bundleId"))
                .setNoReset(false)
                .setFullReset(false);
        try {
            driver = new IOSDriver(new URI(GlobalVariables.appiumLocalUrl).toURL(), options);
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        restAssuredUtility = new RestAssuredUtility();

        calendarHomeScreen = new CalendarHomeScreen(driver);
        newEventScreen = new NewEventScreen(driver);
        eventDetailsScreen = new EventDetailsScreen(driver);
    }

    @AfterMethod(alwaysRun = true)
    @Step("Quit iOS driver")
    public void tearDown() {
        if (driver != null)
            driver.quit();
    }

    @AfterSuite(alwaysRun = true)
    public void stopAppiumServer() {
        appiumServerManager.stopAppiumServer();
    }
}
