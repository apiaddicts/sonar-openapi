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

import com.sonar.sslr.api.RecognitionException;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationIssue;

public class AsyncApiVisitorContext {
  private final JsonNode rootTree;
  private final AsyncApiFile asyncApiFile;
  private final RecognitionException parsingException;
  private final NoSonarCollector collector = new NoSonarCollector();
  private final List<ValidationIssue> issues;

  public AsyncApiVisitorContext(JsonNode rootTree, List<ValidationIssue> issues, AsyncApiFile asyncApiFile) {
    this(rootTree, asyncApiFile, issues, null);
  }

  public AsyncApiVisitorContext(AsyncApiFile asyncApiFile, RecognitionException parsingException) {
    this(null, asyncApiFile, Collections.emptyList(), parsingException);
  }

  private AsyncApiVisitorContext(@Nullable JsonNode rootTree, AsyncApiFile asyncApiFile, List<ValidationIssue> issues, @Nullable RecognitionException parsingException) {
    this.rootTree = rootTree;
    this.asyncApiFile = asyncApiFile;
    this.issues = issues;
    this.parsingException = parsingException;
    if (rootTree != null) {
      AsyncApiVisitor visitor = new AsyncApiVisitor() {
          @Override
          public void scanFile(AsyncApiVisitorContext context) {
              super.scanFile(context);
          }
      };
      visitor.scanFile(this);
    }
  }

  public JsonNode rootTree() {
    return rootTree;
  }

  public AsyncApiFile asyncApiFile() {
    return asyncApiFile;
  }

  public RecognitionException parsingException() {
    return parsingException;
  }

  public List<ValidationIssue> getIssues() {
    return issues;
  }

  public boolean isEnabled(String ruleId, JsonNode node) {
    return collector.isEnabled(node.getPointer(), ruleId);
  }

}