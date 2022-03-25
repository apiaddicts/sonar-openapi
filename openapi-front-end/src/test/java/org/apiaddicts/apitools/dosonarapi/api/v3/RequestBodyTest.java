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

public class RequestBodyTest extends BaseNodeTest<OpenApi3Grammar> {
  @Test
  public void can_parse_body_with_examples() {
    JsonNode model = parseResource(OpenApi3Grammar.REQUEST_BODY, "/models/v3/body/body-with-example.yaml");

    assertEquals("user to add to the system", model, "/description");
    assertPropertyKeys(model, "/content").containsExactlyInAnyOrder("application/json", "application/xml", "text/plain", "*/*");
    assertEquals("http://foo.bar/examples/user-example.txt", model, "/content/text~1plain/examples/user/externalValue");
  }

  @Test
  public void can_parse_body_as_array() {
    JsonNode model = parseResource(OpenApi3Grammar.REQUEST_BODY, "/models/v3/body/body-as-array.yaml");

    assertEquals("user to add to the system", model, "/description");
    assertTrue(model, "/required");
    assertEquals("string", model, "/content/text~1plain/schema/items/type");
  }

  @Test
  public void can_parse_body_with_urlencoded() {
    JsonNode model = parseResource(OpenApi3Grammar.REQUEST_BODY, "/models/v3/body/body-urlencoded.yaml");

    assertPropertyKeys(model, "/content").containsExactly("application/x-www-form-urlencoded");
  }
}
