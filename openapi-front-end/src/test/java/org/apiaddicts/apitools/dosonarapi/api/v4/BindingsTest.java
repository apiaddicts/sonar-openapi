package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class BindingsTest extends BaseNodeTest {
  @Test
  public void can_parse_server_bindings() {
    JsonNode node = parseResource(AsyncApiGrammar.SERVER_BINDINGS, "/models/v4/bindings/serverBindings.yaml");

    assertEquals("http", node, "/server/bindings/type");
    assertEquals("1.1", node, "/server/bindings/version");
  }

  @Test
  public void can_parse_channel_bindings() {
    JsonNode node = parseResource(AsyncApiGrammar.CHANNEL_BINDINGS, "/models/v4/bindings/channelBindings.yaml");

    assertEquals("websocket", node, "/channels/myChannel/bindings/type");
  }

  @Test
  public void can_parse_message_bindings() {
    JsonNode node = parseResource(AsyncApiGrammar.MESSAGE_BINDINGS, "/models/v4/bindings/messageBindings.yaml");

    assertEquals("application/json", node, "/messages/myMessage/bindings/contentType");
  }

  @Test
  public void can_parse_operation_bindings() {
    JsonNode node = parseResource(AsyncApiGrammar.OPERATION_BINDINGS, "/models/v4/bindings/operationBindings.yaml");

    assertEquals("request", node, "/channels/myChannel/subscribe/bindings/type");
    assertEquals("5s", node, "/channels/myChannel/subscribe/bindings/timeout");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_bindings_are_incomplete() {
    parseResource(AsyncApiGrammar.SERVER_BINDINGS, "/models/v4/bindings/incompleteServerBindings.yaml");
  }
}
