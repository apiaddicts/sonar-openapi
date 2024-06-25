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
package org.apiaddicts.apitools.dosonarapi.api.v31;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class ExclusiveMinimumMaximumTest extends BaseNodeTest<OpenApi31Grammar> {

  @Test
  public void can_parse_schema_with_exclusive_minimum_maximum() {
    JsonNode node = parseResource(OpenApi31Grammar.SCHEMA, "/models/v31/minimummaximum.yaml");

    JsonNode properties = node.at("/properties");
    assertPropertyKeys(properties).containsExactlyInAnyOrder("age", "price");

    JsonNode ageProperty = properties.at("/age");
    assertEquals("integer", ageProperty, "/type");
    assertEquals("7", ageProperty, "/exclusiveMinimum");

    JsonNode priceProperty = properties.at("/price");
    assertEquals("number", priceProperty, "/type");
    assertEquals("100", priceProperty, "/exclusiveMaximum");
  }
}
