package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class ExternalDocsTest extends BaseNodeTest<AsyncApiGrammar> {
  @Test
  public void can_parse_externalDocs_at_root_level() {
    JsonNode node = parseResource(AsyncApiGrammar.ASYNCAPI, "/models/v4/externalDocs/externalDocsAtRoot.yaml");

    assertEquals("Find more info here", node, "/externalDocs/description");
    assertEquals("https://example.com", node, "/externalDocs/url");
  }

  @Test
  public void can_parse_externalDocs_in_channel_item() {
    JsonNode node = parseResource(AsyncApiGrammar.CHANNELS, "/models/v4/externalDocs/externalDocsInChannel.yaml");

    // Verificar documentación externa en un canal específico
    assertEquals("Detailed documentation here", node, "/channels/user/created/externalDocs/description");
    assertEquals("https://example.com/user-events", node, "/channels/user/created/externalDocs/url");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_externalDocs_is_incomplete() {
    parseResource(AsyncApiGrammar.ASYNCAPI, "/models/v4/externalDocs/incompleteExternalDocs.yaml");
  }
}
