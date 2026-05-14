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
package org.apiaddicts.apitools.dosonarapi.openapi.parser;

import java.nio.charset.StandardCharsets;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.openapi.OpenApiConfiguration;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.YamlParser;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenApiParserTest {

  private static final OpenApiConfiguration CONFIG = new OpenApiConfiguration(StandardCharsets.UTF_8, false);

  private static final String MINIMAL_V2 =
    "swagger: \"2.0\"\n" +
    "info:\n" +
    "  title: Test\n" +
    "  version: 1.0.0\n" +
    "paths: {}";

  private static final String MINIMAL_V3 =
    "openapi: \"3.0.0\"\n" +
    "info:\n" +
    "  title: Test\n" +
    "  version: 1.0.0\n" +
    "paths: {}";

  @Test
  public void create_v2_parser_parses_swagger_doc() {
    YamlParser parser = OpenApiParser.createV2(CONFIG);
    assertThat(parser).isNotNull();
    JsonNode root = parser.parse(MINIMAL_V2);
    assertThat(root).isNotNull();
    assertThat(root.at("/swagger").getTokenValue()).isEqualTo("2.0");
  }

  @Test
  public void create_v3_parser_parses_openapi_doc() {
    YamlParser parser = OpenApiParser.createV3(CONFIG);
    assertThat(parser).isNotNull();
    JsonNode root = parser.parse(MINIMAL_V3);
    assertThat(root).isNotNull();
    assertThat(root.at("/openapi").getTokenValue()).isEqualTo("3.0.0");
  }

  @Test
  public void create_v31_parser_returns_non_null() {
    YamlParser parser = OpenApiParser.createV31(CONFIG);
    assertThat(parser).isNotNull();
    JsonNode root = parser.parse(MINIMAL_V3);
    assertThat(root).isNotNull();
  }

  @Test
  public void create_v32_parser_returns_non_null() {
    YamlParser parser = OpenApiParser.createV32(CONFIG);
    assertThat(parser).isNotNull();
    JsonNode root = parser.parse(MINIMAL_V3);
    assertThat(root).isNotNull();
  }

  @Test
  public void create_generic_parser_returns_non_null() {
    YamlParser parser = OpenApiParser.createGeneric(CONFIG);
    assertThat(parser).isNotNull();
    JsonNode root = parser.parse("key: value");
    assertThat(root).isNotNull();
  }

  @Test
  public void create_generic_with_non_strict_config() {
    OpenApiConfiguration nonStrict = new OpenApiConfiguration(StandardCharsets.UTF_8, false);
    YamlParser parser = OpenApiParser.createGeneric(nonStrict);
    assertThat(parser).isNotNull();
  }
}
