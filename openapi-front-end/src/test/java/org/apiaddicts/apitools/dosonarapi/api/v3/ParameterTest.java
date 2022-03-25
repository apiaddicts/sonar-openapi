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
package org.apiaddicts.apitools.dosonarapi.api.v3;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class ParameterTest extends BaseNodeTest<OpenApi3Grammar> {
  @Test
  public void can_parse_header() {
    JsonNode node = parseResource(OpenApi3Grammar.PARAMETER, "/models/v3/parameter/header.yaml");

    assertEquals("token", node, "/name");
    assertEquals("header", node, "/in");
    assertEquals("token to be passed as a header", node, "/description");
    assertTrue(node, "/required");
    assertEquals("simple", node, "/style");

    assertEquals("array", node, "/schema/type");
    assertEquals("integer", node, "/schema/items/type");
    assertEquals("int64", node, "/schema/items/format");
  }

  @Test
  public void can_parse_path() {
    JsonNode node = parseResource(OpenApi3Grammar.PARAMETER, "/models/v3/parameter/path.yaml");

    assertEquals("username", node, "/name");
    assertEquals("path", node, "/in");
    assertEquals("username to fetch", node, "/description");
    assertTrue(node, "/required");

    assertEquals("string", node, "/schema/type");
  }

  @Test
  public void can_parse_query() {
    JsonNode node = parseResource(OpenApi3Grammar.PARAMETER, "/models/v3/parameter/query.yaml");

    assertEquals("id", node, "/name");
    assertEquals("query", node, "/in");
    assertEquals("ID of the object to fetch", node, "/description");
    assertFalse(node, "/required");
    assertEquals("form", node, "/style");
    assertTrue(node, "/explode");

    assertEquals("array", node, "/schema/type");
    assertEquals("string", node, "/schema/items/type");
  }

  @Test
  public void can_parse_query_freeForm() {
    JsonNode node = parseResource(OpenApi3Grammar.PARAMETER, "/models/v3/parameter/query-free-form.yaml");

    assertEquals("freeForm", node, "/name");
    assertEquals("query", node, "/in");
    assertEquals("form", node, "/style");

    assertEquals("object", node, "/schema/type");
    assertEquals("integer", node, "/schema/additionalProperties/type");
  }
}
