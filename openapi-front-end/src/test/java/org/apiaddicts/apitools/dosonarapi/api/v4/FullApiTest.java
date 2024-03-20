package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class FullApiTest extends BaseNodeTest {
  @Test
  public void can_validate_full_api_specification() {
    JsonNode node = parseResource(AsyncApiGrammar.ROOT, "/models/v4/fullApi/fullApiSpecification.yaml");

    // Validar información básica de la API
    assertEquals("Chat API", node, "/info/title");
    assertEquals("1.0.0", node, "/info/version");
    
    // Validar servidores
    assertPropertyKeys(node, "/servers").containsExactlyInAnyOrder("production", "development");
    assertEquals("mqtt://prod.example.com", node, "/servers/production/url");
    assertEquals("mqtt://dev.example.com", node, "/servers/development/url");

    // Validar canales y sus operaciones
    assertPropertyKeys(node, "/channels").containsExactlyInAnyOrder("user/created", "user/deleted");
    assertEquals("Publish user created events", node, "/channels/user/created/publish/summary");
    assertEquals("Subscribe to user deleted events", node, "/channels/user/deleted/subscribe/summary");

    // Validar componentes reutilizables, como mensajes y esquemas
    assertPropertyKeys(node, "/components/messages").containsExactlyInAnyOrder("UserCreated", "UserDeleted");
    assertEquals("object", node, "/components/schemas/User/type");

    // Validar seguridad
    assertPropertyKeys(node, "/components/securitySchemes").containsExactlyInAnyOrder("apiKey");
    assertEquals("apiKey", node, "/components/securitySchemes/apiKey/type");

    // Esta prueba puede ampliarse para incluir más verificaciones detalladas de cada sección
  }
}
