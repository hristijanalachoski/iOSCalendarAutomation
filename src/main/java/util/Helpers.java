package util;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.support.ui.FluentWait;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

import static java.time.Duration.ZERO;
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.interactions.PointerInput.Kind.TOUCH;
import static org.openqa.selenium.interactions.PointerInput.Origin.viewport;

public class Helpers {

    public enum Directions {
        UP,
        DOWN
    }

    private final PointerInput FINGER = new PointerInput(TOUCH, "finger");

    public void longPress(IOSDriver driver, WebElement element) {
        Point location = getCenter(element);
        Sequence longPress = new Sequence(FINGER, 0);
        longPress.addAction(FINGER.createPointerMove(ZERO, viewport(), location.x, location.y));
        longPress.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPress.addAction(FINGER.createPointerMove(ofSeconds(1), viewport(), location.x, location.y));
        longPress.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(longPress));
    }

    public void swipeVertically(IOSDriver driver, Directions directions) {
        int startX = driver.manage().window().getSize().getWidth() / 2;
        int startY = driver.manage().window().getSize().getHeight() / 2;

        int endY;

        switch (directions) {
            case UP -> endY = (int) (driver.manage().window().getSize().getHeight() * 0.2);
            case DOWN -> endY = (int) (driver.manage().window().getSize().getHeight() * 0.8);
            default -> throw new IllegalArgumentException("Invalid direction selected:" + directions);
        }

        Sequence swipe = new Sequence(FINGER, 0);
        swipe.addAction(FINGER.createPointerMove(ZERO, viewport(), startX, startY));
        swipe.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(FINGER.createPointerMove(ofSeconds(1), viewport(), startX, endY));
        swipe.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(swipe));
    }

    public void scrollTo(IOSDriver driver, Directions directions, WebElement element, int swipeCount) {
        IntStream.range(0, swipeCount).forEach(obj -> {
            if (!element.isDisplayed())
                swipeVertically(driver, directions);
        });
    }

    public Point getCenter(WebElement element) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        return new Point(location.x + size.getWidth() / 2, location.y + size.getHeight() / 2);
    }

    public boolean waitForElementVisibility(IOSDriver driver, WebElement element) {
        return new FluentWait<>(driver)
                .withTimeout(GlobalVariables.globalTimeout)
                .ignoring(NoSuchElementException.class, TimeoutException.class)
                .until(d -> element.isDisplayed());
    }

    public boolean waitForElementVisibility(IOSDriver driver, By element) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(3))
                .ignoring(NoSuchElementException.class, TimeoutException.class)
                .until(d -> driver.findElement(element).isDisplayed());
    }
}
