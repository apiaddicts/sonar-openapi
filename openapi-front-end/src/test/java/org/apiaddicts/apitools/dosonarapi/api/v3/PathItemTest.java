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

public class PathItemTest extends BaseNodeTest<OpenApi3Grammar> {
  private static void assertHasCorrectGetOperation(JsonNode node) {
    JsonNode op = node.at("/get");

    assertEquals("Returns pets based on ID", op, "/description");
    assertEquals("Find pets by ID", op, "/summary");
    assertEquals("getPetsById", op, "/operationId");

    JsonNode _200 = op.at("/responses/200");
    assertPropertyKeys(_200, "/content").containsExactly("*/*");
    assertEquals("pet response", _200, "/description");
    assertEquals("array", _200, "/content/*~1*/schema/type");
    assertIsRef("#/components/schemas/Pet", _200, "/content/*~1*/schema/items");

    JsonNode def = op.at("/responses/default");
    assertEquals("error payload", def, "/description");
    assertIsRef("#/components/schemas/ErrorModel", def, "/content/text~1html/schema");
  }

  private static void assertHasCorrectParameter(JsonNode node) {
    assertElements(node, "/parameters").hasSize(1);
    JsonNode parameter = node.at("/parameters/0");
    assertEquals("id", parameter, "/name");
    assertEquals("path", parameter, "/in");
    assertEquals("ID of pet to use", parameter, "/description");
    assertTrue(parameter, "/required");
    assertEquals("array", parameter, "/schema/type");
    assertEquals("string", parameter, "/schema/items/type");
    assertEquals("simple", parameter, "/style");
  }

  @Test
  public void can_parse_path_item() {
    JsonNode node = parseResource(OpenApi3Grammar.PATH, "/models/v3/path.yaml");

    assertHasCorrectGetOperation(node);
    assertHasCorrectParameter(node);
  }

}
