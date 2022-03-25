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

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseTest extends BaseNodeTest<OpenApi2Grammar> {
  @Test
  public void can_parse_response_with_no_return() {
    JsonNode model = parseResource(OpenApi2Grammar.RESPONSE, "/models/v2/response/no-return.yaml");

    assertEquals("object created", model, "/description");
    assertMissing(model.at("/schema"));
  }

  @Test
  public void can_parse_response_with_string_return() {
    JsonNode model = parseResource(OpenApi2Grammar.RESPONSE, "/models/v2/response/string.yaml");

    assertEquals("A simple string response", model, "/description");
    assertEquals("string", model, "/schema/type");
  }

  @Test
  public void can_parse_response_with_array_return() {
    JsonNode model = parseResource(OpenApi2Grammar.RESPONSE, "/models/v2/response/array-of-complex.yaml");

    assertEquals("A complex object array response", model, "/description");
    assertEquals("array", model, "/schema/type");
    assertIsRef("#/definitions/VeryComplexType", model, "/schema/items");
  }

  @Test
  public void can_parse_response_with_headers() {
    JsonNode model = parseResource(OpenApi2Grammar.RESPONSE, "/models/v2/response/with-headers.yaml");

    assertEquals("A simple string response", model, "/description");
    assertPropertyKeys(model, "/headers").containsExactlyInAnyOrder(
        "X-Rate-Limit-Limit", "X-Rate-Limit-Remaining", "X-Rate-Limit-Reset");
    assertThat(model.at("/headers").properties())
      .extracting(n -> n.at("/type").value().getTokenValue())
      .contains("integer", "integer", "integer");
  }
}
