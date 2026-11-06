---
name: automate-page-tests
description: >-
  Automate the test cases listed on a page under test. Use when the user
  gives a URL and wants those cases added to this Selenium project. Drive
  the page with the chrome-devtools MCP, pass every case by hand, then add
  a page object and TestNG class and prove them with Maven.
---

# Automate tests from a page URL

The user gives the URL of the page under test. That page lists the cases to automate. Do these steps in order. Stop at the first failed gate.

Before writing Java, read and follow `.cursor/rules/new-tests.mdc` and `.cursor/rules/page-objects.mdc`. Do not restate them. Those files decide class shape, locators, waits, assertions, groups, and suite includes.

## 1. URL

If the user did not give a URL, ask for it and stop. Do not pick a page yourself.

## 2. Open the page

Use the `chrome-devtools` MCP server from `.cursor/mcp.json`. If that server is not connected, stop and say so. Do not use another browser tool.

- Call `new_page` with the user URL.
- Call `list_pages`. Use the `pageId` whose URL is the one the user gave. Ignore the `about:blank` tab.
- Call `take_snapshot` for that `pageId`. Prefer a snapshot over a screenshot.

`pageId` is required on page-scoped tools. `click` and `fill` also need the `uid` from the latest snapshot. Prefer `fill_form` when a case fills several fields. After the page changes, snapshot again before using another `uid`.

## 3. Read the cases

From the snapshot, write down every test case the page lists: name, steps, data, and expected result. Do not add cases the page does not list. If one snapshot cuts the list off, snapshot the rest before continuing.

## 4. Learn the page

From the snapshots, note the controls those cases use. This is still discovery. Do not write page objects or tests yet.

## 5. Manual gate

Run every listed case yourself with the chrome-devtools tools, in the order the page lists them. A case passes only when the latest snapshot shows the expected result.

Record pass or fail for each case. If any case fails, stop. Do not add Java for a failing case. Report the case name, the steps you ran, and what the snapshot showed.

## 6. Automate only the cases that passed

Add the page object and the test class for those cases, following the two rules. One `@Test` per case. Values you typed or asserted come from the case you just ran.

Suite wiring follows the rules. For a new area, add a suite file next to the existing ones that includes the whole new class with `browser` `chrome`, using the same `<test>` shape as `ExceptionsSuite.xml`. Add that same `<test>` to `FullRegressionSuite.xml`. When the rules say the cases belong on `LoginTests` or `ExceptionsTests`, edit that class and its existing includes instead of adding a second class.

## 7. Run

Run:

```bash
mvn test -DsuiteXmlFile=<SuiteFile>.xml
```

`<SuiteFile>.xml` is the file name only. The command must exit 0 and every new test must pass. Fix the code and rerun. Do not delete a case that passed by hand, and do not weaken an assertion to force a pass. A missing Maven, a missing Chrome, or a failed run is not a pass.

## 8. Review and report

Re-read both rules and the diff. Then report:

- The URL
- Each case found on the page
- The manual result for each case, including what the snapshot showed
- Files added or edited
- The Maven command, its exit code, and the pass and fail counts
- Any place the diff breaks a rule
- Any listed case that was not automated, and why

Do not say the suite passed unless step 7 exited 0.
