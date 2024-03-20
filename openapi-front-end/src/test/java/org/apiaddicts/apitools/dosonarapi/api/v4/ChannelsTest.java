package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class ChannelsTest extends BaseNodeTest<AsyncApiGrammar> {
  @Test
  public void can_parse_channel_with_operations() {
    JsonNode node = parseResource(AsyncApiGrammar.CHANNELS, "/models/v4/channels/simpleChannel.yaml");

    assertPropertyKeys(node, "/").containsExactlyInAnyOrder("chat/messages");
    assertEquals("The channel for chat messages", node, "/chat/messages/description");
    assertPropertyKeys(node.at("/chat/messages"), "/").containsExactlyInAnyOrder("description", "subscribe", "publish");
    
    // Verificar operación de suscripción
    assertEquals("Receive chat messages", node, "/chat/messages/subscribe/summary");
    assertEquals("application/json", node, "/chat/messages/subscribe/message/contentType");
    assertEquals("object", node, "/chat/messages/subscribe/message/payload/type");
    assertPropertyKeys(node, "/chat/messages/subscribe/message/payload/properties").containsExactlyInAnyOrder("user", "message");

    // Verificar operación de publicación
    assertEquals("Send chat messages", node, "/chat/messages/publish/summary");
    assertEquals("application/json", node, "/chat/messages/publish/message/contentType");
    assertEquals("object", node, "/chat/messages/publish/message/payload/type");
    assertPropertyKeys(node, "/chat/messages/publish/message/payload/properties").containsExactlyInAnyOrder("user", "message");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_parsing_incomplete_channel() {
    parseResource(AsyncApiGrammar.CHANNELS, "/models/v4/channels/incompleteChannel.yaml");
  }
}
