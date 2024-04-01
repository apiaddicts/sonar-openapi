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
import com.sonar.sslr.api.Token;
import java.util.List;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class AsyncApiVisitor {

  private AsyncApiVisitorContext context;

  public final AsyncApiVisitorContext getContext() {
    return context;
  }

  protected boolean isSubscribed(AstNodeType nodeType) {
    return false;
  }

  protected boolean isSkipped(JsonNode node) {
    return false;
  }

  protected void visitFile(JsonNode root) {
  }

  protected void leaveFile(JsonNode node) {
  }

  protected void visitNode(JsonNode node) {
  }

  protected void leaveNode(JsonNode node) {
  }

  protected void visitToken(Token token) {
  }

  public void scanFile(AsyncApiVisitorContext context) {
    this.context = context;
    JsonNode rootNode = context.rootTree();
    if (rootNode != null) {
      visitFile(rootNode);
      scanNode(rootNode);
      leaveFile(rootNode);
    }
  }

  public void scanNode(JsonNode node) {
    boolean isSubscribedType = isSubscribed(node.getType());
    boolean isSkipped = isSkipped(node);

    if (isSubscribedType && !isSkipped) {
      visitNode(node);
    }

    List<JsonNode> children = node.getJsonChildren();
    if (children.isEmpty() && !isSkipped) {
      for (Token token : node.getTokens()) {
        visitToken(token);
      }
    } else {
      for (JsonNode child : children) {
        scanNode(child);
      }
    }

    if (isSubscribedType && !isSkipped) {
      leaveNode(node);
    }
  }

}
