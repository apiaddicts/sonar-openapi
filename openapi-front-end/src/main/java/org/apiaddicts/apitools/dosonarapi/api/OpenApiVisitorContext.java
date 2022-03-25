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

public class OpenApiVisitorContext {
  private final JsonNode rootTree;
  private final OpenApiFile openApiFile;
  private final RecognitionException parsingException;
  private final NoSonarCollector collector = new NoSonarCollector();
  private final List<ValidationIssue> issues;

  public OpenApiVisitorContext(JsonNode rootTree, List<ValidationIssue> issues, OpenApiFile openApiFile) {
    this(rootTree, openApiFile, issues, null);
  }

  public OpenApiVisitorContext(OpenApiFile openApiFile, RecognitionException parsingException) {
    this(null, openApiFile, Collections.emptyList(), parsingException);
  }

  private OpenApiVisitorContext(@Nullable JsonNode rootTree, OpenApiFile openApiFile, List<ValidationIssue> issues, @Nullable RecognitionException parsingException) {
    this.rootTree = rootTree;
    this.openApiFile = openApiFile;
    this.issues = issues;
    this.parsingException = parsingException;
    if (rootTree != null) {
      this.collector.scanFile(this);
    }
  }

  public JsonNode rootTree() {
    return rootTree;
  }

  public OpenApiFile openApiFile() {
    return openApiFile;
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
