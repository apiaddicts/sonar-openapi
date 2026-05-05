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
package org.apiaddicts.apitools.dosonarapi.api.v32;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class JsonSchemaTest extends BaseNodeTest<OpenApi32Grammar> {

  @Test
  public void can_parse_if_then_else() {
    JsonNode node = parseResource(OpenApi32Grammar.SCHEMA, "/models/v32/jsonschema.yaml");

    assertPropertyKeys(node).contains("if", "then", "else");
  }

  @Test
  public void can_parse_prefix_items_and_contains() {
    JsonNode node = parseResource(OpenApi32Grammar.SCHEMA, "/models/v32/jsonschema.yaml");

    assertPropertyKeys(node).contains("prefixItems", "contains", "minContains", "maxContains");
  }

  @Test
  public void can_parse_property_names_and_pattern_properties() {
    JsonNode node = parseResource(OpenApi32Grammar.SCHEMA, "/models/v32/jsonschema.yaml");

    assertPropertyKeys(node).contains("propertyNames", "patternProperties");
  }

  @Test
  public void can_parse_dependent_required() {
    JsonNode node = parseResource(OpenApi32Grammar.SCHEMA, "/models/v32/jsonschema.yaml");

    assertPropertyKeys(node).contains("dependentRequired");
  }

  @Test
  public void can_parse_defs_and_anchor() {
    JsonNode node = parseResource(OpenApi32Grammar.SCHEMA, "/models/v32/jsonschema.yaml");

    assertPropertyKeys(node).contains("$defs", "$anchor", "$comment");
  }

  @Test
  public void can_parse_unevaluated_keywords() {
    JsonNode node = parseResource(OpenApi32Grammar.SCHEMA, "/models/v32/jsonschema.yaml");

    assertPropertyKeys(node).contains("unevaluatedItems", "unevaluatedProperties");
  }
}
