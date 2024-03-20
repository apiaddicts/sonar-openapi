package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class MessageTraitTest extends BaseNodeTest {
  @Test
  public void can_parse_message_with_traits() {
    JsonNode node = parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/messageTraits/messageWithTraits.yaml");

    // Verificar la aplicación del trait al mensaje
    assertPropertyKeys(node, "/components/messages/UserCreated/traits").containsExactly("generalInfo");
    assertEquals("User creation event", node, "/components/messages/UserCreated/traits/generalInfo/summary");
    assertEquals("This is a detailed description of the user creation event message.", node, "/components/messages/UserCreated/traits/generalInfo/description");
    
    // Verificar propiedades del trait
    assertEquals("application/json", node, "/components/messages/UserCreated/traits/generalInfo/contentType");
    assertPropertyKeys(node, "/components/messages/UserCreated/traits/generalInfo/headers").containsExactlyInAnyOrder("correlationId");
    assertEquals("string", node, "/components/messages/UserCreated/traits/generalInfo/headers/correlationId/type");
  }

  @Test
  public void can_parse_reusable_message_trait() {
    JsonNode node = parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/messageTraits/reusableMessageTrait.yaml");

    // Verificar la definición del trait reusable
    assertEquals("General information about the event", node, "/components/messageTraits/generalInfo/description");
    assertEquals("application/json", node, "/components/messageTraits/generalInfo/contentType");
    assertPropertyKeys(node, "/components/messageTraits/generalInfo/headers").containsExactlyInAnyOrder("correlationId");
    assertEquals("string", node, "/components/messageTraits/generalInfo/headers/correlationId/type");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_message_trait_is_incomplete() {
    parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/messageTraits/incompleteMessageTrait.yaml");
  }
}
