# UI Playground Selenium Automation

This project is a robust, scalable Java-based UI automation framework built to test the elements on [UI Test Automation Playground](http://uitestingplayground.com/). It uses Selenium WebDriver, TestNG, and Maven, and incorporates modern reporting and debugging utilities like Allure Reports and automated video recording of failed test retries.

## 🚀 Technologies Used
- **Language**: Java
- **Automation Tool**: Selenium WebDriver (v4.40.0)
- **Test Framework**: TestNG (v7.12.0)
- **Build Tool**: Maven
- **Driver Management**: WebDriverManager (v6.3.3)
- **Reporting**: Allure TestNG (v2.32.0)
- **Logging**: SLF4J and Log4j2
- **Video Recording**: Monte Screen Recorder (v0.7.7.0)

## 🏗️ Project Architecture

The framework is structured using the **Page Object Model (POM)** pattern to separate test logic from page actions, enhancing code reusability and maintainability.

```text
src/
├── main/java/com/uiplayground/
│   ├── base/        # Base classes like BaseTest, DriverSetup, and DriverManager
│   ├── constants/   # Project constants (e.g., Browser settings)
│   ├── listeners/   # TestNG listeners (TestFailedListener, RetryAnalyzer, RetryTransformer)
│   ├── pages/       # Page Object Models (e.g., HomePage, PlaygroundExamplesPage)
│   └── utils/       # Utility classes (ScreenshotUtil, VideoRecorderUtils)
└── test/java/tests/
    └── DynamicIdTest.java # Test cases
```

### Key Features
1. **Thread-Safe Driver Setup**: `BaseTest` utilizes `ThreadLocal<WebDriver>` and `ThreadLocal<VideoRecorderUtils>` for parallel test execution compatibility.
2. **Auto-Retry & Video Recording**: Configured to capture video recordings using Monte Screen Recorder natively when tests are retried via `RetryAnalyzer`. Upon failure, the video is safely attached directly to the Allure report.
3. **AspectJ Weaver**: Utilized in `pom.xml` via `maven-surefire-plugin` to hook Allure reporting and attach screenshots/videos directly via listeners.

## ⚙️ Setup and Prerequisites

1. **Java JDK**: 11 or higher (configured for 25 in pom.xml, please ensure compatibility or modify `maven.compiler.source` properties as needed).
2. **Maven**: Installed and configured in the system PATH.
3. **Allure CLI**: (Optional) For serving the rendered Allure reports locally.

## 🏃🏽‍♂️ How to Run the Tests

The project invokes execution via the Maven Surefire plugin. You can run the entire smoke suite using the following standard Maven command from the project root directory:

```bash
mvn clean test
```
*Note: This will execute the configuration and test classes specified under `suites/smokeTestng.xml`.*

### Specific Suites
To run a specific XML suite file, you can utilize the `surefire.suiteXmlFiles` parameter:

```bash
mvn clean test -Dsurefire.suiteXmlFiles=suites/smokeTestng.xml
```

## 📊 Viewing the Allure Report

After the test execution finishes, the results are stored in the `allure-results` directory. 

To generate and serve the HTML report using the Allure Commandline, execute:

```bash
allure serve allure-results
```

This will automatically open the interactive graphical report in your default web browser, displaying test pass rates, step-by-step failures, logs, and attached screen recordings for retried tests.

## 📝 Writing a New Test
1. Create a logic module in `src/main/java/com/uiplayground/pages/` representing the web page.
2. Create standard Selenium interaction methods in that page class.
3. Navigate to `src/test/java/tests/` to create a new class extending `BaseTest`.
4. Define your tests annotated with `@Test`, instantiate your Page classes, perform actions, and ensure you use TestNG `Assert` statements to validate functionality.
