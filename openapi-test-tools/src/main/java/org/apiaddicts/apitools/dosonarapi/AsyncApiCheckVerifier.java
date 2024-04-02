/*
 * doSonarAPI: SonarQube OpenAPI Plugin
 * Copyright (C) 2021-2022 Apiaddicts
 * contacta AT apiaddicts DOT org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.apiaddicts.apitools.dosonarapi;

import com.google.common.base.Splitter;
import com.google.common.collect.Ordering;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.Trivia;
import org.apiaddicts.apitools.dosonarapi.api.IssueLocation;
import org.apiaddicts.apitools.dosonarapi.api.AsyncApiCheck;
import org.apiaddicts.apitools.dosonarapi.api.AsyncApiVisitor;
import org.apiaddicts.apitools.dosonarapi.api.PreciseIssue;
import org.apiaddicts.apitools.dosonarapi.api.TestAsyncApiVisitorRunner;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.junit.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;

public class AsyncApiCheckVerifier {

  private List<TestIssue> expectedIssues = new ArrayList<>();

  public static List<PreciseIssue> scanFileForIssues(File file, AsyncApiCheck check) {
    return check.scanFileForIssues(TestAsyncApiVisitorRunner.createContext(file));
  }

  public static void verify(String path, AsyncApiCheck check) {
    AsyncApiCheckVerifier verifier = new AsyncApiCheckVerifier();
    AsyncApiVisitor collector = new ExpectedIssueCollector(verifier);
    File file = new File(path);
    TestAsyncApiVisitorRunner.scanFileForComments(file, collector);

    Iterator<PreciseIssue> actualIssues = getActualIssues(file, check);
    verifier.checkIssues(actualIssues);

    if (actualIssues.hasNext()) {
      PreciseIssue issue = actualIssues.next();
      throw new AssertionError("Unexpected issue at line " + line(issue) + ": \"" + issue.primaryLocation().message() + "\"");
    }
  }

  private static int line(PreciseIssue issue) {
    return issue.primaryLocation().startLine();
  }

  private void checkIssues(Iterator<PreciseIssue> actualIssues) {
    for (TestIssue expected : expectedIssues) {
      if (actualIssues.hasNext()) {
        verifyIssue(expected, actualIssues.next());
      } else {
        throw new AssertionError("Missing issue at line " + expected.line());
      }
    }
  }

  private void verifyIssue(TestIssue expected, PreciseIssue actual) {
    if (line(actual) > expected.line()) {
      fail("Missing issue at line " + expected.line());
    }
    if (line(actual) < expected.line()) {
      fail("Unexpected issue at line " + line(actual) + ": \"" + actual.primaryLocation().message() + "\"");
    }
    if (expected.message() != null) {
      assertThat(actual.primaryLocation().message()).as("Bad message at line " + expected.line()).isEqualTo(expected.message());
    }
    if (expected.effortToFix() != null) {
      assertThat(actual.cost().intValue()).as("Bad effortToFix at line " + expected.line()).isEqualTo(expected.effortToFix());
    }
    if (expected.startColumn() != null) {
      assertThat(actual.primaryLocation().startLineOffset()).as("Bad start column at line " + expected.line()).isEqualTo(expected.startColumn());
    }
    if (expected.endColumn() != null) {
      assertThat(actual.primaryLocation().endLineOffset()).as("Bad end column at line " + expected.line()).isEqualTo(expected.endColumn());
    }
    if (expected.endLine() != null) {
      assertThat(actual.primaryLocation().endLine()).as("Bad end line at line " + expected.line()).isEqualTo(expected.endLine());
    }
    if (expected.secondaryLines() != null) {
      assertThat(secondary(actual)).as("Bad secondary locations at line " + expected.line()).isEqualTo(expected.secondaryLines());
    }
  }

  private static List<Integer> secondary(PreciseIssue issue) {
    List<Integer> result = new ArrayList<>();
    for (IssueLocation issueLocation : issue.secondaryLocations()) {
      result.add(issueLocation.startLine());
    }
    return Ordering.natural().sortedCopy(result);
  }

  private static Iterator<PreciseIssue> getActualIssues(File file, AsyncApiCheck check) {
    List<PreciseIssue> issues = scanFileForIssues(file, check);
    List<PreciseIssue> sortedIssues = Ordering.natural().<PreciseIssue>onResultOf(AsyncApiCheckVerifier::line).sortedCopy(issues);
    return sortedIssues.iterator();
  }

  public void collectExpectedIssue(Trivia trivia) {
    String text = trivia.getToken().getValue().trim();
    String marker = "Noncompliant";
    if (text.startsWith(marker)) {
      int issueLine = trivia.getToken().getLine();
      String paramsAndMessage = text.substring(marker.length()).trim();
      if (paramsAndMessage.startsWith("@")) {
        String[] spaceSplit = paramsAndMessage.split("[\\s\\[{]", 2);
        String lineMarker = spaceSplit[0].substring(1);
        issueLine = lineValue(issueLine, lineMarker);
        paramsAndMessage = spaceSplit.length > 1 ? spaceSplit[1] : "";
      }
      TestIssue issue = TestIssue.create(null, issueLine);
      if (paramsAndMessage.startsWith("[[")) {
        int endIndex = paramsAndMessage.indexOf("]]");
        addParams(issue, paramsAndMessage.substring(2, endIndex));
        paramsAndMessage = paramsAndMessage.substring(endIndex + 2).trim();
      }
      if (paramsAndMessage.startsWith("{{")) {
        int endIndex = paramsAndMessage.indexOf("}}");
        String message = paramsAndMessage.substring(2, endIndex);
        issue.message(message);
      }
      expectedIssues.add(issue);
    } else if (text.startsWith("^")) {
      addPreciseLocation(trivia);
    }
  }

  private static void addParams(TestIssue issue, String params) {
    for (String param : Splitter.on(';').split(params)) {
      int equalIndex = param.indexOf('=');
      String name = param.substring(0, equalIndex);
      String value = param.substring(equalIndex + 1);
      switch (name.toLowerCase()) {
        case "efforttofix":
          issue.effortToFix(Integer.valueOf(value));
          break;
        case "startcolumn":
          issue.startColumn(Integer.valueOf(value));
          break;
        case "endcolumn":
          issue.endColumn(Integer.valueOf(value));
          break;
        case "endline":
          issue.endLine(lineValue(issue.line(), value));
          break;
        case "secondary":
          addSecondaryLines(issue, value);
          break;
        default:
          throw new IllegalStateException("Invalid param at line 1: " + name);
      }
    }
  }

  private static void addSecondaryLines(TestIssue issue, String value) {
    List<Integer> secondaryLines = new ArrayList<>();
    if (!"".equals(value)) {
      for (String secondary : Splitter.on(',').split(value)) {
        secondaryLines.add(lineValue(issue.line(), secondary));
      }
    }
    issue.secondary(secondaryLines);
  }

  private static int lineValue(int baseLine, String shift) {
    if (shift.startsWith("+")) {
      return baseLine + Integer.parseInt(shift.substring(1));
    } else if (shift.startsWith("-")) {
      return baseLine - Integer.parseInt(shift.substring(1));
    } else {
      return Integer.parseInt(shift);
    }
  }

  private void addPreciseLocation(Trivia trivia) {
    Token token = trivia.getToken();
    int line = token.getLine();
    String text = token.getValue();
    if (token.getColumn() > 1) {
      throw new IllegalStateException("Line " + line + ": comments asserting a precise location should start at column 1");
    }
    if (expectedIssues.isEmpty()) {
      throw new IllegalStateException(String.format("Invalid test file: a precise location is provided at line %s but no issue is asserted at line %s", line, line - 1));
    }
    TestIssue issue = expectedIssues.get(expectedIssues.size() - 1);
    if (issue.line() != line - 1) {
      throw new IllegalStateException(String.format("Invalid test file: a precise location is provided at line %s but no issue is asserted at line %s", line, line - 1));
    }
    issue.endLine(issue.line());
    issue.startColumn(text.indexOf('^') + 1);
    issue.endColumn(text.lastIndexOf('^') + 2);
  }

  private static final class ExpectedIssueCollector extends AsyncApiVisitor {
    private final AsyncApiCheckVerifier verifier;

    private ExpectedIssueCollector(AsyncApiCheckVerifier verifier) {
      this.verifier = verifier;
    }

    @Override
    protected void visitToken(Token token) {
      for (Trivia trivia : token.getTrivia()) {
        verifier.collectExpectedIssue(trivia);
      }
    }
  }
}
