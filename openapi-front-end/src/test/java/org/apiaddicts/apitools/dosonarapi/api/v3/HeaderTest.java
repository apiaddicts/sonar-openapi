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

public class HeaderTest extends BaseNodeTest<OpenApi3Grammar> {
  @Test
  public void can_parse_header() {
    JsonNode node = parseResource(OpenApi3Grammar.HEADER, "/models/v3/header.yaml");

    assertEquals("The number of allowed requests in the current period", node, "/description");
    assertEquals("integer", node, "/schema/type");
    assertFalse(node, "/required");
    assertFalse(node, "/deprecated");
    assertTrue(node, "/allowEmptyValue");
    assertFalse(node, "/allowReserved");
    assertFalse(node, "/explode");
    assertEquals("simple", node, "/style");
    assertEquals("object", node, "/content/application~1json/schema/type");
  }
}
