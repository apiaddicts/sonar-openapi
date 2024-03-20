package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class CorrelationIdTest extends BaseNodeTest {
  @Test
  public void can_parse_correlationId_in_message() {
    JsonNode node = parseResource(AsyncApiGrammar.MESSAGE, "/models/v4/correlationId/correlationIdInMessage.yaml");

    // Verificar la existencia y configuración de correlationId en un mensaje
    assertEquals("{$request.header#/correlationId}", node, "/components/messages/UserCreated/correlationId/location");
    assertEquals("Correlation ID passed via message headers", node, "/components/messages/UserCreated/correlationId/description");
  }

  @Test
  public void can_parse_reusable_correlationId() {
    JsonNode node = parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/correlationId/reusableCorrelationId.yaml");

    // Verificar la definición reutilizable de correlationId
    assertEquals("{$request.header#/correlationId}", node, "/components/correlationIds/genericCorrelationId/location");
    assertEquals("A generic correlation ID applicable to any message", node, "/components/correlationIds/genericCorrelationId/description");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_correlationId_is_incomplete() {
    parseResource(AsyncApiGrammar.MESSAGE, "/models/v4/correlationId/incompleteCorrelationId.yaml");
  }
}
