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

public class JsonSchemaTest extends BaseNodeTest<OpenApi31Grammar> {

    @Test
    public void json_$schema_schema() {
    JsonNode node = parseResource(OpenApi31Grammar.SCHEMA, "/models/v31/jsonschemaschema.yaml");

    assertEquals("https://json-schema.org/draft/2020-12/schema", node, "/$schema");  
    }

    @Test
    public void json_$schema_link() {
    JsonNode node = parseResource(OpenApi31Grammar.LINK, "/models/v31/jsonschemalink.yaml");

    JsonNode param = node.at("/parameters");
    assertEquals("getExample", param, "/operationId");
    assertEquals("http://json-schema.org/draft-07/schema#", param, "/exampleParam/$schema");
    }

    @Test
    public void json_$schema_example() {
    JsonNode node = parseResource(OpenApi31Grammar.EXAMPLE, "/models/v31/jsonschemaexample.yaml");

    JsonNode param = node.at("/value");
    assertEquals("http://json-schema.org/draft-07/schema#", param, "/$schema");
    }
}
