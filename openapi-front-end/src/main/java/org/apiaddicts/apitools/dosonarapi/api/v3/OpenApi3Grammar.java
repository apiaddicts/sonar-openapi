/*
 * doSonarAPI: SonarQube OpenAPI Plugin
 * Copyright (C) 2021-2022 Apiaddicts
 * contacta AT apiaddicts DOT org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.apiaddicts.apitools.dosonarapi.api.v3;

import org.sonar.sslr.grammar.GrammarRuleKey;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiGrammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.YamlGrammarBuilder;

@java.lang.SuppressWarnings("squid:S1192") // Voluntarily ignoring string constants redefinitions in this file
public enum OpenApi3Grammar implements GrammarRuleKey {
  ROOT,
  INFO,
  PATHS,
  COMPONENTS,
  PARAMETER,
  RESPONSE,
  SECURITY_SCHEME,
  SECURITY_REQUIREMENT,
  TAG,
  REF,
  EXTERNAL_DOC,
  CONTACT,
  LICENSE,

  PATH,
  OPERATION,
  LINK,
  CALLBACK,
  RESPONSES,
  REQUEST_BODY,

  SCHEMA,
  DISCRIMINATOR,
  HEADER,
  EXAMPLE,
  XML,
  SERVER,
  SERVER_VARIABLE,
  HTTP_SECURITY_SCHEME,
  API_KEY_SECURITY_SCHEME,
  OAUTH2_SECURITY_SCHEME,
  OPENID_SECURITY_SCHEME,
  MEDIA_TYPE,
  ENCODING,
  FLOWS,
  IMPLICIT_FLOW,
  PASSWORD_FLOW,
  CREDENTIALS_FLOW,
  AUTH_FLOW,
  SCHEMAS_COMPONENT,
  RESPONSES_COMPONENT,
  PARAMETERS_COMPONENT,
  EXAMPLES_COMPONENT,
  BODIES_COMPONENT,
  HEADERS_COMPONENT,
  SECURITY_SCHEMES,
  LINKS_COMPONENT,
  CALLBACKS_COMPONENT,
  SCHEMA_PROPERTIES,
  DESCRIPTION;

  private static final String EXTENSION_PATTERN = "^x-.*";

  public static YamlGrammarBuilder create() {
    YamlGrammarBuilder b = new YamlGrammarBuilder();
    b.setRootRule(ROOT);

    b.rule(ROOT).is(b.object(
      b.mandatoryProperty("openapi", b.firstOf("3.0.0", "3.0.1", "3.0.2", "3.0.3")),
      b.mandatoryProperty("info", INFO),
      b.property("servers", b.array(SERVER)),
      b.mandatoryProperty("paths", PATHS),
      b.property("components", COMPONENTS),
      b.property("security", b.array(SECURITY_REQUIREMENT)),
      b.property("tags", b.array(TAG)),
      b.property("externalDocs", EXTERNAL_DOC),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(REF).is(b.object(
      b.mandatoryProperty("$ref", b.string())));
    b.rule(EXTERNAL_DOC).is(b.object(
      b.property("description", DESCRIPTION),
      b.mandatoryProperty("url", b.string()),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));

    b.rule(DESCRIPTION).is(b.string()).skip();
    buildInfo(b);
    buildServer(b);
    buildPaths(b);
    buildComponents(b);
    buildSecurityDefinitions(b);
    buildTags(b);

    return b;
  }

  private static void buildTags(YamlGrammarBuilder b) {
    OpenApiGrammar.buildTags(b, TAG, DESCRIPTION, EXTERNAL_DOC);
  }

  private static void buildSecurityDefinitions(YamlGrammarBuilder b) {
    b.rule(SECURITY_SCHEME).is(
      b.firstOf(HTTP_SECURITY_SCHEME, API_KEY_SECURITY_SCHEME, OAUTH2_SECURITY_SCHEME, OPENID_SECURITY_SCHEME));
    OpenApiGrammar.buildCommonSecuritySchemes(b,
      HTTP_SECURITY_SCHEME, API_KEY_SECURITY_SCHEME, OAUTH2_SECURITY_SCHEME, OPENID_SECURITY_SCHEME,
      FLOWS, DESCRIPTION);
    OpenApiGrammar.buildSecurityFlows(b,
      FLOWS, IMPLICIT_FLOW, PASSWORD_FLOW, CREDENTIALS_FLOW, AUTH_FLOW, SECURITY_REQUIREMENT);
  }

  private static void buildResponses(YamlGrammarBuilder b) {
    OpenApiGrammar.buildResponsesAndHeader(b,
      RESPONSES, RESPONSE, REF, HEADER, SCHEMA, EXAMPLE, MEDIA_TYPE, LINK, DESCRIPTION);
    OpenApiGrammar.buildStandardExample(b, EXAMPLE, DESCRIPTION);
  }

  private static void buildParameters(YamlGrammarBuilder b) {
    OpenApiGrammar.buildStandardParameters(b, PARAMETER, REQUEST_BODY, MEDIA_TYPE, ENCODING,
      REF, SCHEMA, EXAMPLE, HEADER, DESCRIPTION);
  }

  private static void buildComponents(YamlGrammarBuilder b) {
    b.rule(COMPONENTS).is(b.object(
      b.property("schemas", SCHEMAS_COMPONENT),
      b.property("responses", RESPONSES_COMPONENT),
      b.property("parameters", PARAMETERS_COMPONENT),
      b.property("examples", EXAMPLES_COMPONENT),
      b.property("requestBodies", BODIES_COMPONENT),
      b.property("headers", HEADERS_COMPONENT),
      b.property("securitySchemes", SECURITY_SCHEMES),
      b.property("links", LINKS_COMPONENT),
      b.property("callbacks", CALLBACKS_COMPONENT),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    OpenApiGrammar.buildBaseComponentRules(b,
      SCHEMAS_COMPONENT, RESPONSES_COMPONENT, PARAMETERS_COMPONENT, EXAMPLES_COMPONENT,
      BODIES_COMPONENT, HEADERS_COMPONENT, SECURITY_SCHEMES, LINKS_COMPONENT, CALLBACKS_COMPONENT,
      REF, SCHEMA, RESPONSE, PARAMETER, EXAMPLE, REQUEST_BODY, HEADER, SECURITY_SCHEME, LINK, CALLBACK);

    buildParameters(b);
    buildResponses(b);
    buildSchema(b);
    OpenApiGrammar.buildCallbacks(b, CALLBACK, LINK, PATH, SERVER, DESCRIPTION);
  }

  private static void buildSchema(YamlGrammarBuilder b) {
    b.rule(SCHEMA).is(b.object(
      b.property("title", b.string()),
      b.property("multipleOf", b.firstOf(b.integer(), b.floating())),
      b.property("maximum", b.firstOf(b.integer(), b.floating())),
      b.property("exclusiveMaximum", b.bool()),
      b.property("minimum", b.firstOf(b.integer(), b.floating())),
      b.property("exclusiveMinimum", b.bool()),
      b.property("maxLength", b.integer()),
      b.property("minLength", b.integer()),
      b.property("pattern", b.string()),
      b.property("maxItems", b.integer()),
      b.property("minItems", b.integer()),
      b.property("uniqueItems", b.bool()),
      b.property("maxProperties", b.integer()),
      b.property("minProperties", b.integer()),
      b.property("required", b.array(b.string())),
      b.property("enum", b.array(b.anything())),
      b.property("type", b.firstOf("object", "string", "number", "integer", "boolean", "array", "null")),
      b.property("allOf", b.array(b.firstOf(REF, SCHEMA))),
      b.property("oneOf", b.array(b.firstOf(REF, SCHEMA))),
      b.property("anyOf", b.array(b.firstOf(REF, SCHEMA))),
      b.property("not", b.firstOf(REF, SCHEMA)),
      b.property("items", b.firstOf(REF, SCHEMA)),
      b.property("properties", SCHEMA_PROPERTIES),
      b.property("additionalProperties", b.firstOf(b.bool(), REF, SCHEMA)),
      b.property("description", DESCRIPTION),
      b.property("format", b.string()),
      b.property("default", b.anything()),
      b.property("nullable", b.bool()),
      b.property("discriminator", DISCRIMINATOR),
      b.property("readOnly", b.bool()),
      b.property("writeOnly", b.bool()),
      b.property("xml", XML),
      b.property("externalDocs", EXTERNAL_DOC),
      b.property("example", b.anything()),
      b.property("deprecated", b.bool()),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(SCHEMA_PROPERTIES).is(b.object(b.patternProperty(".*", b.firstOf(REF, SCHEMA))));
    b.rule(DISCRIMINATOR).is(b.object(
      b.property("propertyName", b.string()),
      b.property("mapping", b.object(
        b.patternProperty(".*", b.string())))));
    b.rule(XML).is(b.object(
      b.property("name", b.string()),
      b.property("namespace", b.string()),
      b.property("prefix", b.string()),
      b.property("attribute", b.bool()),
      b.property("wrapped", b.bool()),
      b.patternProperty(EXTENSION_PATTERN, b.anything())

    ));
  }

  private static void buildPaths(YamlGrammarBuilder b) {
    b.rule(PATHS).is(b.object(
      b.patternProperty("^/.*", PATH),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    OpenApiGrammar.buildPathRule(b, PATH, OPERATION, REF, PARAMETER, SERVER, DESCRIPTION);
    OpenApiGrammar.buildOperation(b, OPERATION, REF, PARAMETER, REQUEST_BODY, RESPONSES,
      CALLBACK, EXTERNAL_DOC, SECURITY_REQUIREMENT, SERVER, DESCRIPTION);
  }

  private static void buildServer(YamlGrammarBuilder b) {
    OpenApiGrammar.buildServerAndVariable(b, SERVER, SERVER_VARIABLE, DESCRIPTION);
  }

  private static void buildInfo(YamlGrammarBuilder b) {
    b.rule(INFO).is(b.object(
      b.mandatoryProperty("title", b.string()),
      b.property("description", DESCRIPTION),
      b.property("termsOfService", b.string()),
      b.property("contact", CONTACT),
      b.property("license", LICENSE),
      b.mandatoryProperty("version", b.string()),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));

    b.rule(CONTACT).is(b.object(
      b.property("name", b.string()),
      b.property("url", b.string()),
      b.property("email", b.string()),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));

    b.rule(LICENSE).is(b.object(
      b.mandatoryProperty("name", b.string()),
      b.property("url", b.string()),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }
}
