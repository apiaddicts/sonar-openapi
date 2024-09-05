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
  private final OpenApiChecks checks;
  private FileLinesContextFactory fileLinesContextFactory;
  private final NoSonarFilter noSonarFilter;

  public OpenApiScannerSensor(CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory, NoSonarFilter noSonarFilter) {
    this(checkFactory, fileLinesContextFactory, noSonarFilter, null);
  }

  public OpenApiScannerSensor(CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory, NoSonarFilter noSonarFilter, @Nullable OpenApiCustomRuleRepository[] customRuleRepositories) {
    this.checks = OpenApiChecks.createOpenApiCheck(checkFactory)
      .addChecks(CheckList.REPOSITORY_KEY, CheckList.getChecks())
      .addCustomChecks(customRuleRepositories);
    this.fileLinesContextFactory = fileLinesContextFactory;
    this.noSonarFilter = noSonarFilter;
  }

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name("OpenAPI Scanner Sensor")
      .onlyOnFileType(InputFile.Type.MAIN)
      .onlyOnLanguage(OpenApi.KEY);
  }

  @Override
  public void execute(SensorContext context) {
    FilePredicates p = context.fileSystem().predicates();
    scanFiles(context, p);
  }

  public void scanFiles(SensorContext context, FilePredicates p) {
    Iterable<InputFile> it = context.fileSystem().inputFiles(
      p.and(p.hasType(InputFile.Type.MAIN),
        p.hasLanguage(OpenApi.KEY)
        ));
    List<InputFile> list = new ArrayList<>();
    it.forEach(list::add);
    List<InputFile> inputFiles = Collections.unmodifiableList(list);

    if (!inputFiles.isEmpty()) {
      OpenApiAnalyzer scanner = new OpenApiAnalyzer(context, checks, fileLinesContextFactory, noSonarFilter, inputFiles/*, isV2*/);
      LOGGER.info("OpenAPI Scanner called for the following files: {}.", inputFiles);
      scanner.scanFiles();
    }
  }
}
