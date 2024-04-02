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
package org.apiaddicts.apitools.dosonarapi.plugin;

import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import org.apiaddicts.apitools.dosonarapi.openapi.metrics.AsyncApiMetrics;

public class AsyncApiPlugin implements Plugin {

  public static final String FILE_SUFFIXES_KEY = "sonar.asyncapi.file.suffixes";
  public static final String ASYNCAPI_CATEGORY = "AsyncAPI";
  // Subcategories
  private static final String GENERAL = "General";

  @Override
  public void define(Context context) {

    context.addExtensions(
      PropertyDefinition.builder(FILE_SUFFIXES_KEY)
        .index(10)
        .name("File Suffixes")
        .description("A list of suffixes of AsyncAPI files to analyze.")
        .category(ASYNCAPI_CATEGORY)
        .subCategory(GENERAL)
        .onQualifiers(Qualifiers.PROJECT)
        .multiValues(true)
        .defaultValue("yaml,yml,json")
        .build(),
      AsyncApi.class,
      AsyncApiProfileDefinition.class,
      AsyncApiScannerSensor.class,
      AsyncApiRulesDefinition.class, 
      AsyncApiMetrics.class 
    );
  }
}
