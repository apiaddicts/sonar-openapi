package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class SchemaTest extends BaseNodeTest {
  @Test
  public void can_parse_simple_schema() {
    JsonNode node = parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/schemas/simpleSchema.yaml");

    assertEquals("object", node, "/components/schemas/User/type");
    assertPropertyKeys(node, "/components/schemas/User/properties").containsExactlyInAnyOrder("id", "name");
    assertEquals("string", node, "/components/schemas/User/properties/id/type");
    assertEquals("string", node, "/components/schemas/User/properties/name/type");
  }

  @Test
  public void can_parse_schema_with_reference() {
    JsonNode node = parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/schemas/schemaWithReference.yaml");

    assertEquals("object", node, "/components/schemas/Order/type");
    assertPropertyKeys(node, "/components/schemas/Order/properties").containsExactlyInAnyOrder("orderId", "product");
    assertEquals("string", node, "/components/schemas/Order/properties/orderId/type");
    assertEquals("#/components/schemas/Product", node, "/components/schemas/Order/properties/product/$ref");
  }

  @Test
  public void can_parse_schema_with_advanced_types() {
    JsonNode node = parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/schemas/advancedSchema.yaml");

    assertEquals("array", node, "/components/schemas/UserList/type");
    assertEquals("#/components/schemas/User", node, "/components/schemas/UserList/items/$ref");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_parsing_incomplete_schema() {
    parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/schemas/incompleteSchema.yaml");
  }
}
