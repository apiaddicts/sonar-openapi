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

import java.nio.charset.StandardCharsets;

import org.apiaddicts.apitools.dosonarapi.api.IssueLocation;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.openapi.OpenApiConfiguration;
import org.apiaddicts.apitools.dosonarapi.openapi.parser.OpenApiParser;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.YamlParser;

import static org.assertj.core.api.Assertions.assertThat;

public class IssueLocationTest {

  private static final String MESSAGE = "message";

  private YamlParser parser = OpenApiParser.createV2(new OpenApiConfiguration(StandardCharsets.UTF_8, true));

  @Test
  public void can_create_file_level_issue() {
    IssueLocation issueLocation = IssueLocation.atFileLevel(MESSAGE);
    assertThat(issueLocation.message()).isEqualTo(MESSAGE);
    assertThat(issueLocation.startLine()).isEqualTo(IssueLocation.UNDEFINED_LINE);
    assertThat(issueLocation.endLine()).isEqualTo(IssueLocation.UNDEFINED_LINE);
    assertThat(issueLocation.startLineOffset()).isEqualTo(IssueLocation.UNDEFINED_OFFSET);
    assertThat(issueLocation.endLineOffset()).isEqualTo(IssueLocation.UNDEFINED_OFFSET);
    assertThat(issueLocation.pointer()).isEmpty();
  }

  @Test
  public void can_create_line_level_issue() {
    IssueLocation issueLocation = IssueLocation.atLineLevel(MESSAGE, 42);
    assertThat(issueLocation.message()).isEqualTo(MESSAGE);
    assertThat(issueLocation.startLine()).isEqualTo(42);
    assertThat(issueLocation.endLine()).isEqualTo(42);
    assertThat(issueLocation.startLineOffset()).isEqualTo(IssueLocation.UNDEFINED_OFFSET);
    assertThat(issueLocation.endLineOffset()).isEqualTo(IssueLocation.UNDEFINED_OFFSET);
  }

  @Test
  public void can_create_single_node_issue() {
    JsonNode root = parser.parse("swagger: \"2.0\"\n" +
      "info:\n" +
      "  version: 1.0.0\n" +
      "  title: Swagger Petstore\n" +
      "paths:\n" +
      "  /pets: {}");
    JsonNode node = root.at("/paths/~1pets").value();
    IssueLocation issueLocation = IssueLocation.preciseLocation(MESSAGE, node);
    assertThat(issueLocation.message()).isEqualTo(MESSAGE);
    assertThat(issueLocation.startLine()).isEqualTo(6);
    assertThat(issueLocation.endLine()).isEqualTo(6);
    assertThat(issueLocation.startLineOffset()).isEqualTo(9);
    assertThat(issueLocation.endLineOffset()).isEqualTo(11);
    assertThat(issueLocation.pointer()).isEqualTo("/paths/pets");
  }

  @Test
  public void can_create_multiple_nodes_issue() {
    JsonNode root = parser.parse("swagger: \"2.0\"\n" +
      "info:\n" +
      "  version: 1.0.0\n" +
      "  title: Swagger Petstore\n" +
      "paths:\n" +
      "  /pets: {}");
    JsonNode firstNode = root.at("/info/version").key();
    JsonNode lastNode = (JsonNode)root.getFirstDescendant(OpenApi2Grammar.PATH);
    IssueLocation issueLocation = IssueLocation.preciseLocation(firstNode, lastNode, MESSAGE);
    assertThat(issueLocation.message()).isEqualTo(MESSAGE);
    assertThat(issueLocation.startLine()).isEqualTo(3);
    assertThat(issueLocation.endLine()).isEqualTo(6);
    assertThat(issueLocation.startLineOffset()).isEqualTo(2);
    assertThat(issueLocation.endLineOffset()).isEqualTo(11);
  }

  @Test
  public void compare_line_level_equals_objects(){
    IssueLocation issueLocation1 = IssueLocation.atLineLevel(MESSAGE, 42000);
    IssueLocation issueLocation2 = IssueLocation.atLineLevel(MESSAGE, 42000);
    assertThat(issueLocation1.equals(issueLocation2)).isTrue();
    assertThat(issueLocation1.hashCode() == issueLocation2.hashCode()).isTrue();
  }

  @Test
  public void compare_line_level_differents_class_objects(){
    IssueLocation issueLocation1 = IssueLocation.atLineLevel(MESSAGE, 42000);
    assertThat(issueLocation1.equals(new Object())).isFalse();
  }

  @Test
  public void compare_line_level_with_null_messages(){
    IssueLocation issueLocation1 = IssueLocation.atLineLevel(null, 42000);
    IssueLocation issueLocation2 = IssueLocation.atLineLevel(MESSAGE , 42000);

    assertThat(issueLocation1.equals(issueLocation2)).isFalse();;
  }

  @Test
  public void compare_file_equals_objects(){
    IssueLocation issueLocation1 = IssueLocation.atFileLevel(MESSAGE);
    IssueLocation issueLocation2 = IssueLocation.atFileLevel(MESSAGE);
    assertThat(issueLocation1.equals(issueLocation2)).isTrue();
    assertThat(issueLocation1.hashCode() == issueLocation2.hashCode()).isTrue();
  }

  @Test
  public void compare_precise_location_equals_objects() {
    JsonNode root1 = parser.parse("swagger: \"2.0\"\n" +
      "info:\n" +
      "  version: 1.0.0\n" +
      "  title: Swagger Petstore\n" +
      "paths:\n" +
      "  /pets: {}");
    JsonNode firstNode1 = root1.at("/info/version").key();
    JsonNode lastNode1 = (JsonNode)root1.getFirstDescendant(OpenApi2Grammar.PATH);
    IssueLocation issueLocation1 = IssueLocation.preciseLocation(firstNode1, lastNode1, MESSAGE);

    JsonNode root2 = parser.parse("swagger: \"2.0\"\n" +
      "info:\n" +
      "  version: 1.0.0\n" +
      "  title: Swagger Petstore\n" +
      "paths:\n" +
      "  /pets: {}");
    JsonNode firstNode2 = root2.at("/info/version").key();
    JsonNode lastNode2 = (JsonNode)root2.getFirstDescendant(OpenApi2Grammar.PATH);
    IssueLocation issueLocation2 = IssueLocation.preciseLocation(firstNode2, lastNode2, MESSAGE);

    assertThat(issueLocation1.hashCode() == issueLocation2.hashCode()).isTrue();
    assertThat(issueLocation1.equals(issueLocation2)).isTrue();

  }

  @Test
  public void compare_precise_location_differents_objects(){
    JsonNode root1 = parser.parse("swagger: \"2.0\"\n" +
      "info:\n" +
      "  version: 1.0.0\n" +
      "  title: Swagger Petstore\n" +
      "paths:\n" +
      "  /pets: {}");
    JsonNode firstNode1 = root1.at("/info/version").key();
    JsonNode lastNode1 = (JsonNode)root1.getFirstDescendant(OpenApi2Grammar.PATH);
    IssueLocation issueLocation1 = IssueLocation.preciseLocation(firstNode1, lastNode1, MESSAGE);

    JsonNode root2 = parser.parse("swagger: \"2.0\"\n" +
      "info:\n" +
      "  title: Swagger Petstore\n" +
      "  version: 1.0.0\n" +
      "paths:\n" +
      "  /pets: {}");
    JsonNode firstNode2 = root2.at("/info/version").key();
    IssueLocation issueLocation2 = IssueLocation.preciseLocation(firstNode2, lastNode1, MESSAGE);

    assertThat(issueLocation1.equals(issueLocation2)).isFalse();
  }


}
