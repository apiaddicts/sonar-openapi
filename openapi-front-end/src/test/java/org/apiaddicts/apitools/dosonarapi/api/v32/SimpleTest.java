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

public class SimpleTest extends BaseNodeTest<OpenApi32Grammar> {

  @Test
  public void can_parse_paths() {
    JsonNode node = parseResource(OpenApi32Grammar.PATHS, "/models/v32/paths.yaml");

    assertPropertyKeys(node).containsOnly("/pets");
  }

  @Test
  public void path_supports_query_method() {
    JsonNode node = parseResource(OpenApi32Grammar.PATH, "/models/v32/path-item.yaml");

    assertPropertyKeys(node).contains("get", "query");
  }

  @Test
  public void can_parse_pathitems_component() {
    JsonNode node = parseResource(OpenApi32Grammar.PATH_ITEMS_COMPONENT, "/models/v32/pathitems-component.yaml");

    assertPropertyKeys(node).containsOnly("PetItem");
  }

  @Test
  public void can_parse_mediatypes_component() {
    JsonNode node = parseResource(OpenApi32Grammar.MEDIA_TYPES_COMPONENT, "/models/v32/mediatypes-component.yaml");

    assertPropertyKeys(node).containsOnly("JsonPet");
  }

  @Test
  public void server_supports_name_field() {
    JsonNode node = parseResource(OpenApi32Grammar.SERVER, "/models/v32/server-with-name.yaml");

    assertEquals("production", node, "/name");
    assertEquals("https://api.example.com/v1", node, "/url");
  }
}
