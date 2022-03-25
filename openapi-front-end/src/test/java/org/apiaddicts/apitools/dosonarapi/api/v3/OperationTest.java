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

import static org.assertj.core.api.Assertions.assertThat;

public class OperationTest extends BaseNodeTest<OpenApi3Grammar> {
  private static void assertHasCorrectSecurity(JsonNode node) {
    assertElements(node, "/security").hasSize(1);
    assertElements(node, "/security/0/petstore_auth").containsExactly("write:pets", "read:pets");
  }

  @Test
  public void can_parse_operation() {
    JsonNode node = parseResource(OpenApi3Grammar.OPERATION, "/models/v3/operation.yaml");

    assertElements(node, "/tags").containsExactly("pet");
    assertEquals("Updates a pet in the store with form data", node, "/summary");
    assertEquals("updatePetWithForm", node, "/operationId");

    assertThat(node.at("/parameters").elements()).hasSize(1);
    assertPropertyKeys(node, "/requestBody/content").containsExactly("application/x-www-form-urlencoded");
    assertPropertyKeys(node, "/responses").containsExactly("200", "405");

    assertHasCorrectSecurity(node);
  }
}
