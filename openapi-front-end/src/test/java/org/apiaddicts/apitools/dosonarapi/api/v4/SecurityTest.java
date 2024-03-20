package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class SecurityTest extends BaseNodeTest {
  @Test
  public void can_parse_security_scheme_definitions() {
    JsonNode node = parseResource(AsyncApiGrammar.SECURITY_SCHEMES_COMPONENT, "/models/v4/security/securitySchemeDefinitions.yaml");

    // Verificar las definiciones de esquemas de seguridad
    assertEquals("apiKey", node, "/components/securitySchemes/apiKey/type");
    assertEquals("user", node, "/components/securitySchemes/apiKey/in");
    assertEquals("API Key", node, "/components/securitySchemes/apiKey/name");
    
    assertEquals("http", node, "/components/securitySchemes/basicAuth/type");
    assertEquals("basic", node, "/components/securitySchemes/basicAuth/scheme");
  }

  @Test
  public void can_parse_security_requirements() {
    JsonNode node = parseResource(AsyncApiGrammar.SECURITY_REQUIREMENT, "/models/v4/security/securityRequirements.yaml");

    // Verificar los requisitos de seguridad aplicados a nivel de API o canal específico
    assertElements(node, "/security").containsExactlyInAnyOrder("apiKey", "basicAuth");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_security_scheme_is_incomplete() {
    parseResource(AsyncApiGrammar.SECURITY_SCHEMES_COMPONENT, "/models/v4/security/incompleteSecurityScheme.yaml");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_security_requirement_is_unsatisfied() {
    parseResource(AsyncApiGrammar.SECURITY_REQUIREMENT, "/models/v4/security/unsatisfiedSecurityRequirement.yaml");
  }
}
