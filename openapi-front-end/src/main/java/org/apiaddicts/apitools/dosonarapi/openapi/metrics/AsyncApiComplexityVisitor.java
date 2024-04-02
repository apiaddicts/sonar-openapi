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
package org.apiaddicts.apitools.dosonarapi.openapi.metrics;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import java.util.HashSet;

import org.apiaddicts.apitools.dosonarapi.api.AsyncApiVisitor;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class AsyncApiComplexityVisitor extends AsyncApiVisitor {

  private static final HashSet<AstNodeType> COMPLEXITY_TYPES = Sets.newHashSet(
      AsyncApiGrammar.CHANNEL, AsyncApiGrammar.OPERATION, AsyncApiGrammar.MESSAGE,
      AsyncApiGrammar.COMPONENTS, AsyncApiGrammar.PARAMETER,
      AsyncApiGrammar.MESSAGE_TRAIT, AsyncApiGrammar.OPERATION_TRAIT
  );
  private int complexity;

  @Override
  protected boolean isSubscribed(AstNodeType nodeType) {
    return COMPLEXITY_TYPES.contains(nodeType);
  }

  @Override
  public void visitFile(JsonNode node) {
    complexity = 0;
  }

  @Override
  public void visitNode(JsonNode node) {
    if (!node.isRef()) {
      complexity++;
    }
  }

  public int getComplexity() {
    return complexity;
  }

}
