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
package org.apiaddicts.apitools.dosonarapi.api.v4;

import org.sonar.sslr.grammar.GrammarRuleKey;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.YamlGrammarBuilder;

@java.lang.SuppressWarnings("squid:S1192") // Voluntarily ignoring string constants redefinitions in this file
public enum AsyncApiGrammar implements GrammarRuleKey {
  ROOT,
  INFO,
  CHANNEL,
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
  DESCRIPTION,
  
  CHANNELS, CHANNEL_ITEM, 
  MESSAGE, MESSAGES, MESSAGE_TRAIT, MESSAGE_BINDING, MESSAGES_COMPONENT, MESSAGE_EXAMPLE, 
  OPERATION_TRAIT, OPERATION_BINDING, 
  SERVER_BINDINGS, CHANNEL_BINDINGS, MESSAGE_BINDINGS, OPERATION_BINDINGS, SCHEMA_BINDINGS, 
  SERVER_BINDING, CHANNEL_BINDING, 
  CORRELATION_ID, BINDING_DEFINITION,
  HEADERS_SCHEMA, PAYLOAD_SCHEMA; 

  private static final String EXTENSION_PATTERN = "^x-.*";

  public static YamlGrammarBuilder create() {
    YamlGrammarBuilder b = new YamlGrammarBuilder();
    b.setRootRule(ROOT);

    b.rule(ROOT).is(b.object(
      b.mandatoryProperty("asyncapi", b.firstOf("2.0.0", "2.1.0", "2.2.0", "2.3.0", "2.4.0")), 
      b.mandatoryProperty("info", INFO),
      b.property("servers", b.array(SERVER)),
      b.mandatoryProperty("channels", CHANNELS),
      b.property("components", COMPONENTS),
      b.property("tags", b.array(TAG)),
      b.property("externalDocs", EXTERNAL_DOC),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));

    b.rule(DESCRIPTION).is(b.string()).skip();
    buildInfo(b);
    buildServer(b);
    buildComponents(b);
    buildChannels(b);
    buildSecurityDefinitions(b);
    buildTags(b);

    return b;
  }

  private static void buildTags(YamlGrammarBuilder b) {
    b.rule(TAG).is(b.object(
      b.mandatoryProperty("name", b.string()),
      b.property("description", DESCRIPTION),
      b.property("externalDocs", EXTERNAL_DOC),
      b.patternProperty(EXTENSION_PATTERN, b.anything())
    ));
  }

  private static void buildSecurityDefinitions(YamlGrammarBuilder b) {
    b.rule(SECURITY_SCHEME).is(
      b.firstOf(HTTP_SECURITY_SCHEME, API_KEY_SECURITY_SCHEME, OAUTH2_SECURITY_SCHEME, OPENID_SECURITY_SCHEME));
    b.rule(HTTP_SECURITY_SCHEME).is(b.object(
      b.discriminant("type", "http"),
      b.property("description", DESCRIPTION),
      b.mandatoryProperty("scheme", b.string()),
      b.property("bearerFormat", b.string()),
      b.patternProperty(EXTENSION_PATTERN, b.anything()))).skip();
    b.rule(API_KEY_SECURITY_SCHEME).is(b.object(
      b.discriminant("type", "apiKey"),
      b.property("description", DESCRIPTION),
      b.mandatoryProperty("name", b.string()),
      b.mandatoryProperty("in", b.firstOf("query", "header", "cookie")),
      b.patternProperty(EXTENSION_PATTERN, b.anything()))).skip();
    b.rule(OAUTH2_SECURITY_SCHEME).is(b.object(
      b.discriminant("type", "oauth2"),
      b.property("description", DESCRIPTION),
      b.mandatoryProperty("flows", FLOWS),
      b.patternProperty(EXTENSION_PATTERN, b.anything()))).skip();
    b.rule(OPENID_SECURITY_SCHEME).is(b.object(
      b.discriminant("type", "openIdConnect"),
      b.property("description", DESCRIPTION),
      b.mandatoryProperty("openIdConnectUrl", b.string()),
      b.patternProperty(EXTENSION_PATTERN, b.anything()))).skip();
    b.rule(FLOWS).is(b.object(
      b.property("implicit", IMPLICIT_FLOW),
      b.property("password", PASSWORD_FLOW),
      b.property("clientCredentials", CREDENTIALS_FLOW),
      b.property("authorizationCode", AUTH_FLOW),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(IMPLICIT_FLOW).is(b.object(
      b.mandatoryProperty("authorizationUrl", b.string()),
      b.property("refreshUrl", b.string()),
      b.mandatoryProperty("scopes", b.object(
        b.patternProperty(".*", b.string()))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(PASSWORD_FLOW).is(b.object(
      b.mandatoryProperty("tokenUrl", b.string()),
      b.property("refreshUrl", b.string()),
      b.mandatoryProperty("scopes", b.object(
        b.patternProperty(".*", b.string()))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(CREDENTIALS_FLOW).is(b.object(
      b.mandatoryProperty("tokenUrl", b.string()),
      b.property("refreshUrl", b.string()),
      b.mandatoryProperty("scopes", b.object(
        b.patternProperty(".*", b.string()))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(AUTH_FLOW).is(b.object(
      b.mandatoryProperty("authorizationUrl", b.string()),
      b.mandatoryProperty("tokenUrl", b.string()),
      b.property("refreshUrl", b.string()),
      b.mandatoryProperty("scopes", b.object(
        b.patternProperty(".*", b.string()))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(SECURITY_REQUIREMENT).is(b.object(
      b.patternProperty(".*", b.array(b.string()))));
  }

  private static void buildChannels(YamlGrammarBuilder b) {
    b.rule(CHANNELS).is(b.object(
      b.patternProperty("^[^/].*", CHANNEL), 
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    
    b.rule(CHANNEL).is(b.object(
      b.property("$ref", b.string()),
      b.property("description", DESCRIPTION),
      b.property("subscribe", OPERATION),
      b.property("publish", OPERATION),
      b.property("parameters", b.array(b.firstOf(REF, PARAMETER))),
      b.property("bindings", CHANNEL_BINDINGS),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));

    b.rule(OPERATION).is(b.object(
      b.property("operationId", b.string()),
      b.property("summary", b.string()),
      b.property("description", DESCRIPTION),
      b.property("tags", b.array(TAG)),
      b.property("externalDocs", EXTERNAL_DOC),
      b.property("message", b.firstOf(REF, MESSAGE)),
      b.property("bindings", OPERATION_BINDINGS),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));

    b.rule(CHANNEL_BINDINGS).is(b.object(
      b.patternProperty(".*", BINDING_DEFINITION),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    
    b.rule(OPERATION_BINDINGS).is(b.object(
      b.patternProperty(".*", BINDING_DEFINITION),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
      
    b.rule(BINDING_DEFINITION).is(b.object(
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  private static void buildComponents(YamlGrammarBuilder b) {
    b.rule(COMPONENTS).is(b.object(
      b.property("schemas", b.object(
          b.patternProperty(".+", SCHEMA))),
      b.property("messages", b.object(
          b.patternProperty(".+", MESSAGE))),
      b.property("messageTraits", b.object(
          b.patternProperty(".+", MESSAGE_TRAIT))),
      b.property("operationTraits", b.object(
          b.patternProperty(".+", OPERATION_TRAIT))),
      b.property("parameters", b.object(
          b.patternProperty(".+", PARAMETER))),
      b.property("securitySchemes", b.object(
          b.patternProperty(".+", SECURITY_SCHEME))),
      b.property("serverBindings", b.object(
          b.patternProperty(".+", SERVER_BINDING))),
      b.property("channelBindings", b.object(
          b.patternProperty(".+", CHANNEL_BINDING))),
      b.property("operationBindings", b.object(
          b.patternProperty(".+", OPERATION_BINDING))),
      b.property("messageBindings", b.object(
          b.patternProperty(".+", MESSAGE_BINDING))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())
    ));
      b.rule(SCHEMAS_COMPONENT).is(b.object(b.patternProperty(".*", b.firstOf(REF, SCHEMA))));
      b.rule(MESSAGES_COMPONENT).is(b.object(b.patternProperty(".*", b.firstOf(REF, MESSAGE))));
      b.rule(PARAMETERS_COMPONENT).is(b.object(b.patternProperty(".*", b.firstOf(REF, PARAMETER))));
      b.rule(SECURITY_SCHEMES).is(b.object(b.patternProperty(".*", b.firstOf(REF, SECURITY_SCHEME))));
      buildSchema(b);
      buildMessages(b);
      buildMessageTraits(b);
      buildOperationTraits(b);
  }

  private static void buildMessageTraits(YamlGrammarBuilder b) {
    b.rule(MESSAGE_TRAIT).is(b.object(
      b.property("contentType", b.string()),
      b.property("headers", b.object(b.patternProperty(".+", SCHEMA))),
      b.property("correlationId", b.object(
          b.property("description", DESCRIPTION),
          b.property("location", b.string())
      )),
      b.property("schemaFormat", b.string()),
      b.property("name", b.string()),
      b.property("description", DESCRIPTION),
      b.property("tags", b.array(TAG)),
      b.property("externalDocs", EXTERNAL_DOC),
      b.patternProperty(EXTENSION_PATTERN, b.anything())
    ));
  }

  private static void buildOperationTraits(YamlGrammarBuilder b) {
    b.rule(OPERATION_TRAIT).is(b.object(
      b.property("operationId", b.string()),
      b.property("summary", b.string()),
      b.property("description", DESCRIPTION),
      b.property("tags", b.array(TAG)),
      b.property("externalDocs", EXTERNAL_DOC),
      b.property("bindings", OPERATION_BINDINGS),
      b.patternProperty(EXTENSION_PATTERN, b.anything())
    ));
  }

  private static void buildMessages(YamlGrammarBuilder b) {
    b.rule(HEADERS_SCHEMA).is(b.object(
      b.property("correlationId", b.string()),
      b.property("contentType", b.string()),
      b.property("authorization", b.string()),
      b.property("customHeader", b.string())
    ));

    b.rule(PAYLOAD_SCHEMA).is(b.object(
      b.property("type", b.string()),
      b.property("data", b.anything())
    ));

    b.rule(EXAMPLE).is(b.object(
      b.property("summary", b.string()),
      b.property("value", b.object(
        b.property("headers", HEADERS_SCHEMA),
        b.property("payload", PAYLOAD_SCHEMA)
      ))
    ));

    b.rule(MESSAGE_BINDINGS).is(b.object(
      b.property("mqtt", b.object(
        b.property("qos", b.integer()),
        b.property("retain", b.string())
      )),
      b.property("amqp", b.object(
        b.property("contentEncoding", b.string()),
        b.property("messageType", b.string())
      ))
    ));

    b.rule(MESSAGE).is(b.object(
      b.property("contentType", b.string()),
      b.property("headers", HEADERS_SCHEMA),
      b.property("payload", PAYLOAD_SCHEMA),
      b.property("name", b.string()),
      b.property("title", b.string()),
      b.property("summary", b.string()),
      b.property("description", DESCRIPTION),
      b.property("tags", b.array(TAG)),
      b.property("externalDocs", EXTERNAL_DOC),
      b.property("examples", b.array(EXAMPLE)),
      b.property("bindings", MESSAGE_BINDINGS),
      b.patternProperty(EXTENSION_PATTERN, b.anything())
    ));
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
  private static void buildServer(YamlGrammarBuilder b) {
    b.rule(SERVER).is(b.object(
      b.mandatoryProperty("url", b.string()),
      b.mandatoryProperty("protocol", b.string()), 
      b.property("description", DESCRIPTION),
      b.property("variables", b.object( 
        b.patternProperty(".*", SERVER_VARIABLE))),
      b.property("security", b.array(SECURITY_REQUIREMENT)), 
      b.patternProperty(EXTENSION_PATTERN, b.anything())
    ));

    b.rule(SERVER_VARIABLE).is(b.object(
      b.property("enum", b.array(b.string())), 
      b.mandatoryProperty("default", b.string()), 
      b.property("description", DESCRIPTION),
      b.patternProperty(EXTENSION_PATTERN, b.anything())
    ));
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
