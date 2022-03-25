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

import com.sonar.sslr.api.RecognitionException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apiaddicts.apitools.dosonarapi.openapi.OpenApiConfiguration;
import org.apiaddicts.apitools.dosonarapi.plugin.cpd.OpenApiCpdAnalyzer;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Metric;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.apiaddicts.apitools.dosonarapi.openapi.metrics.FileLinesVisitor;
import org.apiaddicts.apitools.dosonarapi.openapi.metrics.FileMetrics;
import org.apiaddicts.apitools.dosonarapi.openapi.metrics.OpenApiMetrics;
import org.apiaddicts.apitools.dosonarapi.openapi.parser.OpenApiParser;
import org.apiaddicts.apitools.dosonarapi.api.IssueLocation;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiCheck;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiFile;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiVisitorContext;
import org.apiaddicts.apitools.dosonarapi.api.PreciseIssue;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.YamlParser;

public class OpenApiAnalyzer {
  private static final Logger LOG = Loggers.get(OpenApiAnalyzer.class);

  private final SensorContext context;
  private final List<InputFile> inputFiles;
  private final OpenApiChecks checks;
  private final YamlParser v2parser;
  private final YamlParser v3parser;
  private final YamlParser genericParser;
  private final NoSonarFilter noSonarFilter;
  private final OpenApiCpdAnalyzer cpdAnalyzer;
  private FileLinesContextFactory fileLinesContextFactory;

  public OpenApiAnalyzer(SensorContext context, OpenApiChecks checks, FileLinesContextFactory fileLinesContextFactory, NoSonarFilter noSonarFilter, List<InputFile> inputFiles/*, boolean isv2*/) {
    this.context = context;
    this.checks = checks;
    this.fileLinesContextFactory = fileLinesContextFactory;
    this.noSonarFilter = noSonarFilter;
    this.cpdAnalyzer = new OpenApiCpdAnalyzer(context);
    this.inputFiles = inputFiles;
    OpenApiConfiguration configuration = new OpenApiConfiguration(context.fileSystem().encoding(), true);
    this.v2parser = OpenApiParser.createV2(configuration);
    this.v3parser = OpenApiParser.createV3(configuration);
    this.genericParser = OpenApiParser.createGeneric(configuration);
  }

  private static NewIssueLocation newLocation(InputFile inputFile, NewIssue issue, IssueLocation location) {
    NewIssueLocation newLocation = issue.newLocation().on(inputFile);
    if (location.startLine() != IssueLocation.UNDEFINED_LINE) {
      TextRange range;
      if (location.startLineOffset() == IssueLocation.UNDEFINED_OFFSET) {
        range = inputFile.selectLine(location.startLine());
      } else {
        range = inputFile.newRange(location.startLine(), location.startLineOffset(), location.endLine(), location.endLineOffset());
      }
      newLocation.at(range);
    }

    String message = location.message();
    if (message != null) {
      newLocation.message(message);
    }
    return newLocation;
  }

  public void scanFiles() {
    for (InputFile openApiFile : inputFiles) {
      if (context.isCancelled()) {
        return;
      }
      scanFile(openApiFile);
    }
  }

  private void scanFile(InputFile inputFile) {
    OpenApiFile openApiFile = SonarQubeOpenApiFile.create(inputFile);
    OpenApiVisitorContext visitorContext;
    try {
      String content = getContent(inputFile);

      JsonNode rootNode = genericParser.parse(content);
      boolean isV2 = !rootNode.at("/swagger").isMissing();
      boolean isV3 = !rootNode.at("/openapi").isMissing();
      YamlParser targetParser = null;
      if (isV2) targetParser = v2parser;
      if (isV3) targetParser = v3parser;
      if (targetParser == null) return;

      visitorContext = new OpenApiVisitorContext(targetParser.parse(content), targetParser.getIssues(), openApiFile);
      saveMeasures(inputFile, visitorContext);
    } catch (ValidationException e) {
      visitorContext = new OpenApiVisitorContext(openApiFile, e);
      LOG.error("Error during file validation: " + inputFile.filename() + "\"\n" + e.formatMessage());
      for (ValidationException cause : e.getCauses()) {
        dumpException(cause, inputFile);
      }

    } catch (RecognitionException e) {
      visitorContext = new OpenApiVisitorContext(openApiFile, e);
      LOG.error("Unable to parse file: " + inputFile.filename() + "\"\n" + e.getMessage());
      dumpException(e, inputFile);

    } catch (IOException ex) {
      RecognitionException re = new RecognitionException(0, ex.getMessage());
      visitorContext = new OpenApiVisitorContext(openApiFile, re);
      LOG.error("Unable to parse file: " + inputFile.filename() + "\"\n" + ex.getMessage());
    }

    for (OpenApiCheck check : checks.all()) {
      saveIssues(inputFile, check, check.scanFileForIssues(visitorContext));
    }
  }

  /**
   * This method is required to avoid a parsing issue with yaml,
   * sometimes, when an empty line is followed by a comment, it breaks the parser
   *
   * FIXME: Try to solve in the yaml parser lib
   */
  private String getContent(InputFile inputFile) throws IOException {
    String [] lines = inputFile.contents().split("\n");
    for (int i = 1; i < lines.length; i++) {
      if (!lines[i].trim().isEmpty()) continue;
      int n = lines[i-1].indexOf(lines[i-1].trim());
      if (n < 0) n = 0;
      lines[i] = String.join("", Collections.nCopies(n, " ")) + "#";
    }
    return String.join("\n", lines);
  }

  private void dumpException(RecognitionException e, InputFile inputFile) {
    int line = e.getLine();
    if (line == 0) {
      line = 1;
    }
    int column = 0;
    if (e instanceof ValidationException) {
      column = ((ValidationException) e).getNode().getToken().getColumn();
      for (ValidationException cause : ((ValidationException) e).getCauses()) {
        dumpException(cause, inputFile);
      }
    }
    context.newAnalysisError()
        .onFile(inputFile)
        .at(inputFile.newPointer(line, column))
        .message(e.getMessage())
        .save();
  }

  private void saveIssues(InputFile inputFile, OpenApiCheck check, List<PreciseIssue> issues) {
    RuleKey ruleKey = checks.ruleKeyFor(check);
    for (PreciseIssue preciseIssue : issues) {

      NewIssue newIssue = context
        .newIssue()
        .forRule(ruleKey);

      Integer cost = preciseIssue.cost();
      if (cost != null) {
        newIssue.gap(cost.doubleValue());
      }

      newIssue.at(newLocation(inputFile, newIssue, preciseIssue.primaryLocation()));

      for (IssueLocation secondaryLocation : preciseIssue.secondaryLocations()) {
        newIssue.addLocation(newLocation(inputFile, newIssue, secondaryLocation));
      }

      newIssue.save();
    }
  }

  private void saveMeasures(InputFile inputFile, OpenApiVisitorContext visitorContext) {
    FileMetrics fileMetrics = new FileMetrics(visitorContext);
    FileLinesVisitor fileLinesVisitor = fileMetrics.fileLinesVisitor();

    cpdAnalyzer.pushCpdTokens(inputFile, visitorContext);
    noSonarFilter.noSonarInFile(inputFile, fileLinesVisitor.getLinesWithNoSonar());

    Set<Integer> linesOfCode = fileLinesVisitor.getLinesOfCode();
    Set<Integer> linesOfComments = fileLinesVisitor.getLinesOfComments();

    saveMetricOnFile(inputFile, CoreMetrics.NCLOC, linesOfCode.size());
    saveMetricOnFile(inputFile, CoreMetrics.COMMENT_LINES, linesOfComments.size());

    saveMetricOnFile(inputFile, OpenApiMetrics.SCHEMAS_COUNT, fileMetrics.numberOfSchemas());
    saveMetricOnFile(inputFile, OpenApiMetrics.OPERATIONS_COUNT, fileMetrics.numberOfOperations());
    saveMetricOnFile(inputFile, OpenApiMetrics.PATHS_COUNT, fileMetrics.numberOfPaths());

    saveMetricOnFile(inputFile, CoreMetrics.COMPLEXITY, fileMetrics.complexity());

    FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(inputFile);
    for (int line : linesOfCode) {
      fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, line, 1);
    }
    for (int line : linesOfComments) {
      fileLinesContext.setIntValue(CoreMetrics.COMMENT_LINES_DATA_KEY, line, 1);
    }
    fileLinesContext.save();
  }

  private void saveMetricOnFile(InputFile inputFile, Metric<Integer> metric, Integer value) {
    context.<Integer>newMeasure()
        .withValue(value)
        .forMetric(metric)
        .on(inputFile)
        .save();
  }
}
