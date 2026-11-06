# Selenium AI Automation

Maven and TestNG UI tests for the public practice site [practicetestautomation.com](https://practicetestautomation.com/). This repository does not contain the site. Each test opens it in a local browser.

Maven coordinates: `org.example:SeleniumForBeginners:1.0-SNAPSHOT`.

## Prerequisites

- JDK 21
- Maven on `PATH`
- Google Chrome, Mozilla Firefox, or Microsoft Edge, matching the suite you run
- A display. `BaseTest` starts the browser with no options, so the session is headed
- Outbound HTTPS to `practicetestautomation.com`

Selenium Manager, bundled with Selenium 4, downloads the matching driver on first use. There is no Maven wrapper.

## Run the tests

Surefire runs one suite from `src/test/resources/TestSuites/`. Pass the file name only. The default file is `FullRegressionSuite.xml`.

```bash
mvn test
mvn test -DsuiteXmlFile=SmokeTestSuite.xml
mvn test -DsuiteXmlFile=ExceptionsSuite.xml
mvn test -DsuiteXmlFile=DebugSuite.xml
mvn test -DsuiteXmlFile=TableSuite.xml
```

| Suite | What it runs |
| --- | --- |
| `FullRegressionSuite.xml` | Positive login on Chrome and Firefox. Invalid username on Firefox. Invalid password with browser value `safari`, which starts Chrome. All of `ExceptionsTests` on Chrome. All of `TableTests` on Chrome. |
| `SmokeTestSuite.xml` | `LoginTests` methods in the `smoke` group. No `browser` parameter, so Chrome. |
| `ExceptionsSuite.xml` | All of `ExceptionsTests` on Chrome. |
| `DebugSuite.xml` | Each exceptions test method in its own `<test>`. No `browser` parameter, so Chrome. |
| `TableSuite.xml` | All of `TableTests` on Chrome. |

## What is covered

**Login** at `https://practicetestautomation.com/practice-test-login/`.

- `loginFunctionalityTest` signs in with the practice site's published student account and checks the success URL `https://practicetestautomation.com/logged-in-successfully/`, the congratulations text, and the `Log out` link. Groups: `positive`, `regression`, `smoke`.
- `negativeLoginTest` checks the `#error` text. A suite that includes it must pass `username`, `password`, and `expectedErrorMessage`. Groups: `negative`, `regression`.

**Exceptions** at `https://practicetestautomation.com/practice-test-exceptions/`.

- `noSuchElementExceptionsTest` and `timeOutExceptionTest` click Add and expect `Row 2 was added`.
- `elementNotInteractableExceptionTest` enters text on row 2 and expects `Row 2 was saved`.
- `invalidElementStateExceptionTest` edits row 1 and expects `Row 1 was saved`.
- `staleElementReferenceExceptionTest` checks that the instructions are shown, then gone after Add.

**Table** at `https://practicetestautomation.com/practice-test-table/`. `TableTests` covers the eight cases published on that page: language filter, level filter, minimum enrollments, combined filters, the empty state, Reset, sort by enrollments, and sort by course name.

## Browsers

`BaseTest` reads the TestNG parameter `browser`. The default is `chrome`. Values `chrome`, `edge`, and `firefox` are supported, ignoring case. Any other value logs a warning and starts Chrome. No suite currently requests `edge`.

Each test method opens a browser in `@BeforeMethod` and calls `driver.quit()` in `@AfterMethod`.

## Layout

```
pom.xml
src/main/java/com/practicetestautomation/pageobjects/
  BasePage.java
  LoginPage.java
  SuccessfulLoginPage.java
  ExceptionsPage.java
  TablePage.java
src/test/java/com/practicetestautomation/tests/
  BaseTest.java
  login/LoginTests.java
  exceptions/ExceptionsTests.java
  table/TableTests.java
src/test/resources/TestSuites/
  FullRegressionSuite.xml
  SmokeTestSuite.xml
  ExceptionsSuite.xml
  DebugSuite.xml
  TableSuite.xml
```

Page objects hold locators and UI actions. Tests extend `BaseTest`, call a page object, and assert with TestNG. The shared wait in `BasePage` is 10 seconds.

## Stack

- Java 21
- Selenium Java 4.44.0
- TestNG 7.11.0
- Maven Surefire Plugin 3.5.4

## Adding a test

Put a new page object in `com.practicetestautomation.pageobjects`, extending `BasePage`. Put the test under `com.practicetestautomation.tests`, extending `BaseTest`. Include the method from a suite file under `src/test/resources/TestSuites/`. The Cursor rules for that work are `.cursor/rules/page-objects.mdc` and `.cursor/rules/new-tests.mdc`.

`.cursor/mcp.json` registers the Chrome DevTools MCP server for inspecting a page from Cursor. That server is separate from these Selenium tests and needs Node.js, npm, Chrome, and a display.
