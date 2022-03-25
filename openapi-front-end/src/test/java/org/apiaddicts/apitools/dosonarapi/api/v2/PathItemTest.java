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

public class PathItemTest extends BaseNodeTest<OpenApi2Grammar> {
  private static void assertHasCorrectGetOperation(JsonNode node) {
    JsonNode op = node.at("/get");

    assertEquals("Returns pets based on ID", op, "/description");
    assertEquals("Find pets by ID", op, "/summary");
    assertEquals("getPetsById", op, "/operationId");
    assertElements(op, "/produces").containsExactly("application/json", "text/html");

    JsonNode _200 = op.at("/responses/200");
    assertEquals("pet response", _200, "/description");
    assertEquals("array", _200, "/schema/type");
    assertIsRef("#/definitions/Pet", _200, "/schema/items");

    JsonNode def = op.at("/responses/default");
    assertEquals("error payload", def, "/description");
    assertIsRef("#/definitions/ErrorModel", def, "/schema");
  }

  private static void assertHasCorrectParameter(JsonNode node) {
    assertElements(node, "/parameters").hasSize(1);
    JsonNode parameter = node.at("/parameters/0");
    assertEquals("id", parameter, "/name");
    assertEquals("path", parameter, "/in");
    assertEquals("ID of pet to use", parameter, "/description");
    assertTrue(parameter, "/required");
    assertEquals("array", parameter, "/type");
    assertEquals("string", parameter, "/items/type");
    assertEquals("csv", parameter, "/collectionFormat");
  }

  @Test
  public void can_parse_path_item() {
    JsonNode node = parseResource(OpenApi2Grammar.PATH, "/models/v2/path.yaml");

    assertHasCorrectGetOperation(node);
    assertHasCorrectParameter(node);
  }

}
