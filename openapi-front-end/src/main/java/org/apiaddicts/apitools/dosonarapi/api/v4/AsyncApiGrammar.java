package org.apiaddicts.apitools.dosonarapi.api.v4;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.YamlGrammarBuilder;

public enum AsyncApiGrammar implements GrammarRuleKey {
  ROOT,
  INFO,
  SERVERS,
  SERVER,
  SERVER_VARIABLE,
  CHANNELS,
  COMPONENTS,
  MESSAGE,
  OPERATION,
  SECURITY_SCHEME,
  SECURITY_REQUIREMENT,
  TAG,
  EXTERNAL_DOC,
  SCHEMA,
  MESSAGE_TRAIT,
  OPERATION_TRAIT,
  SERVER_BINDINGS,
  CHANNEL_BINDINGS,
  MESSAGE_BINDINGS,
  OPERATION_BINDINGS,
  SCHEMA_BINDINGS,
  DESCRIPTION,
  CONTACT,
  LICENSE,
  CHANNEL,
  REF,
  HEADERS_SCHEMA,
  EXAMPLE,
  PAYLOAD_SCHEMA,
  SERVER_BINDING,
  CHANNEL_BINDING,
  OPERATION_BINDING,
  MESSAGE_BINDING,
  MESSAGES,
  
  // Secciones específicas de AsyncAPI
  CHANNEL_ITEM,
  PARAMETER,
  CORRELATION_ID,
  MESSAGE_EXAMPLE,
  // Componentes
  MESSAGES_COMPONENT,
  PARAMETERS_COMPONENT,
  SECURITY_SCHEMES_COMPONENT,
  // Bindings
  SERVER_BINDINGS_COMPONENT,
  CHANNEL_BINDINGS_COMPONENT,
  MESSAGE_BINDINGS_COMPONENT,
  OPERATION_BINDINGS_COMPONENT,
  SCHEMA_BINDINGS_COMPONENT;


  

  

  private static final String EXTENSION_PATTERN = "^x-.*";

    public static YamlGrammarBuilder create() {
        YamlGrammarBuilder b = new YamlGrammarBuilder();
        b.setRootRule(ROOT);

        b.rule(ROOT).is(b.object(
            b.mandatoryProperty("asyncapi", b.firstOf("2.0.0", "2.6.0", "3.0.0")),
            b.mandatoryProperty("info", INFO),
            b.property("servers", b.array(SERVERS)),
            b.mandatoryProperty("channels", CHANNELS),
            b.property("components", COMPONENTS),
            b.property("tags", b.array(TAG)),
            b.property("externalDocs", EXTERNAL_DOC),
            b.patternProperty(EXTENSION_PATTERN, b.anything())));

        buildInfo(b);
        buildServers(b);
        buildChannels(b);
        buildComponents(b);
        // Llama aquí a otros métodos build según sea necesario...
        
        return b;
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
          b.patternProperty(EXTENSION_PATTERN, b.anything())));
    
        b.rule(LICENSE).is(b.object(
          b.mandatoryProperty("name", b.string()),
          b.property("url", b.string()),
          b.patternProperty(EXTENSION_PATTERN, b.anything())));
      }

      private static void buildServers(YamlGrammarBuilder b) {
        b.rule(SERVER).is(b.object(
          b.mandatoryProperty("url", b.string()),
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

      private static void buildChannels(YamlGrammarBuilder b) {
        b.rule(CHANNELS).is(b.object(
            b.patternProperty("^/.*", CHANNEL),
            b.patternProperty(EXTENSION_PATTERN, b.anything())));
        b.rule(CHANNEL).is(b.object(
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
            b.property("tags", b.array(b.string())),
            b.property("externalDocs", EXTERNAL_DOC),
            b.property("message", b.firstOf(REF, MESSAGE)),
            b.property("bindings", OPERATION_BINDINGS),
            b.patternProperty(EXTENSION_PATTERN, b.anything())));
        b.rule(MESSAGE).is(b.object(
            b.property("contentType", b.string()),
            b.property("headers", b.firstOf(REF, HEADERS_SCHEMA)),
            b.property("payload", b.firstOf(REF, PAYLOAD_SCHEMA)),
            b.property("correlationId", b.firstOf(REF, CORRELATION_ID)),
            b.property("schemaFormat", b.string()),
            b.property("name", b.string()),
            b.property("title", b.string()),
            b.property("summary", b.string()),
            b.property("description", DESCRIPTION),
            b.property("tags", b.array(TAG)),
            b.property("externalDocs", EXTERNAL_DOC),
            b.property("examples", b.array(EXAMPLE)),
            b.property("bindings", MESSAGE_BINDINGS),
            b.patternProperty(EXTENSION_PATTERN, b.anything())));
        b.rule(CHANNEL_BINDINGS).is(b.object(
            // Channel bindings can vary based on the protocol (HTTP, WebSocket, AMQP, etc.)
            b.patternProperty("^x-", b.anything()), // Extension for custom bindings
            b.patternProperty(EXTENSION_PATTERN, b.anything())));
        b.rule(OPERATION_BINDINGS).is(b.object(
            // Operation bindings can vary based on the protocol
            b.patternProperty("^x-", b.anything()), // Extension for custom bindings
            b.patternProperty(EXTENSION_PATTERN, b.anything())));
        b.rule(MESSAGE_BINDINGS).is(b.object(
            // Message bindings can vary based on the protocol
            b.patternProperty("^x-", b.anything()), // Extension for custom bindings
            b.patternProperty(EXTENSION_PATTERN, b.anything())));
    }
    
    private static void buildComponents(YamlGrammarBuilder b) {
      b.rule(COMPONENTS).is(b.object(
          b.property("schemas", b.object(
              b.patternProperty(".+", SCHEMA))),
          b.property("messages", b.object(
              b.patternProperty(".+", MESSAGE))),
          b.property("securitySchemes", b.object(
              b.patternProperty(".+", SECURITY_SCHEME))),
          b.property("parameters", b.object(
              b.patternProperty(".+", PARAMETER))),
          b.property("correlationIds", b.object(
              b.patternProperty(".+", CORRELATION_ID))),
          b.property("operationTraits", b.object(
              b.patternProperty(".+", OPERATION_TRAIT))),
          b.property("messageTraits", b.object(
              b.patternProperty(".+", MESSAGE_TRAIT))),
          b.property("serverBindings", b.object(
              b.patternProperty(".+", SERVER_BINDING))),
          b.property("channelBindings", b.object(
              b.patternProperty(".+", CHANNEL_BINDING))),
          b.property("operationBindings", b.object(
              b.patternProperty(".+", OPERATION_BINDING))),
          b.property("messageBindings", b.object(
              b.patternProperty(".+", MESSAGE_BINDING))),
          b.patternProperty(EXTENSION_PATTERN, b.anything())));
  }
  

    // Implementación de otros métodos build...
}