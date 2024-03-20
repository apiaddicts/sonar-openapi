package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class OperationTest extends BaseNodeTest<AsyncApiGrammar> {
  @Test
  public void can_parse_subscribe_operation() {
    JsonNode node = parseResource(AsyncApiGrammar.CHANNELS, "/models/v4/operations/subscribeOperation.yaml");

    assertEquals("Subscribe to user creation events", node, "/channels/user/created/subscribe/summary");
    assertEquals("This operation allows you to subscribe to user creation events.", node, "/channels/user/created/subscribe/description");
    assertEquals("receiveUserCreatedEvent", node, "/channels/user/created/subscribe/operationId");
    assertEquals("UserCreated", node, "/channels/user/created/subscribe/message/$ref");
  }

  @Test
  public void can_parse_publish_operation() {
    JsonNode node = parseResource(AsyncApiGrammar.CHANNELS, "/models/v4/operations/publishOperation.yaml");

    assertEquals("Publish user creation events", node, "/channels/user/created/publish/summary");
    assertEquals("This operation allows you to publish user creation events.", node, "/channels/user/created/publish/description");
    assertEquals("publishUserCreatedEvent", node, "/channels/user/created/publish/operationId");
    assertEquals("UserCreated", node, "/channels/user/created/publish/message/$ref");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_parsing_incomplete_operation() {
    parseResource(AsyncApiGrammar.CHANNELS, "/models/v4/operations/incompleteOperation.yaml");
  }
}
