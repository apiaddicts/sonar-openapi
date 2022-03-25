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
package org.apiaddicts.apitools.dosonarapi.openapi.parser;

import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.openapi.OpenApiConfiguration;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.YamlParser;

public class OpenApiParser {
  private OpenApiParser() {
    // Hidden utility class constructor
  }

  public static YamlParser createV2(OpenApiConfiguration configuration) {
    return YamlParser.builder().withCharset(configuration.getCharset()).withGrammar(OpenApi2Grammar.create()).withStrictValidation(configuration.isStrict()).build();
  }

  public static YamlParser createV3(OpenApiConfiguration configuration) {
    return YamlParser.builder().withCharset(configuration.getCharset()).withGrammar(OpenApi3Grammar.create()).withStrictValidation(configuration.isStrict()).build();
  }

  public static YamlParser createGeneric(OpenApiConfiguration configuration) {
    return YamlParser.builder().withCharset(configuration.getCharset()).withStrictValidation(configuration.isStrict()).build();
  }
}
