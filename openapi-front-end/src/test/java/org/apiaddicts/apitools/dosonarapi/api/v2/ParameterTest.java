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
package org.apiaddicts.apitools.dosonarapi.api.v2;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class ParameterTest extends BaseNodeTest<OpenApi2Grammar> {
  @Test
  public void can_parse_body_parameter_with_reference() {
    JsonNode model = parseResource(OpenApi2Grammar.PARAMETER, "/models/v2/parameter/body-with-reference.yaml");

    assertEquals("user", model, "/name");
    assertEquals("body", model, "/in");
    assertEquals("user to add to the system", model, "/description");
    assertTrue(model, "/required");
    assertIsRef("#/definitions/User", model, "/schema");
  }

  @Test
  public void can_parse_body_as_array() {
    JsonNode model = parseResource(OpenApi2Grammar.PARAMETER, "/models/v2/parameter/body-as-array.yaml");

    assertEquals("user", model, "/name");
    assertEquals("body", model, "/in");
    assertEquals("user to add to the system", model, "/description");
    assertTrue(model, "/required");
    assertEquals("string", model, "/schema/items/type");
  }

  @Test
  public void can_parse_header_parameter() {
    JsonNode model = parseResource(OpenApi2Grammar.PARAMETER, "/models/v2/parameter/header.yaml");

    assertEquals("token", model, "/name");
    assertEquals("header", model, "/in");
    assertEquals("token to be passed as a header", model, "/description");
    assertTrue(model, "/required");
    assertEquals("array", model, "/type");
    assertEquals("integer", model, "/items/type");
    assertEquals("csv", model, "/collectionFormat");
  }

  @Test
  public void can_parse_path_parameter() {
    JsonNode model = parseResource(OpenApi2Grammar.PARAMETER, "/models/v2/parameter/path.yaml");

    assertEquals("username", model, "/name");
    assertEquals("path", model, "/in");
    assertEquals("username to fetch", model, "/description");
    assertTrue(model, "/required");
    assertEquals("string", model, "/type");
  }

  @Test
  public void can_parse_query_parameter() {
    JsonNode model = parseResource(OpenApi2Grammar.PARAMETER, "/models/v2/parameter/query.yaml");

    assertEquals("id", model, "/name");
    assertEquals("query", model, "/in");
    assertEquals("ID of the object to fetch", model, "/description");
    assertFalse(model, "/required");
    assertEquals("array", model, "/type");
    assertEquals("string", model, "/items/type");
    assertEquals("multi", model, "/collectionFormat");
  }

}
