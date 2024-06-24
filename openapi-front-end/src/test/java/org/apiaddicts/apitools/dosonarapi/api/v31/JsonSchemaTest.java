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
