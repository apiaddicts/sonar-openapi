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

import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.Trivia;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static com.sonar.sslr.api.GenericTokenType.COMMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OpenApiCheckVerifierTest {

  public static final int COMMENT_LINE = 4;
  public static final int COMMENT_COLUMN = 1;

  @Test
  public void can_capture_issue_parameters() {
    Trivia trivia = makeComment(" Noncompliant @+1 [[startColumn=1;endLine=+1;endColumn=2;effortToFix=4;secondary=-3,+4]] {{Issue message }}", COMMENT_LINE, COMMENT_COLUMN);
    OpenApiCheckVerifier verifier = new OpenApiCheckVerifier();

    verifier.collectExpectedIssue(trivia);

    assertThat(verifier.getCollectedIssues()).hasSize(1);
    TestIssue issue = verifier.getCollectedIssues().get(0);

    assertThat(issue.line()).isEqualTo(COMMENT_LINE + 1);
    assertThat(issue.endLine()).isEqualTo(issue.line() + 1);
    assertThat(issue.startColumn()).isEqualTo(1);
    assertThat(issue.endColumn()).isEqualTo(2);
    assertThat(issue.effortToFix()).isEqualTo(4);
    assertThat(issue.secondaryLines()).containsExactly(issue.line() - 3, issue.line() + 4);
    assertThat(issue.message()).isEqualTo("Issue message ");
  }

  @Test
  public void ignores_parameters_not_starting_with_keyword() {
    Trivia trivia = makeComment(" Something else Noncompliant @+1", COMMENT_LINE, COMMENT_COLUMN);
    OpenApiCheckVerifier verifier = new OpenApiCheckVerifier();

    verifier.collectExpectedIssue(trivia);

    assertThat(verifier.getCollectedIssues()).isEmpty();
  }

  @Test
  public void adjusts_issue_column_based_on_issue_next_line() {
    OpenApiCheckVerifier verifier = new OpenApiCheckVerifier();
    verifier.collectExpectedIssue(makeComment("Noncompliant @+1 [[startColumn=1;endLine=+1;endColumn=25]]", COMMENT_LINE, COMMENT_COLUMN));
    verifier.collectExpectedIssue(makeComment("  ^^^", COMMENT_LINE + 2, 0));

    assertThat(verifier.getCollectedIssues()).hasSize(1);
    TestIssue issue = verifier.getCollectedIssues().get(0);

    assertThat(issue.startColumn()).isEqualTo(3);
    assertThat(issue.endColumn()).isEqualTo(6);
  }

  @Test
  public void noncompliant_without_line_offset_uses_comment_line() {
    Trivia trivia = makeComment("Noncompliant {{message}}", COMMENT_LINE, COMMENT_COLUMN);
    OpenApiCheckVerifier verifier = new OpenApiCheckVerifier();
    verifier.collectExpectedIssue(trivia);

    assertThat(verifier.getCollectedIssues()).hasSize(1);
    assertThat(verifier.getCollectedIssues().get(0).line()).isEqualTo(COMMENT_LINE);
    assertThat(verifier.getCollectedIssues().get(0).message()).isEqualTo("message");
  }

  @Test
  public void noncompliant_with_absolute_line_number() {
    Trivia trivia = makeComment("Noncompliant @10", COMMENT_LINE, COMMENT_COLUMN);
    OpenApiCheckVerifier verifier = new OpenApiCheckVerifier();
    verifier.collectExpectedIssue(trivia);

    assertThat(verifier.getCollectedIssues()).hasSize(1);
    assertThat(verifier.getCollectedIssues().get(0).line()).isEqualTo(10);
  }

  @Test
  public void noncompliant_with_negative_line_offset() {
    Trivia trivia = makeComment("Noncompliant @-1", COMMENT_LINE, COMMENT_COLUMN);
    OpenApiCheckVerifier verifier = new OpenApiCheckVerifier();
    verifier.collectExpectedIssue(trivia);

    assertThat(verifier.getCollectedIssues()).hasSize(1);
    assertThat(verifier.getCollectedIssues().get(0).line()).isEqualTo(COMMENT_LINE - 1);
  }

  @Test
  public void empty_secondary_lines_list() {
    Trivia trivia = makeComment("Noncompliant [[secondary=]]", COMMENT_LINE, COMMENT_COLUMN);
    OpenApiCheckVerifier verifier = new OpenApiCheckVerifier();
    verifier.collectExpectedIssue(trivia);

    assertThat(verifier.getCollectedIssues()).hasSize(1);
    assertThat(verifier.getCollectedIssues().get(0).secondaryLines()).isEmpty();
  }

  @Test
  public void throws_on_invalid_param_without_equals() {
    OpenApiCheckVerifier verifier = new OpenApiCheckVerifier();
    assertThatThrownBy(() -> verifier.collectExpectedIssue(
        makeComment("Noncompliant [[invalidparam]]", COMMENT_LINE, COMMENT_COLUMN)))
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("Invalid param");
  }

  @Test
  public void throws_on_unknown_param_name() {
    OpenApiCheckVerifier verifier = new OpenApiCheckVerifier();
    assertThatThrownBy(() -> verifier.collectExpectedIssue(
        makeComment("Noncompliant [[unknownParam=5]]", COMMENT_LINE, COMMENT_COLUMN)))
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("Invalid param");
  }

  @Test
  public void precise_location_throws_when_no_preceding_issue() {
    OpenApiCheckVerifier verifier = new OpenApiCheckVerifier();
    assertThatThrownBy(() -> verifier.collectExpectedIssue(makeComment("^^^", COMMENT_LINE, 0)))
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("Invalid test file");
  }

  @Test
  public void precise_location_throws_when_not_on_next_line() {
    OpenApiCheckVerifier verifier = new OpenApiCheckVerifier();
    verifier.collectExpectedIssue(makeComment("Noncompliant", COMMENT_LINE, COMMENT_COLUMN));
    assertThatThrownBy(() -> verifier.collectExpectedIssue(makeComment("^^^", COMMENT_LINE + 3, 0)))
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("Invalid test file");
  }

  @Test
  public void precise_location_throws_when_column_not_zero() {
    OpenApiCheckVerifier verifier = new OpenApiCheckVerifier();
    verifier.collectExpectedIssue(makeComment("Noncompliant", COMMENT_LINE, COMMENT_COLUMN));
    assertThatThrownBy(() -> verifier.collectExpectedIssue(makeComment("^^^", COMMENT_LINE + 1, 2)))
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("column 1");
  }

  @Test
  public void verify_passes_when_no_issues_expected_and_none_reported() {
    OpenApiCheckVerifier.verify("src/test/resources/test-verify.yaml", new TestNoOpCheck(), true, false, false);
  }

  @Test
  public void verify_passes_when_issue_matches_annotation() {
    OpenApiCheckVerifier.verify("src/test/resources/test-verify-noncompliant.yaml", new TestPathsReportCheck(), true, false, false);
  }

  @Test
  public void verify_fails_when_expected_issue_not_reported() {
    assertThatThrownBy(() ->
      OpenApiCheckVerifier.verify("src/test/resources/test-verify-noncompliant.yaml", new TestNoOpCheck(), true, false, false))
      .isInstanceOf(AssertionError.class)
      .hasMessageContaining("Missing issue");
  }

  @Test
  public void verify_fails_when_unexpected_issue_reported() {
    assertThatThrownBy(() ->
      OpenApiCheckVerifier.verify("src/test/resources/test-verify.yaml", new TestPathsReportCheck(), true, false, false))
      .isInstanceOf(AssertionError.class)
      .hasMessageContaining("Unexpected issue");
  }

  private Trivia makeComment(String comment, int line, int column) {
    try {
      return Trivia.createComment(
        Token.builder().setType(COMMENT).setURI(new URI("unittest://tests"))
            .setLine(line).setColumn(column)
          .setValueAndOriginalValue(comment)
          .build());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
