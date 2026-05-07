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
package org.apiaddicts.apitools.dosonarapi.api.v32;

import org.sonar.sslr.grammar.GrammarRuleKey;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiGrammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.YamlGrammarBuilder;

@java.lang.SuppressWarnings("squid:S1192")
public enum OpenApi32Grammar implements GrammarRuleKey {
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
  OPERATION_WEBHOOKS,
  LINK,
  CALLBACK,
  RESPONSES,
  REQUEST_BODY,

  SCHEMA,
  WEBHOOK,
  WEBHOOKS,
  DISCRIMINATOR,
  HEADER,
  EXAMPLE,
  XML,
  SERVER,
  SERVER_VARIABLE,
  HTTP_SECURITY_SCHEME,
  API_KEY_SECURITY_SCHEME,
  MUTUALTLS_SECURITY_SCHEME,
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
  WEBHOOKS_COMPONENT,
  RESPONSES_COMPONENT,
  PARAMETERS_COMPONENT,
  EXAMPLES_COMPONENT,
  BODIES_COMPONENT,
  HEADERS_COMPONENT,
  SECURITY_SCHEMES,
  LINKS_COMPONENT,
  CALLBACKS_COMPONENT,
  PATH_ITEMS_COMPONENT,
  MEDIA_TYPES_COMPONENT,
  SCHEMA_PROPERTIES,
  DESCRIPTION;

  private static final String EXTENSION_PATTERN = "^x-.*";

  public static YamlGrammarBuilder create() {
    YamlGrammarBuilder b = new YamlGrammarBuilder();
    b.setRootRule(ROOT);

    b.rule(ROOT).is(b.object(
      b.mandatoryProperty("openapi", "3.2.0"),
      b.mandatoryProperty("info", INFO),
      b.property("$self", b.string()),
      b.property("jsonSchemaDialect", b.string()),
      b.property("servers", b.array(SERVER)),
      b.property("paths", PATHS),
      b.property("webhooks", WEBHOOKS),
      b.property("components", COMPONENTS),
      b.property("security", b.array(SECURITY_REQUIREMENT)),
      b.property("tags", b.array(TAG)),
      b.property("externalDocs", EXTERNAL_DOC),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));

    b.rule(REF).is(b.object(
      b.mandatoryProperty("$ref", b.string()),
      b.property("summary", b.string()),
      b.property("description", DESCRIPTION)));

    b.rule(EXTERNAL_DOC).is(b.object(
      b.property("description", DESCRIPTION),
      b.mandatoryProperty("url", b.string()),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));

    b.rule(DESCRIPTION).is(b.string()).skip();
    buildInfo(b);
    buildServer(b);
    buildPaths(b);
    buildWebhooks(b);
    buildComponents(b);
    buildSecurityDefinitions(b);
    OpenApiGrammar.buildTags(b, TAG, DESCRIPTION, EXTERNAL_DOC);

    return b;
  }

  private static void buildSecurityDefinitions(YamlGrammarBuilder b) {
    OpenApiGrammar.buildMutualTlsSecuritySetup(b, SECURITY_SCHEME,
      HTTP_SECURITY_SCHEME, API_KEY_SECURITY_SCHEME, OAUTH2_SECURITY_SCHEME, OPENID_SECURITY_SCHEME,
      MUTUALTLS_SECURITY_SCHEME, FLOWS, DESCRIPTION);
    b.rule(MUTUALTLS_SECURITY_SCHEME).is(b.object(
      b.discriminant("type", "mutualTLS"),
      b.property("description", DESCRIPTION),
      b.patternProperty(EXTENSION_PATTERN, b.anything()))).skip();
    OpenApiGrammar.buildSecurityFlows(b,
      FLOWS, IMPLICIT_FLOW, PASSWORD_FLOW, CREDENTIALS_FLOW, AUTH_FLOW, SECURITY_REQUIREMENT);
  }

  private static void buildCallbacks(YamlGrammarBuilder b) {
    OpenApiGrammar.buildCallbacks(b, CALLBACK, LINK, PATH, SERVER, DESCRIPTION);
  }

  private static void buildResponses(YamlGrammarBuilder b) {
    OpenApiGrammar.buildResponsesAndHeader(b,
      RESPONSES, RESPONSE, REF, HEADER, SCHEMA, EXAMPLE, MEDIA_TYPE, LINK, DESCRIPTION);
    b.rule(EXAMPLE).is(b.object(
      b.property("summary", b.string()),
      b.property("description", DESCRIPTION),
      b.property("value", b.anything()),
      b.property("externalValue", b.string()),
      b.property("dataValue", b.anything()),
      b.property("serializedValue", b.string()),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  private static void buildParameters(YamlGrammarBuilder b) {
    b.rule(PARAMETER).is(b.object(
      b.mandatoryProperty("name", b.string()),
      b.mandatoryProperty("in", b.firstOf("path", "query", "querystring", "header", "cookie")),
      b.property("description", DESCRIPTION),
      b.property("required", b.bool()),
      b.property("deprecated", b.bool()),
      b.property("allowEmptyValue", b.bool()),
      b.property("style", b.firstOf("matrix", "label", "form", "simple", "spaceDelimited", "pipeDelimited", "deepObject")),
      b.property("explode", b.bool()),
      b.property("allowReserved", b.bool()),
      b.property("schema", b.firstOf(REF, SCHEMA)),
      b.property("example", b.anything()),
      b.property("examples", b.object(
        b.patternProperty(".*", b.firstOf(REF, EXAMPLE)))),
      b.property("content", b.object(
        b.patternProperty(".*", MEDIA_TYPE))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    OpenApiGrammar.buildRequestBody(b, REQUEST_BODY, REF, MEDIA_TYPE, DESCRIPTION);
    b.rule(MEDIA_TYPE).is(b.object(
      b.property("schema", b.firstOf(REF, SCHEMA)),
      b.property("example", b.anything()),
      b.property("examples", b.object(
        b.patternProperty(".*", b.firstOf(REF, EXAMPLE)))),
      b.property("encoding", b.object(
        b.patternProperty(".*", ENCODING))),
      b.property("headers", b.object(
        b.patternProperty(".*", b.firstOf(REF, HEADER)))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    OpenApiGrammar.buildEncoding(b, ENCODING, REF, HEADER);
  }

  private static void buildComponents(YamlGrammarBuilder b) {
    b.rule(COMPONENTS).is(b.object(
      b.property("schemas", SCHEMAS_COMPONENT),
      b.property("webhooks", WEBHOOKS_COMPONENT),
      b.property("responses", RESPONSES_COMPONENT),
      b.property("parameters", PARAMETERS_COMPONENT),
      b.property("examples", EXAMPLES_COMPONENT),
      b.property("requestBodies", BODIES_COMPONENT),
      b.property("headers", HEADERS_COMPONENT),
      b.property("securitySchemes", SECURITY_SCHEMES),
      b.property("links", LINKS_COMPONENT),
      b.property("callbacks", CALLBACKS_COMPONENT),
      b.property("pathItems", PATH_ITEMS_COMPONENT),
      b.property("mediaTypes", MEDIA_TYPES_COMPONENT),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    OpenApiGrammar.buildBaseComponentRules(b,
      SCHEMAS_COMPONENT, RESPONSES_COMPONENT, PARAMETERS_COMPONENT, EXAMPLES_COMPONENT,
      BODIES_COMPONENT, HEADERS_COMPONENT, SECURITY_SCHEMES, LINKS_COMPONENT, CALLBACKS_COMPONENT,
      REF, SCHEMA, RESPONSE, PARAMETER, EXAMPLE, REQUEST_BODY, HEADER, SECURITY_SCHEME, LINK, CALLBACK);
    b.rule(WEBHOOKS_COMPONENT).is(b.object(b.patternProperty(".*", b.firstOf(REF, WEBHOOK))));
    b.rule(PATH_ITEMS_COMPONENT).is(b.object(b.patternProperty(".*", b.firstOf(REF, PATH))));
    b.rule(MEDIA_TYPES_COMPONENT).is(b.object(b.patternProperty(".*", b.firstOf(REF, MEDIA_TYPE))));

    buildParameters(b);
    buildResponses(b);
    buildSchema(b);
    buildCallbacks(b);
  }

  private static void buildSchema(YamlGrammarBuilder b) {
    b.rule(SCHEMA).is(b.object(
      b.property("title", b.string()),
      b.property("multipleOf", b.firstOf(b.integer(), b.floating())),
      b.property("maximum", b.firstOf(b.integer(), b.floating())),
      b.property("minimum", b.firstOf(b.integer(), b.floating())),
      b.property("exclusiveMaximum", b.firstOf(b.integer(), b.floating())),
      b.property("exclusiveMinimum", b.firstOf(b.integer(), b.floating())),
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
      b.property("type", b.firstOf(b.string(), b.array(b.string()))),
      b.property("contentMediaType", b.string()),
      b.property("contentEncoding", b.string()),
      b.property("allOf", b.array(b.firstOf(REF, SCHEMA))),
      b.property("oneOf", b.array(b.firstOf(REF, SCHEMA))),
      b.property("anyOf", b.array(b.firstOf(REF, SCHEMA))),
      b.property("not", b.firstOf(REF, SCHEMA)),
      b.property("if", b.firstOf(REF, SCHEMA)),
      b.property("then", b.firstOf(REF, SCHEMA)),
      b.property("else", b.firstOf(REF, SCHEMA)),
      b.property("prefixItems", b.array(b.firstOf(REF, SCHEMA))),
      b.property("items", b.firstOf(REF, SCHEMA)),
      b.property("contains", b.firstOf(REF, SCHEMA)),
      b.property("minContains", b.integer()),
      b.property("maxContains", b.integer()),
      b.property("unevaluatedItems", b.firstOf(b.bool(), REF, SCHEMA)),
      b.property("properties", SCHEMA_PROPERTIES),
      b.property("patternProperties", SCHEMA_PROPERTIES),
      b.property("propertyNames", b.firstOf(REF, SCHEMA)),
      b.property("dependentSchemas", b.object(b.patternProperty(".*", b.firstOf(REF, SCHEMA)))),
      b.property("dependentRequired", b.object(b.patternProperty(".*", b.array(b.string())))),
      b.property("$schema", b.string()),
      b.property("$anchor", b.string()),
      b.property("$defs", b.object(b.patternProperty(".*", b.firstOf(REF, SCHEMA)))),
      b.property("$dynamicRef", b.string()),
      b.property("$dynamicAnchor", b.string()),
      b.property("$comment", b.string()),
      b.property("additionalProperties", b.firstOf(b.bool(), REF, SCHEMA)),
      b.property("description", DESCRIPTION),
      b.property("unevaluatedProperties", b.firstOf(b.bool(), REF, SCHEMA)),
      b.property("format", b.string()),
      b.property("default", b.anything()),
      b.property("nullable", b.bool()),
      b.property("discriminator", DISCRIMINATOR),
      b.property("const", b.anything()),
      b.property("readOnly", b.bool()),
      b.property("writeOnly", b.bool()),
      b.property("xml", XML),
      b.property("externalDocs", EXTERNAL_DOC),
      b.property("examples", b.array(b.anything())),
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
      b.property("text", b.bool()),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  private static void buildWebhooks(YamlGrammarBuilder b) {
    b.rule(WEBHOOKS).is(b.object(
      b.patternProperty("^.*", WEBHOOK),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(WEBHOOK).is(b.object(
      b.property("$ref", b.string()),
      b.property("summary", b.string()),
      b.property("description", DESCRIPTION),
      b.property("get", OPERATION),
      b.property("put", OPERATION),
      b.property("post", OPERATION),
      b.property("delete", OPERATION),
      b.property("options", OPERATION),
      b.property("head", OPERATION),
      b.property("patch", OPERATION),
      b.property("trace", OPERATION),
      b.property("query", OPERATION),
      b.property("additionalOperations", b.object(b.patternProperty(".*", OPERATION))),
      b.property("servers", b.array(SERVER)),
      b.property("parameters", b.array(b.firstOf(REF, PARAMETER))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    OpenApiGrammar.buildOperation(b, OPERATION_WEBHOOKS, REF, PARAMETER, REQUEST_BODY, RESPONSES,
      CALLBACK, EXTERNAL_DOC, SECURITY_REQUIREMENT, SERVER, DESCRIPTION);
  }

  private static void buildPaths(YamlGrammarBuilder b) {
    b.rule(PATHS).is(b.object(
      b.patternProperty("^/.*", PATH),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(PATH).is(b.object(
      b.property("$ref", b.string()),
      b.property("summary", b.string()),
      b.property("description", DESCRIPTION),
      b.property("get", OPERATION),
      b.property("put", OPERATION),
      b.property("post", OPERATION),
      b.property("delete", OPERATION),
      b.property("options", OPERATION),
      b.property("head", OPERATION),
      b.property("patch", OPERATION),
      b.property("trace", OPERATION),
      b.property("query", OPERATION),
      b.property("additionalOperations", b.object(b.patternProperty(".*", OPERATION))),
      b.property("servers", b.array(SERVER)),
      b.property("parameters", b.array(b.firstOf(REF, PARAMETER))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    OpenApiGrammar.buildOperation(b, OPERATION, REF, PARAMETER, REQUEST_BODY, RESPONSES,
      CALLBACK, EXTERNAL_DOC, SECURITY_REQUIREMENT, SERVER, DESCRIPTION);
  }

  private static void buildServer(YamlGrammarBuilder b) {
    b.rule(SERVER).is(b.object(
      b.mandatoryProperty("url", b.string()),
      b.property("name", b.string()),
      b.property("description", DESCRIPTION),
      b.property("variables", b.object(
        b.patternProperty(".*", SERVER_VARIABLE))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(SERVER_VARIABLE).is(b.object(
      b.property("enum", b.array(b.string())),
      b.mandatoryProperty("default", b.string()),
      b.property("description", DESCRIPTION),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  private static void buildInfo(YamlGrammarBuilder b) {
    b.rule(INFO).is(b.object(
      b.mandatoryProperty("title", b.string()),
      b.property("summary", b.string()),
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
      b.property("identifier", b.string()),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }
}
