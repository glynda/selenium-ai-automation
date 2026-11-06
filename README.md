# Selenium AI Automation

TestNG UI tests for the live site [practicetestautomation.com](https://practicetestautomation.com/). They cover the login page, the exceptions page, and the course table.

## Run

JDK 21, Maven, Chrome, and Firefox. Tests open a headed browser, so a display is required. Selenium Manager downloads the driver.

```bash
mvn test
mvn test -DsuiteXmlFile=SmokeTestSuite.xml
mvn test -DsuiteXmlFile=ExceptionsSuite.xml
mvn test -DsuiteXmlFile=TableSuite.xml
```

`mvn test` runs `FullRegressionSuite.xml`: login, exceptions, and the table. The other commands run one area. `DebugSuite.xml` runs each exceptions test on its own. Pass the file name only. Suites are in `src/test/resources/TestSuites/`.
