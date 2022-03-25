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
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

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
}
