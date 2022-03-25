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

import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class HeaderTest extends BaseNodeTest<OpenApi2Grammar> {
  @Test
  public void can_parse_simple_header() {
    JsonNode model = parseResource(OpenApi2Grammar.HEADER, "/models/v2/header/simple.yaml");

    assertEquals("The number of allowed requests in the current period", model, "/description");
    assertEquals("integer", model, "/type");
  }

  @Test
  public void can_parse_header_parameter() {
    JsonNode model = parseResource(OpenApi2Grammar.HEADER, "/models/v2/header/token.yaml");

    assertEquals("token to be passed as a header", model, "/description");
    assertEquals("array", model, "/type");
    assertEquals("integer", model, "/items/type");
    assertEquals("csv", model, "/collectionFormat");
  }
}
