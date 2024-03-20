package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class ParameterTest extends BaseNodeTest {
  @Test
  public void can_parse_channel_with_parameters() {
    JsonNode node = parseResource(AsyncApiGrammar.CHANNELS, "/models/v4/parameters/channelWithParameters.yaml");

    // Verificar la existencia de parámetros en un canal específico
    assertPropertyKeys(node, "/channels/user/created/parameters").containsExactlyInAnyOrder("userId");
    assertEquals("string", node, "/channels/user/created/parameters/userId/schema/type");
    assertEquals("Path parameter", node, "/channels/user/created/parameters/userId/description");
    assertEquals(true, node, "/channels/user/created/parameters/userId/required");
  }

  @Test
  public void can_parse_operation_with_parameters() {
    JsonNode node = parseResource(AsyncApiGrammar.OPERATION, "/models/v4/parameters/operationWithParameters.yaml");

    // Verificar la existencia de parámetros en una operación específica
    assertPropertyKeys(node, "/operationBindings/mqtt/parameters").containsExactlyInAnyOrder("qos", "retain");
    assertEquals("integer", node, "/operationBindings/mqtt/parameters/qos/schema/type");
    assertEquals("boolean", node, "/operationBindings/mqtt/parameters/retain/schema/type");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_parsing_incomplete_parameters() {
    parseResource(AsyncApiGrammar.PARAMETERS_COMPONENT, "/models/v4/parameters/incompleteParameters.yaml");
  }
}
