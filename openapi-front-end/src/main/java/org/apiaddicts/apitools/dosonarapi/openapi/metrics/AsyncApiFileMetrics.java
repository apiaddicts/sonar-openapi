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

import org.apiaddicts.apitools.dosonarapi.api.AsyncApiVisitorContext;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import com.sonar.sslr.api.AstNode;

public class AsyncApiFileMetrics {

  private int numberOfMessages;
  private int numberOfChannels;
  private int numberOfOperations;

  private final AsyncApiComplexityVisitor complexityVisitor = new AsyncApiComplexityVisitor();
  private final AsyncApiFileLinesVisitor fileLinesVisitor;

  public AsyncApiFileMetrics(AsyncApiVisitorContext context) {
    countObjects(context);
    complexityVisitor.scanFile(context);
    fileLinesVisitor = new AsyncApiFileLinesVisitor();
    fileLinesVisitor.scanFile(context);
  }

  private void countObjects(AsyncApiVisitorContext context) {
    JsonNode rootTree = context.rootTree();
    if (rootTree != null) {
      numberOfMessages = (int) rootTree.getDescendants(AsyncApiGrammar.MESSAGE, AsyncApiGrammar.MESSAGES)
          .stream().filter(AsyncApiFileMetrics::isNotRef)
          .count();
      numberOfChannels = (int) rootTree.getDescendants(AsyncApiGrammar.CHANNEL, AsyncApiGrammar.CHANNELS)
          .stream().filter(AsyncApiFileMetrics::isNotRef)
          .count();
      numberOfOperations = rootTree.getDescendants(AsyncApiGrammar.OPERATION).size();
    } else {
      numberOfMessages = 0;
      numberOfChannels = 0;
      numberOfOperations = 0;
    }
  }

  public int numberOfOperations() {
    return numberOfOperations;
  }

  public int numberOfChannels() {
    return numberOfChannels;
  }

  public int numberOfMessages() {
    return numberOfMessages;
  }

  public int complexity() {
    return complexityVisitor.getComplexity();
  }

  public AsyncApiFileLinesVisitor fileLinesVisitor() {
    return fileLinesVisitor;
  }

  private static boolean isNotRef(AstNode node) {
    return !((JsonNode)node).isRef();
  }

}