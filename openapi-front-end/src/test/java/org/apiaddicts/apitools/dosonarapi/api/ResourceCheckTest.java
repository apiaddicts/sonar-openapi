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
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.apiaddicts.apitools.dosonarapi.openapi.OpenApiConfiguration;
import org.apiaddicts.apitools.dosonarapi.openapi.parser.OpenApiParser;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.YamlParser;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Theories.class)
public class ResourceCheckTest {

  private static class RecordingResourceCheck extends ResourceCheck {
    final List<String> visitedPaths = new ArrayList<>();

    @Override
    protected void visitResource(JsonNode node) {
      visitedPaths.add(node.key().getTokenValue());
    }
  }

  private static OpenApiVisitorContext createV2Context(String yaml) {
    OpenApiConfiguration config = new OpenApiConfiguration(StandardCharsets.UTF_8, true);
    YamlParser parser = OpenApiParser.createV2(config);
    JsonNode root = parser.parse(yaml);
    OpenApiFile file = new OpenApiFile() {
      @Override public String content() { return yaml; }
      @Override public String fileName() { return "test.yaml"; }
    };
    return new OpenApiVisitorContext(root, parser.getIssues(), file);
  }

  @DataPoints
  public static String[] yamlsWithNoResourcePaths() {
    return new String[] {
      "swagger: \"2.0\"\n" +
      "info:\n" +
      "  version: 1.0.0\n" +
      "  title: Test\n" +
      "paths:\n" +
      "  /pets/{petId}:\n" +
      "    get:\n" +
      "      responses:\n" +
      "        '200':\n" +
      "          description: ok\n",

      "swagger: \"2.0\"\n" +
      "info:\n" +
      "  version: 1.0.0\n" +
      "  title: Test\n" +
      "paths:\n" +
      "  /pets/:\n" +
      "    get:\n" +
      "      responses:\n" +
      "        '200':\n" +
      "          description: ok\n",

      "swagger: \"2.0\"\n" +
      "info:\n" +
      "  version: 1.0.0\n" +
      "  title: Test\n" +
      "paths:\n" +
      "  /{entity}:\n" +
      "    get:\n" +
      "      responses:\n" +
      "        '200':\n" +
      "          description: ok\n"
    };
  }

  @Theory
  public void does_not_visit_non_resource_paths(String yaml) {
    RecordingResourceCheck check = new RecordingResourceCheck();
    check.scanFileForIssues(createV2Context(yaml));
    assertThat(check.visitedPaths).isEmpty();
  }

  @Test
  public void visits_resource_paths_only() {
    String yaml =
      "swagger: \"2.0\"\n" +
      "info:\n" +
      "  version: 1.0.0\n" +
      "  title: Test\n" +
      "paths:\n" +
      "  /pets:\n" +
      "    get:\n" +
      "      responses:\n" +
      "        '200':\n" +
      "          description: ok\n" +
      "  /pets/{petId}:\n" +
      "    get:\n" +
      "      responses:\n" +
      "        '200':\n" +
      "          description: ok\n";

    RecordingResourceCheck check = new RecordingResourceCheck();
    check.scanFileForIssues(createV2Context(yaml));

    assertThat(check.visitedPaths).contains("/pets").doesNotContain("/pets/{petId}");
  }

  @Test
  public void visits_sub_resource_paths() {
    String yaml =
      "swagger: \"2.0\"\n" +
      "info:\n" +
      "  version: 1.0.0\n" +
      "  title: Test\n" +
      "paths:\n" +
      "  /pets/{petId}/tags:\n" +
      "    get:\n" +
      "      responses:\n" +
      "        '200':\n" +
      "          description: ok\n" +
      "  /pets/{petId}/tags/{tagId}:\n" +
      "    get:\n" +
      "      responses:\n" +
      "        '200':\n" +
      "          description: ok\n";

    RecordingResourceCheck check = new RecordingResourceCheck();
    check.scanFileForIssues(createV2Context(yaml));

    assertThat(check.visitedPaths).contains("/pets/{petId}/tags");
  }

  @Test
  public void path_is_last_without_following_variable_child_is_not_resource() {
    String yaml =
      "swagger: \"2.0\"\n" +
      "info:\n" +
      "  version: 1.0.0\n" +
      "  title: Test\n" +
      "paths:\n" +
      "  /pets:\n" +
      "    get:\n" +
      "      responses:\n" +
      "        '200':\n" +
      "          description: ok\n" +
      "  /other:\n" +
      "    get:\n" +
      "      responses:\n" +
      "        '200':\n" +
      "          description: ok\n";

    RecordingResourceCheck check = new RecordingResourceCheck();
    check.scanFileForIssues(createV2Context(yaml));

    assertThat(check.visitedPaths).doesNotContain("/pets", "/other");
  }
}
