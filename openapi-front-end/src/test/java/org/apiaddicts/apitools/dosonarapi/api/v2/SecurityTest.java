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

public class SecurityTest extends BaseNodeTest<OpenApi2Grammar> {
  @Test
  public void can_parse_basic() {
    JsonNode model = parseResource(OpenApi2Grammar.SECURITY_SCHEME, "/models/v2/security/basic.yaml");

    assertEquals("basic", model, "/type");
  }

  @Test
  public void can_parse_api_key() {
    JsonNode model = parseResource(OpenApi2Grammar.SECURITY_SCHEME, "/models/v2/security/api-key.yaml");

    assertEquals("apiKey", model, "/type");
    assertEquals("api_key", model, "/name");
    assertEquals("header", model, "/in");
  }

  @Test
  public void can_parse_oauth2() {
    JsonNode model = parseResource(OpenApi2Grammar.SECURITY_SCHEME, "/models/v2/security/oauth2.yaml");

    assertEquals("oauth2", model, "/type");
    assertEquals("http://swagger.io/api/oauth/dialog", model, "/authorizationUrl");
    assertEquals("implicit", model, "/flow");
    assertPropertyKeys(model, "/scopes").containsExactly("write:pets", "read:pets");
    assertProperties(model, "/scopes").containsExactly("modify pets in your account", "read your pets");
  }
}
