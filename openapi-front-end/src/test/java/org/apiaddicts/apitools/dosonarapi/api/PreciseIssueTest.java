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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;

public class PreciseIssueTest {

  @Test
  public void compare_equals_objects() {
    PreciseIssue preciseIssue1 = new PreciseIssue(IssueLocation.atLineLevel(null, 42000)).withCost(5);
    PreciseIssue preciseIssue2 = new PreciseIssue(IssueLocation.atLineLevel(null, 42000)).withCost(5);
    assertThat(preciseIssue1.equals(preciseIssue2)).isTrue();
    assertThat(preciseIssue1.hashCode() == preciseIssue2.hashCode()).isTrue();
  }

  @Test
  public void not_equal_to_non_precise_issue() {
    PreciseIssue issue = new PreciseIssue(IssueLocation.atLineLevel("msg", 1));
    assertThatObject(issue).isNotEqualTo("not an issue");
    assertThatObject(issue).isNotEqualTo(null);
  }

  @Test
  public void not_equal_when_costs_differ() {
    PreciseIssue issue1 = new PreciseIssue(IssueLocation.atLineLevel("msg", 1)).withCost(1);
    PreciseIssue issue2 = new PreciseIssue(IssueLocation.atLineLevel("msg", 1)).withCost(2);
    assertThat(issue1.equals(issue2)).isFalse();
  }

  @Test
  public void not_equal_when_primary_location_differs() {
    PreciseIssue issue1 = new PreciseIssue(IssueLocation.atLineLevel("msg", 1));
    PreciseIssue issue2 = new PreciseIssue(IssueLocation.atLineLevel("msg", 2));
    assertThat(issue1.equals(issue2)).isFalse();
  }

  @Test
  public void secondary_location_via_issue_location() {
    IssueLocation loc = IssueLocation.atLineLevel("secondary", 5);
    PreciseIssue issue = new PreciseIssue(IssueLocation.atLineLevel("primary", 1));
    issue.secondary(loc);
    assertThat(issue.secondaryLocations()).hasSize(1);
    assertThat(issue.secondaryLocations().get(0)).isEqualTo(loc);
  }

  @Test
  public void cost_is_null_by_default() {
    PreciseIssue issue = new PreciseIssue(IssueLocation.atLineLevel("msg", 1));
    assertThat(issue.cost()).isNull();
  }

  @Test
  public void hashcode_without_cost() {
    PreciseIssue issue1 = new PreciseIssue(IssueLocation.atLineLevel("msg", 1));
    PreciseIssue issue2 = new PreciseIssue(IssueLocation.atLineLevel("msg", 1));
    assertThat(issue1.hashCode()).isEqualTo(issue2.hashCode());
  }

  @Test
  public void equals_with_null_primary_location() {
    PreciseIssue issue1 = new PreciseIssue(null);
    PreciseIssue issue2 = new PreciseIssue(null);
    assertThat(issue1.equals(issue2)).isTrue();
    assertThat(issue1.hashCode()).isEqualTo(issue2.hashCode());
  }

  @Test
  public void null_primary_location_not_equal_to_non_null() {
    PreciseIssue issue1 = new PreciseIssue(null);
    PreciseIssue issue2 = new PreciseIssue(IssueLocation.atLineLevel("msg", 1));
    assertThat(issue1.equals(issue2)).isFalse();
    assertThat(issue2.equals(issue1)).isFalse();
  }

  @Test
  public void secondary_location_via_node() {
    java.nio.charset.Charset utf8 = java.nio.charset.StandardCharsets.UTF_8;
    org.apiaddicts.apitools.dosonarapi.openapi.OpenApiConfiguration config =
        new org.apiaddicts.apitools.dosonarapi.openapi.OpenApiConfiguration(utf8, true);
    org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.YamlParser parser =
        org.apiaddicts.apitools.dosonarapi.openapi.parser.OpenApiParser.createV2(config);
    org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode root =
        parser.parse("swagger: \"2.0\"\ninfo:\n  version: 1.0.0\n  title: T\npaths:\n  /pets: {}");
    org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode node = root.at("/paths/~1pets").value();
    PreciseIssue issue = new PreciseIssue(IssueLocation.atLineLevel("primary", 1));
    issue.secondary(node, "secondary message");
    assertThat(issue.secondaryLocations()).hasSize(1);
  }
}
