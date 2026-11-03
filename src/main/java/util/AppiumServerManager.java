package util;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class AppiumServerManager {

    private AppiumDriverLocalService appiumService;

    public void startAppiumServer() {
        appiumService = AppiumDriverLocalService.buildDefaultService();
        appiumService.start();
        System.out.println("Appium Server started at: " + appiumService.getUrl());
    }

    public void startAppiumServerWithCustomFlags() {
        appiumService = AppiumDriverLocalService.buildService(
                new AppiumServiceBuilder()
                        .usingPort(4723)
                        .withArgument(GeneralServerFlag.ALLOW_INSECURE, "*:chromedriver_autodownload")
        );
        appiumService.start();
        System.out.println("Appium Server started at: " + appiumService.getUrl());
    }

    public void stopAppiumServer() {
        if (appiumService != null)
            appiumService.stop();
    }
}
