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

import com.sonar.sslr.api.AstNodeType;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import static org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.YamlGrammar.BLOCK_MAPPING;
import static org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.YamlGrammar.FLOW_MAPPING;

/**
 * Collects the rules that are disabled by scanning all the document.
 *
 * <ul>
 *  <li>{@code x-nosonar} disables a rule (or a set of rules) for the whole document (valid on document root only).
 *  <li>{@code x-sonar-disable} disables a rule (or a set of rules) for the current node only.
 *  <li>{@code x-sonar-enable} enables a rule (or a set of rules) for the current node only.
 * </ul>
 *
 * When a rule is both enabled and disabled by {@code x-sonar-[dis|en]able}, it is considered enabled.
 */
public class NoSonarCollector extends OpenApiVisitor {
  private final Set<String> globallyDisabled = new HashSet<>();
  private final Map<String, Set<String>> disabledByPointer = new HashMap<>();
  private final Map<String, Set<String>> enabledByPointer = new HashMap<>();

  public boolean isEnabled(String pointer, String ruleId) {
    Set<String> enabled = enabledByPointer.getOrDefault(pointer, Collections.emptySet());
    Set<String> disabled = disabledByPointer.getOrDefault(pointer, Collections.emptySet());
    if (globallyDisabled.contains(ruleId)) {
      return enabled.contains(ruleId);
    }
    return !disabled.contains(ruleId) || enabled.contains(ruleId);
  }

  @Override
  protected boolean isSubscribed(AstNodeType nodeType) {
    return nodeType instanceof OpenApi2Grammar || nodeType instanceof OpenApi3Grammar || nodeType == BLOCK_MAPPING || nodeType == FLOW_MAPPING || nodeType instanceof AsyncApiGrammar;
  }

  @Override
  protected void visitFile(JsonNode root) {
    JsonNode node = root.get("x-nosonar");
    if (node.isMissing()) {
      return;
    }
    Set<String> disabled = extractRuleIds(node);
    globallyDisabled.addAll(disabled);
  }

  @Override
  protected void visitNode(JsonNode node) {
    visitSonarDisable(node);
    visitSonarEnable(node);
  }

  private void visitSonarDisable(JsonNode parent) {
    JsonNode node = parent.get("x-sonar-disable");
    if (node.isMissing()) {
      return;
    }
    Set<String> disabled = extractRuleIds(node);
    disabledByPointer.put(parent.getPointer(), disabled);
  }

  private void visitSonarEnable(JsonNode parent) {
    JsonNode node = parent.get("x-sonar-enable");
    if (node.isMissing()) {
      return;
    }
    Set<String> enabled = extractRuleIds(node);
    enabledByPointer.put(parent.getPointer(), enabled);
  }

  private static Set<String> extractRuleIds(JsonNode node) {
    Set<String> disabled;
    if (node.isArray()) {
      disabled = node.elements().stream().map(JsonNode::stringValue).collect(Collectors.toSet());
    } else {
      disabled = Collections.singleton(node.stringValue());
    }
    return disabled;
  }
}
