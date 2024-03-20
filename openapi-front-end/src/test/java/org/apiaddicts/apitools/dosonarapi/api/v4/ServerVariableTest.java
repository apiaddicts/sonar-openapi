package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class ServerVariableTest extends BaseNodeTest {
  @Test
  public void can_parse_server_with_variables() {
    JsonNode node = parseResource(AsyncApiGrammar.SERVER, "/models/v4/serverVariables/serverWithVariables.yaml");

    // Verificar la existencia de variables en la configuración del servidor
    assertPropertyKeys(node, "/servers/productionServer/variables").containsExactlyInAnyOrder("username", "port", "basePath");
    
    // Verificar detalles de las variables
    assertEquals("demo", node, "/servers/productionServer/variables/username/default");
    assertEquals("User provided by the service", node, "/servers/productionServer/variables/username/description");
    
    assertElements(node, "/servers/productionServer/variables/port/enum").containsExactly("1883", "8883");
    assertEquals("1883", node, "/servers/productionServer/variables/port/default");
    
    assertEquals("v2", node, "/servers/productionServer/variables/basePath/default");
    assertEquals("Base path for the API", node, "/servers/productionServer/variables/basePath/description");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_server_variables_are_incomplete() {
    parseResource(AsyncApiGrammar.SERVER, "/models/v4/serverVariables/incompleteServerVariables.yaml");
  }
}
