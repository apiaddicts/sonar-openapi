package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class ExamplesTest extends BaseNodeTest {
  @Test
  public void can_parse_message_with_examples() {
    JsonNode node = parseResource(AsyncApiGrammar.MESSAGE, "/models/v4/examples/messageWithExamples.yaml");

    // Verificar la existencia de ejemplos en un mensaje
    assertPropertyKeys(node, "/components/messages/UserCreated/examples").containsExactlyInAnyOrder("example1", "example2");
    assertEquals("John Doe", node, "/components/messages/UserCreated/examples/example1/payload/username");
    assertEquals("Jane Doe", node, "/components/messages/UserCreated/examples/example2/payload/username");
  }

  @Test
  public void can_parse_operation_with_examples() {
    JsonNode node = parseResource(AsyncApiGrammar.OPERATION, "/models/v4/examples/operationWithExamples.yaml");

    // Verificar ejemplos en operaciones específicas
    assertPropertyKeys(node, "/channels/userCreated/publish/examples").containsExactly("examplePublish");
    assertEquals("New user published event", node, "/channels/userCreated/publish/examples/examplePublish/summary");
  }

  @Test
  public void can_parse_component_with_examples() {
    JsonNode node = parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/examples/componentWithExamples.yaml");

    // Verificar ejemplos en componentes reutilizables
    assertPropertyKeys(node, "/components/schemas/User/examples").containsExactly("exampleUser");
    assertEquals("JohnDoe123", node, "/components/schemas/User/examples/exampleUser/properties/userId");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_example_is_incomplete() {
    parseResource(AsyncApiGrammar.EXAMPLE, "/models/v4/examples/incompleteExample.yaml");
  }
}
