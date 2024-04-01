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
package org.apiaddicts.apitools.dosonarapi.plugin.cpd;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.TokenType;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.apiaddicts.apitools.dosonarapi.api.IssueLocation;
import org.apiaddicts.apitools.dosonarapi.api.AsyncApiVisitorContext;

public class AsyncApiCpdAnalyzer {

  private final SensorContext context;

  public AsyncApiCpdAnalyzer(SensorContext context) {
    this.context = context;
  }

  public void pushCpdTokens(InputFile inputFile, AsyncApiVisitorContext visitorContext) {
    AstNode root = visitorContext.rootTree();
    if (root != null) {
      NewCpdTokens cpdTokens = context.newCpdTokens().onFile(inputFile);
      for (Token token : root.getTokens()) {
        if (!isIgnoredType(token.getType())) {
          IssueLocation.TokenLocation location = new IssueLocation.TokenLocation(token);
          if (location.startLine() < location.endLine() || location.startLineOffset() < location.endLineOffset()) {
            cpdTokens.addToken(location.startLine(), location.startLineOffset(), location.endLine(), location.endLineOffset(), getImage(token));
          }
        }
      }
      cpdTokens.save();
    }
  }

  private boolean isIgnoredType(TokenType type) {
    return type.equals(GenericTokenType.EOF);
  }

  private String getImage(Token token) {
    return token.getValue();
  }

}

