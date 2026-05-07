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
package org.apiaddicts.apitools.dosonarapi.api;

import org.sonar.sslr.grammar.GrammarRuleKey;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.YamlGrammarBuilder;

public final class OpenApiGrammar {

  static final String EXTENSION_PATTERN = "^x-.*";

  private static final String PROP_DESCRIPTION = "description";
  private static final String PROP_SUMMARY = "summary";
  private static final String PROP_REQUIRED = "required";
  private static final String PROP_DEPRECATED = "deprecated";
  private static final String PROP_PARAMETERS = "parameters";
  private static final String PROP_CONTENT = "content";
  private static final String PROP_SCHEMA = "schema";
  private static final String PROP_EXAMPLE = "example";
  private static final String PROP_EXAMPLES = "examples";
  private static final String PROP_STYLE = "style";
  private static final String STYLE_SIMPLE = "simple";
  private static final String PROP_EXPLODE = "explode";
  private static final String PROP_ALLOW_RESERVED = "allowReserved";
  private static final String PROP_REFRESH_URL = "refreshUrl";
  private static final String PROP_SCOPES = "scopes";
  private static final String PROP_TOKEN_URL = "tokenUrl";

  private OpenApiGrammar() {}

  public static void buildCommonSecuritySchemes(YamlGrammarBuilder b,
      GrammarRuleKey httpScheme, GrammarRuleKey apiKeyScheme,
      GrammarRuleKey oauth2Scheme, GrammarRuleKey openIdScheme,
      GrammarRuleKey flows, GrammarRuleKey description) {
    b.rule(httpScheme).is(b.object(
      b.discriminant("type", "http"),
      b.property(PROP_DESCRIPTION, description),
      b.mandatoryProperty("scheme", b.string()),
      b.property("bearerFormat", b.string()),
      b.patternProperty(EXTENSION_PATTERN, b.anything()))).skip();
    b.rule(apiKeyScheme).is(b.object(
      b.discriminant("type", "apiKey"),
      b.property(PROP_DESCRIPTION, description),
      b.mandatoryProperty("name", b.string()),
      b.mandatoryProperty("in", b.firstOf("query", "header", "cookie")),
      b.patternProperty(EXTENSION_PATTERN, b.anything()))).skip();
    b.rule(oauth2Scheme).is(b.object(
      b.discriminant("type", "oauth2"),
      b.property(PROP_DESCRIPTION, description),
      b.mandatoryProperty("flows", flows),
      b.patternProperty(EXTENSION_PATTERN, b.anything()))).skip();
    b.rule(openIdScheme).is(b.object(
      b.discriminant("type", "openIdConnect"),
      b.property(PROP_DESCRIPTION, description),
      b.mandatoryProperty("openIdConnectUrl", b.string()),
      b.patternProperty(EXTENSION_PATTERN, b.anything()))).skip();
  }

  public static void buildSecurityFlows(YamlGrammarBuilder b,
      GrammarRuleKey flows, GrammarRuleKey implicitFlow, GrammarRuleKey passwordFlow,
      GrammarRuleKey credentialsFlow, GrammarRuleKey authFlow, GrammarRuleKey securityRequirement) {
    b.rule(flows).is(b.object(
      b.property("implicit", implicitFlow),
      b.property("password", passwordFlow),
      b.property("clientCredentials", credentialsFlow),
      b.property("authorizationCode", authFlow),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(implicitFlow).is(b.object(
      b.mandatoryProperty("authorizationUrl", b.string()),
      b.property(PROP_REFRESH_URL, b.string()),
      b.property(PROP_SCOPES, b.object(b.patternProperty(".*", b.string()))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(passwordFlow).is(b.object(
      b.mandatoryProperty(PROP_TOKEN_URL, b.string()),
      b.property(PROP_REFRESH_URL, b.string()),
      b.property(PROP_SCOPES, b.object(b.patternProperty(".*", b.string()))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(credentialsFlow).is(b.object(
      b.mandatoryProperty(PROP_TOKEN_URL, b.string()),
      b.property(PROP_REFRESH_URL, b.string()),
      b.property(PROP_SCOPES, b.object(b.patternProperty(".*", b.string()))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(authFlow).is(b.object(
      b.mandatoryProperty("authorizationUrl", b.string()),
      b.mandatoryProperty(PROP_TOKEN_URL, b.string()),
      b.property(PROP_REFRESH_URL, b.string()),
      b.property(PROP_SCOPES, b.object(b.patternProperty(".*", b.string()))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(securityRequirement).is(b.object(
      b.patternProperty(".*", b.array(b.string()))));
  }

  public static void buildCallbacks(YamlGrammarBuilder b,
      GrammarRuleKey callback, GrammarRuleKey link,
      GrammarRuleKey path, GrammarRuleKey server, GrammarRuleKey description) {
    b.rule(callback).is(b.object(
      b.patternProperty("^[^x].*", path),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(link).is(b.object(
      b.property("operationRef", b.string()),
      b.property("operationId", b.string()),
      b.property(PROP_PARAMETERS, b.object(b.patternProperty(".*", b.anything()))),
      b.property("requestBody", b.anything()),
      b.property(PROP_DESCRIPTION, description),
      b.property("server", server),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  @SuppressWarnings("java:S107")
  public static void buildResponsesAndHeader(YamlGrammarBuilder b,
      GrammarRuleKey responses, GrammarRuleKey response, GrammarRuleKey ref,
      GrammarRuleKey header, GrammarRuleKey schema, GrammarRuleKey example,
      GrammarRuleKey mediaType, GrammarRuleKey link, GrammarRuleKey description) {
    b.rule(responses).is(b.object(
      b.property("default", b.firstOf(response, ref)),
      b.patternProperty("^[0-9xX]+", b.firstOf(response, ref)),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(response).is(b.object(
      b.mandatoryProperty(PROP_DESCRIPTION, description),
      b.property("headers", b.object(b.patternProperty(".*", b.firstOf(ref, header)))),
      b.property(PROP_CONTENT, b.object(b.patternProperty(".*", mediaType))),
      b.property("links", b.object(b.patternProperty(".*", b.firstOf(ref, link)))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    buildHeader(b, header, ref, schema, example, mediaType, description);
  }

  public static void buildHeader(YamlGrammarBuilder b,
      GrammarRuleKey header, GrammarRuleKey ref, GrammarRuleKey schema,
      GrammarRuleKey example, GrammarRuleKey mediaType, GrammarRuleKey description) {
    b.rule(header).is(b.object(
      b.property(PROP_DESCRIPTION, description),
      b.property(PROP_REQUIRED, b.bool()),
      b.property(PROP_DEPRECATED, b.bool()),
      b.property("allowEmptyValue", b.bool()),
      b.property(PROP_STYLE, STYLE_SIMPLE),
      b.property(PROP_EXPLODE, b.bool()),
      b.property(PROP_ALLOW_RESERVED, b.bool()),
      b.property(PROP_SCHEMA, b.firstOf(ref, schema)),
      b.property(PROP_EXAMPLE, b.anything()),
      b.property(PROP_EXAMPLES, b.object(b.patternProperty(".*", b.firstOf(ref, example)))),
      b.property(PROP_CONTENT, b.object(b.patternProperty(".*", mediaType))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  @SuppressWarnings("java:S107")
  public static void buildBaseComponentRules(YamlGrammarBuilder b,
      GrammarRuleKey schemasComponent, GrammarRuleKey responsesComponent,
      GrammarRuleKey parametersComponent, GrammarRuleKey examplesComponent,
      GrammarRuleKey bodiesComponent, GrammarRuleKey headersComponent,
      GrammarRuleKey securitySchemes, GrammarRuleKey linksComponent,
      GrammarRuleKey callbacksComponent,
      GrammarRuleKey ref, GrammarRuleKey schema, GrammarRuleKey response,
      GrammarRuleKey parameter, GrammarRuleKey example, GrammarRuleKey requestBody,
      GrammarRuleKey header, GrammarRuleKey securityScheme, GrammarRuleKey link,
      GrammarRuleKey callback) {
    b.rule(schemasComponent).is(b.object(b.patternProperty(".*", b.firstOf(ref, schema))));
    b.rule(responsesComponent).is(b.object(b.patternProperty(".*", b.firstOf(ref, response))));
    b.rule(parametersComponent).is(b.object(b.patternProperty(".*", b.firstOf(ref, parameter))));
    b.rule(examplesComponent).is(b.object(b.patternProperty(".*", b.firstOf(ref, example))));
    b.rule(bodiesComponent).is(b.object(b.patternProperty(".*", b.firstOf(ref, requestBody))));
    b.rule(headersComponent).is(b.object(b.patternProperty(".*", b.firstOf(ref, header))));
    b.rule(securitySchemes).is(b.object(b.patternProperty(".*", b.firstOf(ref, securityScheme))));
    b.rule(linksComponent).is(b.object(b.patternProperty(".*", b.firstOf(ref, link))));
    b.rule(callbacksComponent).is(b.object(b.patternProperty(".*", b.firstOf(ref, callback))));
  }

  @SuppressWarnings("java:S107")
  public static void buildOperation(YamlGrammarBuilder b,
      GrammarRuleKey operation, GrammarRuleKey ref, GrammarRuleKey parameter,
      GrammarRuleKey requestBody, GrammarRuleKey responses,
      GrammarRuleKey callback, GrammarRuleKey externalDoc,
      GrammarRuleKey securityRequirement, GrammarRuleKey server,
      GrammarRuleKey description) {
    b.rule(operation).is(b.object(
      b.property("tags", b.array(b.string())),
      b.property(PROP_SUMMARY, b.string()),
      b.property(PROP_DESCRIPTION, description),
      b.property("externalDocs", externalDoc),
      b.property("operationId", b.string()),
      b.property(PROP_PARAMETERS, b.array(b.firstOf(ref, parameter))),
      b.property("requestBody", b.firstOf(ref, requestBody)),
      b.mandatoryProperty("responses", responses),
      b.property("callbacks", b.object(
        b.patternProperty(".*", b.firstOf(ref, callback)))),
      b.property(PROP_DEPRECATED, b.bool()),
      b.property("security", b.array(securityRequirement)),
      b.property("servers", b.array(server)),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  public static void buildParameter(YamlGrammarBuilder b,
      GrammarRuleKey parameter, GrammarRuleKey ref, GrammarRuleKey schema,
      GrammarRuleKey example, GrammarRuleKey mediaType, GrammarRuleKey description) {
    b.rule(parameter).is(b.object(
      b.mandatoryProperty("name", b.string()),
      b.mandatoryProperty("in", b.firstOf("path", "query", "header", "cookie")),
      b.property(PROP_DESCRIPTION, description),
      b.property(PROP_REQUIRED, b.bool()),
      b.property(PROP_DEPRECATED, b.bool()),
      b.property("allowEmptyValue", b.bool()),
      b.property(PROP_STYLE, b.firstOf("matrix", "label", "form", STYLE_SIMPLE, "spaceDelimited", "pipeDelimited", "deepObject")),
      b.property(PROP_EXPLODE, b.bool()),
      b.property(PROP_ALLOW_RESERVED, b.bool()),
      b.property(PROP_SCHEMA, b.firstOf(ref, schema)),
      b.property(PROP_EXAMPLE, b.anything()),
      b.property(PROP_EXAMPLES, b.object(
        b.patternProperty(".*", b.firstOf(ref, example)))),
      b.property(PROP_CONTENT, b.object(
        b.patternProperty(".*", mediaType))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  public static void buildRequestBody(YamlGrammarBuilder b,
      GrammarRuleKey requestBody, GrammarRuleKey ref, GrammarRuleKey mediaType,
      GrammarRuleKey description) {
    b.rule(requestBody).is(b.object(
      b.property(PROP_DESCRIPTION, description),
      b.property(PROP_REQUIRED, b.bool()),
      b.property(PROP_CONTENT, b.object(
        b.patternProperty(".*", b.firstOf(ref, mediaType)))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  public static void buildMediaType(YamlGrammarBuilder b,
      GrammarRuleKey mediaType, GrammarRuleKey ref, GrammarRuleKey schema,
      GrammarRuleKey example, GrammarRuleKey encoding) {
    b.rule(mediaType).is(b.object(
      b.property(PROP_SCHEMA, b.firstOf(ref, schema)),
      b.property(PROP_EXAMPLE, b.anything()),
      b.property(PROP_EXAMPLES, b.object(
        b.patternProperty(".*", b.firstOf(ref, example)))),
      b.property("encoding", b.object(
        b.patternProperty(".*", encoding))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  public static void buildEncoding(YamlGrammarBuilder b,
      GrammarRuleKey encoding, GrammarRuleKey ref, GrammarRuleKey header) {
    b.rule(encoding).is(b.object(
      b.property("contentType", b.string()),
      b.property("headers", b.object(
        b.patternProperty(".*", b.firstOf(ref, header)))),
      b.property(PROP_STYLE, b.firstOf("matrix", "label", "form", STYLE_SIMPLE, "spaceDelimited", "pipeDelimited", "deepObject")),
      b.property(PROP_EXPLODE, b.bool()),
      b.property(PROP_ALLOW_RESERVED, b.bool()),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  public static void buildPathRule(YamlGrammarBuilder b,
      GrammarRuleKey path, GrammarRuleKey operation, GrammarRuleKey ref,
      GrammarRuleKey parameter, GrammarRuleKey server, GrammarRuleKey description) {
    b.rule(path).is(b.object(
      b.property("$ref", b.string()),
      b.property(PROP_SUMMARY, b.string()),
      b.property(PROP_DESCRIPTION, description),
      b.property("get", operation),
      b.property("put", operation),
      b.property("post", operation),
      b.property("delete", operation),
      b.property("options", operation),
      b.property("head", operation),
      b.property("patch", operation),
      b.property("trace", operation),
      b.property("servers", b.array(server)),
      b.property(PROP_PARAMETERS, b.array(b.firstOf(ref, parameter))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  public static void buildServerAndVariable(YamlGrammarBuilder b,
      GrammarRuleKey server, GrammarRuleKey serverVariable, GrammarRuleKey description) {
    b.rule(server).is(b.object(
      b.mandatoryProperty("url", b.string()),
      b.property(PROP_DESCRIPTION, description),
      b.property("variables", b.object(
        b.patternProperty(".*", serverVariable))),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
    b.rule(serverVariable).is(b.object(
      b.property("enum", b.array(b.string())),
      b.mandatoryProperty("default", b.string()),
      b.property(PROP_DESCRIPTION, description),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  public static void buildTags(YamlGrammarBuilder b,
      GrammarRuleKey tag, GrammarRuleKey description, GrammarRuleKey externalDoc) {
    b.rule(tag).is(b.object(
      b.mandatoryProperty("name", b.string()),
      b.property(PROP_DESCRIPTION, description),
      b.property("externalDocs", externalDoc),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  @SuppressWarnings("java:S107")
  public static void buildMutualTlsSecuritySetup(YamlGrammarBuilder b,
      GrammarRuleKey securityScheme, GrammarRuleKey httpScheme, GrammarRuleKey apiKeyScheme,
      GrammarRuleKey oauth2Scheme, GrammarRuleKey openIdScheme, GrammarRuleKey mutualTlsScheme,
      GrammarRuleKey flows, GrammarRuleKey description) {
    b.rule(securityScheme).is(
      b.firstOf(httpScheme, apiKeyScheme, oauth2Scheme, openIdScheme, mutualTlsScheme));
    buildCommonSecuritySchemes(b, httpScheme, apiKeyScheme, oauth2Scheme, openIdScheme, flows, description);
  }

  public static void buildStandardExample(YamlGrammarBuilder b,
      GrammarRuleKey example, GrammarRuleKey description) {
    b.rule(example).is(b.object(
      b.property(PROP_SUMMARY, b.string()),
      b.property(PROP_DESCRIPTION, description),
      b.property("value", b.anything()),
      b.property("externalValue", b.string()),
      b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }

  @SuppressWarnings("java:S107")
  public static void buildStandardParameters(YamlGrammarBuilder b,
      GrammarRuleKey parameter, GrammarRuleKey requestBody, GrammarRuleKey mediaType,
      GrammarRuleKey encoding, GrammarRuleKey ref, GrammarRuleKey schema,
      GrammarRuleKey example, GrammarRuleKey header, GrammarRuleKey description) {
    buildParameter(b, parameter, ref, schema, example, mediaType, description);
    buildRequestBody(b, requestBody, ref, mediaType, description);
    buildMediaType(b, mediaType, ref, schema, example, encoding);
    buildEncoding(b, encoding, ref, header);
  }
}
