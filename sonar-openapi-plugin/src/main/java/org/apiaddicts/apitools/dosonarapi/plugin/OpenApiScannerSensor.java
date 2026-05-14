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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.apiaddicts.apitools.dosonarapi.checks.CheckList;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiCustomRuleRepository;

public class OpenApiScannerSensor implements Sensor {
  private static final Logger LOGGER = Loggers.get(OpenApiScannerSensor.class);
  private final OpenApiChecks yamlChecks;
  private final OpenApiChecks jsonChecks;
  private FileLinesContextFactory fileLinesContextFactory;
  private final NoSonarFilter noSonarFilter;

  public OpenApiScannerSensor(CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory, NoSonarFilter noSonarFilter) {
    this(checkFactory, fileLinesContextFactory, noSonarFilter, null);
  }

  public OpenApiScannerSensor(CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory, NoSonarFilter noSonarFilter, @Nullable OpenApiCustomRuleRepository[] customRuleRepositories) {
    this.yamlChecks = OpenApiChecks.createOpenApiCheck(checkFactory)
      .addChecks(CheckList.YAML_REPOSITORY_KEY, CheckList.getChecks())
      .addCustomYamlChecks(customRuleRepositories);
    this.jsonChecks = OpenApiChecks.createOpenApiCheck(checkFactory)
      .addChecks(CheckList.JSON_REPOSITORY_KEY, CheckList.getChecks())
      .addCustomJsonChecks(customRuleRepositories);
    this.fileLinesContextFactory = fileLinesContextFactory;
    this.noSonarFilter = noSonarFilter;
  }

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name("OpenAPI Scanner Sensor")
      .onlyOnFileType(InputFile.Type.MAIN)
      .onlyOnLanguages(CheckList.YAML_LANGUAGE, CheckList.JSON_LANGUAGE);
  }

  @Override
  public void execute(SensorContext context) {
    FilePredicates p = context.fileSystem().predicates();
    scanFiles(context, p);
  }

  public void scanFiles(SensorContext context, FilePredicates p) {
    List<InputFile> yamlFiles = new ArrayList<>();
    context.fileSystem().inputFiles(
      p.and(p.hasType(InputFile.Type.MAIN), p.hasLanguage(CheckList.YAML_LANGUAGE))
    ).forEach(yamlFiles::add);

    List<InputFile> jsonFiles = new ArrayList<>();
    context.fileSystem().inputFiles(
      p.and(p.hasType(InputFile.Type.MAIN), p.hasLanguage(CheckList.JSON_LANGUAGE))
    ).forEach(jsonFiles::add);

    if (!yamlFiles.isEmpty()) {
      LOGGER.info("OpenAPI Scanner called for yaml files: {}.", yamlFiles);
      new OpenApiAnalyzer(context, yamlChecks, fileLinesContextFactory, noSonarFilter, Collections.unmodifiableList(yamlFiles)).scanFiles();
    }
    if (!jsonFiles.isEmpty()) {
      LOGGER.info("OpenAPI Scanner called for json files: {}.", jsonFiles);
      new OpenApiAnalyzer(context, jsonChecks, fileLinesContextFactory, noSonarFilter, Collections.unmodifiableList(jsonFiles)).scanFiles();
    }
  }
}
