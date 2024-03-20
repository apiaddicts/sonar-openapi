package org.apiaddicts.apitools.dosonarapi.api.v4;

import javax.xml.bind.ValidationException;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class MessageTest extends BaseNodeTest<AsyncApiGrammar> {
  @Test
  public void can_parse_simple_message() {
    JsonNode node = parseResource(AsyncApiGrammar.MESSAGES, "/models/v4/messages/simpleMessage.yaml");

    assertEquals("UserCreatedEvent", node, "/name");
    assertEquals("A message representing a user creation event.", node, "/description");
    assertEquals("application/json", node, "/contentType");
    assertEquals("object", node, "/payload/type");
    assertPropertyKeys(node, "/payload/properties").containsExactlyInAnyOrder("userId", "username");
    assertEquals("string", node, "/payload/properties/userId/type");
    assertEquals("string", node, "/payload/properties/username/type");
  }

  @Test
  public void can_parse_message_with_examples() {
    JsonNode node = parseResource(AsyncApiGrammar.MESSAGES, "/models/v4/messages/messageWithExamples.yaml");

    assertPropertyKeys(node, "/examples").containsExactly("example1");
    assertEquals("{\"userId\":\"1234\",\"username\":\"john.doe\"}", node, "/examples/example1");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_parsing_incomplete_message() {
    parseResource(AsyncApiGrammar.MESSAGES, "/models/v4/messages/incompleteMessage.yaml");
  }
}
