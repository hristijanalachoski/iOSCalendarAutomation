package util;

import io.appium.java_client.ios.IOSDriver;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtility {

    private static final String SCREENSHOTS_DIR = "screenshots";
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public void captureScreenshot(ITestResult result, String status) {
        if (!(result.getInstance() instanceof DriverSetup setup) || setup.driver == null) {
            System.out.println("No driver available, skipping screenshot for " + result.getName());
            return;
        }
        IOSDriver driver = setup.driver;

        String folder = status.equalsIgnoreCase("pass") ? "Pass" : "Fails";
        String fileName = result.getTestClass().getRealClass().getSimpleName() + "." + result.getName()
                + " - " + LocalDateTime.now().format(TIMESTAMP) + ".png";

        try {
            File source = driver.getScreenshotAs(OutputType.FILE);
            File destination = new File(SCREENSHOTS_DIR + File.separator + folder, fileName);
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot saved: " + destination.getPath());
        } catch (IOException | RuntimeException e) {
            System.out.println("Failed to capture screenshot for " + result.getName() + ": " + e.getMessage());
        }
    }
}
