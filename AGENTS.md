# AGENTS.md

Context for AI coding sessions and contributors. This repo has no README and no CI.

## Purpose

Maven TestNG UI tests against the public practice site `https://practicetestautomation.com/`. Page objects cover the login practice page and the exceptions practice page. This repository does not contain the site under test; every test drives the live site in a local browser.

Maven coordinates: `org.example:SeleniumForBeginners:1.0-SNAPSHOT`. The GitHub repository is `selenium-ai-automation`.

## Stack

- Java 21 (`maven.compiler.source` and `maven.compiler.target` in `pom.xml`). Source encoding is UTF-8. `.idea/misc.xml` sets the IntelliJ project JDK to 21.
- Selenium Java `4.44.0` (`org.seleniumhq.selenium:selenium-java`), default `compile` scope.
- TestNG `7.11.0` (`org.testng:testng`), default `compile` scope. It is not `test`-scoped.
- `maven-surefire-plugin` `3.5.4` and `maven-clean-plugin` `3.3.2` are declared only under `<pluginManagement>`.
- No Maven wrapper, no pinned `maven-compiler-plugin`, no WebDriverManager, no browser options, no Selenium Grid, and no Docker setup.

`BaseTest` starts `ChromeDriver`, `EdgeDriver`, or `FirefoxDriver` with no options, so sessions are headed. Selenium Manager (bundled with Selenium 4) resolves the driver binary. A matching browser must already be installed, and the process needs a display.

## Layout

```
.cursor/mcp.json          # Chrome DevTools MCP server for Cursor
.cursor/rules/new-tests.mdc
.cursor/rules/page-objects.mdc
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
  FullRegressionSuite.xml    # default Surefire suite
  SmokeTestSuite.xml
  ExceptionsSuite.xml
  DebugSuite.xml
  TableSuite.xml
```

There is no `src/main/resources` directory. Maven output is `target/` (gitignored). IntelliJ output is `out/` (not gitignored). `.idea/misc.xml` and `.idea/encodings.xml` are committed.

## Run tests

Requires JDK 21, Maven on `PATH`, the browser the suite asks for, outbound HTTPS to `practicetestautomation.com`, and a display.

```bash
mvn test
mvn test -DsuiteXmlFile=SmokeTestSuite.xml
mvn test -DsuiteXmlFile=ExceptionsSuite.xml
mvn test -DsuiteXmlFile=DebugSuite.xml
mvn test -DsuiteXmlFile=TableSuite.xml
```

Surefire runs one suite file: `src/test/resources/TestSuites/${suiteXmlFile}`. The property defaults to `FullRegressionSuite.xml`. Pass the file name only. There is no `testng.xml` at the default TestNG location.

| Suite | What it runs |
| --- | --- |
| `FullRegressionSuite.xml` | Positive login on `chrome` and `firefox`. Invalid username on `firefox`. Invalid password with browser value `safari`. All of `ExceptionsTests` on `chrome`. All of `TableTests` on `chrome`. |
| `SmokeTestSuite.xml` | `LoginTests` methods in the `smoke` group. No `browser` parameter, so Chrome. |
| `ExceptionsSuite.xml` | All of `ExceptionsTests` on `chrome`. |
| `DebugSuite.xml` | Each exceptions test method in its own `<test>`. No `browser` parameter, so Chrome. |
| `TableSuite.xml` | All of `TableTests` on `chrome`. |

## Inspect pages

`.cursor/mcp.json` registers the `chrome-devtools` server as `npx -y chrome-devtools-mcp@latest`. Cursor launches that process over stdio. The server starts Chrome only when a tool needs a browser, using the installed stable Chrome in headed mode. This is separate from the Selenium suite: `BaseTest` still creates its own `ChromeDriver`, `EdgeDriver`, or `FirefoxDriver`.

Requirements beyond the Maven test run: Node.js LTS, npm, current stable Chrome, and a display. The npm package tag is `@latest`, so the tool list can change when the server updates.

Page-scoped tools require `pageId`. Open a URL with `new_page` (argument `url`) or `navigate_page`, then read ids from `list_pages`. A fresh browser already has an `about:blank` tab, so the page from `new_page` is not id 1. Inspect with `take_snapshot` (accessibility tree; prefer this over a screenshot), `take_screenshot`, `list_console_messages`, and `list_network_requests`.

## Cloud Agent environment

Sessions are headed. Chrome and Firefox are installed. Xvfb listens on display `:99` at boot. `/usr/local/bin/mvn` checks that `DISPLAY` is a live X server and, when it is missing or dead, starts Xvfb on `:99` before calling `/usr/bin/mvn`. `/etc/profile.d/selenium-display.sh` and `/etc/mavenrc` set `DISPLAY=:99` when it is unset. The DevTools server is not launched by Maven, so it only sees `DISPLAY` when the process that starts `npx` has a live display.

Ubuntu's `firefox` package is a snap stub. This environment installs Mozilla's deb and pins it ahead of Ubuntu. Edge is not installed. The full-regression value `safari` still falls back to Chrome.

Node.js 26 LTS is on the default PATH as `/usr/local/bin/node`, `/usr/local/bin/npm`, and `/usr/local/bin/npx` for `.cursor/mcp.json`.

Selenium Manager downloads the matching driver on first use into `~/.cache/selenium`. That needs HTTPS to Maven Central, `practicetestautomation.com`, and the driver hosts. A Chrome DevTools version warning for the installed Chrome build does not fail these tests.

## Conventions

- When adding a test or suite entry, follow `.cursor/rules/new-tests.mdc`. When adding a page object, follow `.cursor/rules/page-objects.mdc`.
- Put UI actions and locators on a page object under `com.practicetestautomation.pageobjects`, extending `BasePage`. Tests live under `com.practicetestautomation.tests`, extend `BaseTest`, and call page objects.
- Keep locators as private `By` fields. Reuse the `BasePage` helpers below instead of constructing another `WebDriverWait`. The shared wait is 10 seconds.
  - `waitForElement` waits until visible.
  - `waitForElementToBeClickable` waits until clickable.
  - `waitForIsDisplayed` returns `true` when the element becomes visible, and `false` on `TimeoutException`.
  - `waitForInvisibilityOfElement` returns `true` when the element is gone, and `false` on `TimeoutException`.
  - `isDisplayed` returns `false` when `findElement` throws `NoSuchElementException`.
- Suites select methods and supply parameters. Follow the existing `<parameter>` pattern for `browser` and for negative-login data.
- `BaseTest.setup` reads TestNG parameter `browser` (`@Optional("chrome")`). Values `chrome`, `edge`, and `firefox` are supported, case-insensitively. Any other value logs a warning and starts Chrome. The full-regression invalid-password test passes `safari`, which takes that fallback. No suite currently passes `edge`.
- `@BeforeMethod` and `@AfterMethod` are `alwaysRun = true`. Teardown calls `driver.quit()`.
- Logging is `java.util.logging` at `INFO`. `BaseTest` always builds the logger with `LoginTests.class.getName()`, including for exceptions and table tests.
- `loginFunctionalityTest` groups: `positive`, `regression`, `smoke`. `negativeLoginTest` groups: `negative`, `regression`. It requires suite parameters `username`, `password`, and `expectedErrorMessage`. Including that method without those parameters fails. No suite filters on the `regression` group; full regression includes methods by name.
- Positive login uses the practice site's published student credentials, hardcoded in `LoginTests`. Negative username and password cases and their expected `#error` text are the parameters in `FullRegressionSuite.xml`.
- Login page: `https://practicetestautomation.com/practice-test-login/`. Success URL: `https://practicetestautomation.com/logged-in-successfully/`. Logout is the link text `Log out`.
- Exceptions page: `https://practicetestautomation.com/practice-test-exceptions/`.
- `noSuchElementExceptionsTest` and `timeOutExceptionTest` currently perform the same steps: click Add, wait for row 2, assert confirmation `Row 2 was added`.
- `elementNotInteractableExceptionTest` enters `sampleText` in row 2, clicks the row 2 Save button, and expects `Row 2 was saved`.
- `invalidElementStateExceptionTest` clicks Edit, enters `sampleText` in row 1, clicks Save, and expects `Row 1 was saved`.
- `staleElementReferenceExceptionTest` asserts `#instructions` is displayed, clicks Add, then calls `isInstructionsNoLongerDisplayedAfterWait()`. That method waits for invisibility of `//button[@id='edit_btn']`. The instructions locator is `#instructions`.
- `ExceptionsTests` still contains large commented blocks of the earlier raw-Selenium steps. The uncommented statements are the tests that run.
- Table page: `https://practicetestautomation.com/practice-test-table/`. `TableTests` covers the eight cases published on that page. The page sorts by ID on load, so filtered rows are asserted in that order unless the test changes Sort by. `languageFilterJavaTest` selects Java and expects the six Java courses. `levelFilterBeginnerOnlyTest` unchecks Intermediate and Advanced and expects the five Beginner courses. `minEnrollmentsFilterTest` selects the 10,000+ minimum and expects every visible enrollment to be at least 10,000. `combinedFiltersTest` applies Python, Beginner, and 10,000+ and expects only Selenium with Python. `noResultsStateTest` selects Python with only Advanced checked, expects the text `No matching courses.`, then selects Any and expects the two Advanced courses. `resetButtonTest` expects Reset to be hidden, to appear after selecting Java, and after the click to restore Language Any, all three levels, Min enrollments Any, a hidden Reset button, and all nine courses. `sortByEnrollmentsTest` expects numeric ascending enrollments `1365, 2207, 2667, 6678, 8254, 10705, 14762, 16452, 64284`. `sortByCourseNameTest` expects A-to-Z course names, then the same order for the Java subset after selecting Java. `TablePage` exposes `Language`, `Level`, `MinEnrollments`, and `SortBy`. Language and level locators are scoped to the fieldset legend. Enrollment options use `role='option'` and `data-value`. Course cells use both `data-col` and `headers`. Enrollment text is parsed by removing commas and spaces, then parsing the remainder as an integer.
