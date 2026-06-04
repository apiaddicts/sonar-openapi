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
package org.apiaddicts.apitools.dosonarapi.api;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.openapi.OpenApiConfiguration;
import org.apiaddicts.apitools.dosonarapi.openapi.parser.OpenApiParser;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.YamlParser;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenApiCheckTest {
  private static class BaseRule extends OpenApiCheck {
    boolean visited = false;
    Set<String> visitedNodes = new HashSet<>();
    @Override
    protected void visitFile(JsonNode root) {
      visited = true;
    }
    @Override
    protected void visitNode(JsonNode node) {
      visitedNodes.add(node.getPointer());
    }

    @Override
    public Set<AstNodeType> subscribedKinds() {
      return Sets.newHashSet(OpenApi3Grammar.OPERATION, OpenApi3Grammar.PARAMETER);
    }
  }
  @Rule(key="RuleId1")
  private static class Rule1Check extends BaseRule {
  }
  @Rule(key="RuleId2")
  private static class Rule2Check extends BaseRule {
  }
  @Rule(key="RuleId3")
  private static class Rule3Check extends BaseRule {
  }

  @Test
  public void skips_rules_on_x_nosonar() {
    Rule1Check rule1 = new Rule1Check();
    Rule2Check rule2 = new Rule2Check();
    Rule3Check rule3 = new Rule3Check();

    TestOpenApiVisitorRunner.scanFile(new File(NoSonarCollectorTest.class.getResource("/nosonar-test.yaml").getFile()), rule1, rule2, rule3);

    assertThat(rule1.visited).isTrue();
    assertThat(rule2.visited).isTrue();
    assertThat(rule3.visited).isTrue();

    assertThat(rule1.visitedNodes).containsOnly("/paths/~1pets/get");
    assertThat(rule2.visitedNodes).isEmpty();
    assertThat(rule3.visitedNodes).containsOnly("/paths/~1pets/get", "/paths/~1pets/get/parameters/0");
  }

  private static class LineIssueCheck extends OpenApiCheck {
    @Override
    public Set<AstNodeType> subscribedKinds() {
      return Collections.emptySet();
    }

    @Override
    protected void visitFile(JsonNode root) {
      addLineIssue("line problem", 3);
    }
  }

  private static class NoAnnotationCheck extends OpenApiCheck {}

  @Test
  public void add_line_issue_creates_issue_on_given_line() {
    OpenApiConfiguration config = new OpenApiConfiguration(StandardCharsets.UTF_8, false);
    YamlParser parser = OpenApiParser.createV3(config);
    JsonNode root = parser.parse("openapi: \"3.0.0\"\ninfo:\n  title: T\n  version: 1.0\npaths: {}");
    OpenApiFile file = new OpenApiFile() {
      @Override public String content() { return ""; }
      @Override public String fileName() { return "test.yaml"; }
    };
    OpenApiVisitorContext ctx = new OpenApiVisitorContext(root, parser.getIssues(), file);

    LineIssueCheck check = new LineIssueCheck();
    List<PreciseIssue> issues = check.scanFileForIssues(ctx);
    assertThat(issues).hasSize(1);
    assertThat(issues.get(0).primaryLocation().startLine()).isEqualTo(3);
  }

  @Test
  public void no_rule_annotation_returns_empty_rule_id() {
    assertThat(new NoAnnotationCheck().getRuleId()).isEmpty();
  }
}
