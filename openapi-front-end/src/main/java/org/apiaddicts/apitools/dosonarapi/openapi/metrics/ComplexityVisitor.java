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

import org.apiaddicts.apitools.dosonarapi.api.OpenApiVisitor;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class ComplexityVisitor extends OpenApiVisitor {

  private static final HashSet<AstNodeType> COMPLEXITY_TYPES = Sets.newHashSet(
      OpenApi2Grammar.PATH, OpenApi2Grammar.OPERATION, OpenApi2Grammar.PARAMETER,
      OpenApi2Grammar.SCHEMA, OpenApi2Grammar.HEADER,
      OpenApi3Grammar.PATH, OpenApi3Grammar.OPERATION, OpenApi3Grammar.PARAMETER,
      OpenApi3Grammar.SCHEMA, OpenApi3Grammar.HEADER, OpenApi3Grammar.CALLBACK, OpenApi3Grammar.MEDIA_TYPE
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
