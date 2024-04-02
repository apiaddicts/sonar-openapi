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
import org.apiaddicts.apitools.dosonarapi.checks.AsyncApiCheckList;
import org.apiaddicts.apitools.dosonarapi.api.AsyncApiCustomRuleRepository;

public class AsyncApiScannerSensor implements Sensor {
  private static final Logger LOGGER = Loggers.get(AsyncApiScannerSensor.class);
  private final AsyncApiChecks checks;
  private FileLinesContextFactory fileLinesContextFactory;
  private final NoSonarFilter noSonarFilter;

  public AsyncApiScannerSensor(CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory, NoSonarFilter noSonarFilter) {
    this(checkFactory, fileLinesContextFactory, noSonarFilter, null);
  }

  public AsyncApiScannerSensor(CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory, NoSonarFilter noSonarFilter, @Nullable AsyncApiCustomRuleRepository[] customRuleRepositories) {
    this.checks = AsyncApiChecks.createAsyncApiCheck(checkFactory)
      .addChecks(AsyncApiCheckList.REPOSITORY_KEY, AsyncApiCheckList.getChecks())
      .addCustomChecks(customRuleRepositories);
    this.fileLinesContextFactory = fileLinesContextFactory;
    this.noSonarFilter = noSonarFilter;
  }

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name("AsyncAPI Scanner Sensor")
      .onlyOnFileType(InputFile.Type.MAIN)
      .onlyOnLanguage(AsyncApi.KEY);
  }

  @Override
  public void execute(SensorContext context) {
    FilePredicates p = context.fileSystem().predicates();
    Iterable<InputFile> it = context.fileSystem().inputFiles(
      p.and(p.hasType(InputFile.Type.MAIN),
        p.hasLanguage(AsyncApi.KEY)));
    List<InputFile> list = new ArrayList<>();
    it.forEach(list::add);
    List<InputFile> inputFiles = Collections.unmodifiableList(list);

    if (!inputFiles.isEmpty()) {
      AsyncApiAnalyzer scanner = new AsyncApiAnalyzer(context, checks, fileLinesContextFactory, noSonarFilter, inputFiles);
      LOGGER.info("AsyncAPI Scanner called for the following files: {}.", inputFiles);
      scanner.scanFiles();
    }
  }
}
