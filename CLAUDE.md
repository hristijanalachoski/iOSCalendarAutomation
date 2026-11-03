# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

An Appium + TestNG mobile UI test project (Java 20, Maven) that automates the stock iOS Calendar app (`com.apple.mobilecal`) on an iOS simulator. Test data (event titles) is fetched live from a public REST API via Rest Assured.

## Commands

There is no Maven wrapper (`.mvn/` is empty), so use a system `mvn`.

```bash
mvn compile                                          # build
mvn test                                             # run all tests
mvn test -Dtest=CalendarTest                         # run one test class
mvn test -Dtest=CalendarTest#calendarTest            # run one test method
```

There is no testng.xml suite file yet; surefire discovers test classes under `src/test/java` by default. Reports land in `target/surefire-reports/`.

### Allure reports

Each run writes Allure results to `allure-results/` (gitignored). View them with the Allure CLI:

```bash
allure serve allure-results                          # build and open a temporary report
allure generate allure-results -o allure-report --clean   # static report
```

Surefire loads the AspectJ weaver as a `-javaagent` (see `pom.xml`); without it `@Step` annotations are silently ignored. Page-object methods, `DriverSetup.setUp()/tearDown()` and `RestAssuredUtility.getActivityValue()` carry `@Step`; test classes carry `@Epic`/`@Feature`/`@Severity`/`@Description`. Annotate new page-object methods and tests the same way.

### Prerequisites for running tests

- Appium 2+ installed globally (`npm i -g appium`) with the XCUITest driver (`appium driver install xcuitest`). The test suite **starts and stops its own Appium server** on port 4723 — do not run a separate Appium server on that port.
- Xcode with an iOS simulator matching `src/main/resources/configs/config.properties` (`deviceName=iPhone 17 Pro`, `platformVersion=26.3`). Change these values to match an available simulator.
- Network access to `https://bored-api.appbrewery.com/random` (used for event title data).

## Architecture

- **Production code under `src/main/java`, tests under `src/test/java`.** The framework (pages + utilities) lives in `main`, so dependencies like TestNG are compile-scope, not test-scope.
- **`util.DriverSetup` is the base class every test extends.** It owns the lifecycle:
  - `@BeforeSuite` starts a local Appium server via `AppiumServerManager` (port 4723, `chromedriver_autodownload` allowed).
  - `@BeforeMethod` builds `XCUITestOptions` from `ConfigReader`, creates a fresh `IOSDriver` against `GlobalVariables.appiumLocalUrl`, and instantiates `RestAssuredUtility` plus every page object as protected fields. **When adding a new page object, add a field and instantiate it here** so tests can use it.
  - `@AfterMethod` quits the driver; `@AfterSuite` stops the Appium server.
- **Page objects (`pages/`)** use Appium PageFactory (`io.appium.java_client.support.PageFactory` + `AppiumFieldDecorator`) with `@iOSXCUITFindBy` locators (prefer `iOSNsPredicate` / `iOSClassChain` / accessibility id). Each exposes a `...Loaded()` method that waits with `GlobalVariables.globalTimeout` and is asserted in tests.
- **`util.Helpers`** contains W3C-actions gestures (long press, vertical swipe, scroll-until-visible) and FluentWait helpers. Page objects that need gestures extend `Helpers` (e.g. `CalendarHomeScreen`).
- **Screenshots:** `DriverSetup` is annotated with `@Listeners({ITestListenerUtility.class})`, so every test extending it gets a screenshot on pass and on fail, saved to `screenshots/Pass` or `screenshots/Fails` (gitignored) as `<Class>.<method> - <timestamp>.png`. `ScreenshotUtility` reads the driver from the test instance (`((DriverSetup) result.getInstance()).driver`); this works because TestNG fires `onTestSuccess`/`onTestFailure` before `@AfterMethod` quits the driver.
- **Test data:** tests take parameters from TestNG DataProviders in `src/test/java/data/` (e.g. `CalendarTestData#calendarData` returns `{timeSlot, title}` rows), referenced via `@Test(dataProviderClass = ..., dataProvider = ...)`. Each row runs the test once.
- **`util.ConfigReader`** statically loads `configs/config.properties` from the classpath. `isHeadless` is defined there but not currently used.
- **`util.GlobalVariables`** holds shared constants (Appium URL, default timeout, API endpoint) and a mutable static `response` that `RestAssuredUtility` sets to the last API response.

## Dependency notes

`io.appium:java-client` 11 only pulls in `selenium-api`; `selenium-remote-driver` and `selenium-support` are declared explicitly in `pom.xml` and their versions must stay compatible with the java-client version when upgrading.
