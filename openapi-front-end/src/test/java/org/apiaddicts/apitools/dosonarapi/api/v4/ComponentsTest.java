package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class ComponentsTest extends BaseNodeTest<AsyncApiGrammar> {
  @Test
  public void can_parse_components_with_all_elements() {
    JsonNode node = parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/components/fullComponents.yaml");

    // Verificar componentes de mensaje
    assertPropertyKeys(node, "/messages").containsExactlyInAnyOrder("UserCreated", "UserDeleted");
    assertEquals("A user created event.", node, "/messages/UserCreated/description");
    assertEquals("A user deleted event.", node, "/messages/UserDeleted/description");

    // Verificar esquemas
    assertPropertyKeys(node, "/schemas").containsExactlyInAnyOrder("User", "Error");
    assertEquals("object", node, "/schemas/User/type");
    assertEquals("object", node, "/schemas/Error/type");

    // Verificar esquemas de seguridad
    assertPropertyKeys(node, "/securitySchemes").containsExactlyInAnyOrder("apiKey", "openIdConnect");
    assertEquals("apiKey", node, "/securitySchemes/apiKey/type");
    assertEquals("openIdConnect", node, "/securitySchemes/openIdConnect/type");

    // Añadir más verificaciones según sea necesario para otros componentes como parameters, messageTraits, etc.
  }

  @Test(expected = ValidationException.class)
  public void throws_when_parsing_incomplete_components() {
    parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/components/incompleteComponents.yaml");
  }
}
