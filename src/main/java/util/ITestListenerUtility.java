package util;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class ITestListenerUtility extends ScreenshotUtility implements ITestListener {

    @Override
    public void onTestSuccess(ITestResult result) {
        captureScreenshot(result, "pass");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        captureScreenshot(result, "fail");
    }
}
