package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class OperationTraitTest extends BaseNodeTest {
  @Test
  public void can_parse_operation_with_traits() {
    JsonNode node = parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/operationTraits/operationWithTraits.yaml");

    // Verificar la aplicación del trait a la operación
    assertPropertyKeys(node, "/components/channels/userCreated/subscribe/traits").containsExactly("documentationTrait");
    assertEquals("Operation documentation", node, "/components/channels/userCreated/subscribe/traits/documentationTrait/summary");
    assertEquals("This is a detailed documentation of the subscribe operation.", node, "/components/channels/userCreated/subscribe/traits/documentationTrait/description");

    // Verificar detalles específicos del trait como seguridad
    assertPropertyKeys(node, "/components/channels/userCreated/subscribe/traits/documentationTrait/security").containsExactlyInAnyOrder("apiKey");
  }

  @Test
  public void can_parse_reusable_operation_trait() {
    JsonNode node = parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/operationTraits/reusableOperationTrait.yaml");

    // Verificar la definición del trait reusable
    assertEquals("Reusable operation trait", node, "/components/operationTraits/documentationTrait/summary");
    assertEquals("This trait provides common documentation for operations.", node, "/components/operationTraits/documentationTrait/description");
    assertPropertyKeys(node, "/components/operationTraits/documentationTrait/security").containsExactlyInAnyOrder("apiKey");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_operation_trait_is_incomplete() {
    parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/operationTraits/incompleteOperationTrait.yaml");
  }
}
