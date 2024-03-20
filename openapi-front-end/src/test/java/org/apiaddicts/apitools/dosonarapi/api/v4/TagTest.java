package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class TagTest extends BaseNodeTest<AsyncApiGrammar> {
  @Test
  public void can_parse_tags_at_root_level() {
    JsonNode node = parseResource(AsyncApiGrammar., "/models/v4/tags/tagsAtRoot.yaml");

    assertPropertyKeys(node, "/tags").containsExactlyInAnyOrder("userEvents", "orderEvents");
    assertEquals("User related events", node, "/tags/userEvents/description");
    assertEquals("Order related events", node, "/tags/orderEvents/description");
  }

  @Test
  public void can_parse_tags_used_in_channels() {
    JsonNode node = parseResource(AsyncApiGrammar.CHANNELS, "/models/v4/tags/tagsUsedInChannels.yaml");

    // Verificar el uso de tags en un canal específico
    assertPropertyKeys(node, "/channels/user/created").contains("tags");
    assertElements(node, "/channels/user/created/tags").containsExactly("userEvents");

    assertPropertyKeys(node, "/channels/order/placed").contains("tags");
    assertElements(node, "/channels/order/placed/tags").containsExactly("orderEvents");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_tags_are_undefined_in_channels() {
    parseResource(AsyncApiGrammar.CHANNELS, "/models/v4/tags/undefinedTagsInChannels.yaml");
  }
}
