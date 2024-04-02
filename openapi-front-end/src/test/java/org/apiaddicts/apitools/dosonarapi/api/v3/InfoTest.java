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
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class InfoTest extends BaseNodeTest<OpenApi3Grammar> {
  @Test
  public void can_parse_info_with_missing_optionals() {
    JsonNode node = parseResource(OpenApi3Grammar.INFO, "/models/shared/info/minimal.yaml");

    assertMissing(node.at("/description"));
    assertMissing(node.at("/termsOfService"));
    assertMissing(node.at("/contact"));
    assertMissing(node.at("/license"));

    assertEquals("simple model", node, "/title");

    assertEquals("1.0.0", node, "/version");
  }

  @Test
  public void can_parse_info_with_flow() {
    JsonNode node = parseResource(OpenApi3Grammar.INFO, "/models/shared/info/withFlow.yaml");

    assertEquals("\nThis is a multiline description", node, "/description");
  }

  @Test
  public void can_parse_full_info() {
    JsonNode node = parseResource(OpenApi3Grammar.INFO, "/models/shared/info/full.yaml");

    assertEquals("http://example.com/terms/", node, "/termsOfService");
    assertEquals("API Support", node, "/contact/name");
    assertEquals("http://www.example.com/support", node, "/contact/url");
    assertEquals("support@example.com", node, "/contact/email");
    assertEquals("Apache 2.0", node, "/license/name");
    assertEquals("https://www.apache.org/licenses/LICENSE-2.0.html", node, "/license/url");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_parsing_incomplete_object() {
    parseResource(OpenApi3Grammar.INFO, "/models/shared/info/incomplete.yaml");
  }
}
