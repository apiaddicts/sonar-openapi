package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;

public class SecuritySchemeTest extends BaseNodeTest<AsyncApiGrammar> {
  @Test
  public void can_parse_apiKey_security_scheme() {
    JsonNode node = parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/securitySchemes/apiKeyScheme.yaml");

    assertEquals("apiKey", node, "/components/securitySchemes/apiKey/type");
    assertEquals("user", node, "/components/securitySchemes/apiKey/in");
    assertEquals("API Key", node, "/components/securitySchemes/apiKey/name");
    assertEquals("API key required", node, "/components/securitySchemes/apiKey/description");
  }

  @Test
  public void can_parse_oauth2_security_scheme() {
    JsonNode node = parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/securitySchemes/oauth2Scheme.yaml");

    assertEquals("oauth2", node, "/components/securitySchemes/oauth2/type");
    assertEquals("implicit", node, "/components/securitySchemes/oauth2/flows/implicit/authorizationUrl");
    assertEquals("http://example.com/api/oauth/dialog", node, "/components/securitySchemes/oauth2/flows/implicit/authorizationUrl");
    assertEquals("write:pets read:pets", node, "/components/securitySchemes/oauth2/flows/implicit/scopes");
  }

  @Test(expected = ValidationException.class)
  public void throws_when_parsing_incomplete_security_scheme() {
    parseResource(AsyncApiGrammar.COMPONENTS, "/models/v4/securitySchemes/incompleteSecurityScheme.yaml");
  }
}

