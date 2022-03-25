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

import com.google.common.base.Charsets;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apiaddicts.apitools.dosonarapi.plugin.OpenApi;
import org.apiaddicts.apitools.dosonarapi.plugin.TestUtils;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.duplications.internal.pmd.TokensLine;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiVisitorContext;
import org.apiaddicts.apitools.dosonarapi.api.TestOpenApiVisitorRunner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class OpenApiCpdAnalyzerTest {

  private static final String BASE_DIR = "src/test/resources/sensor";
  private SensorContextTester context = SensorContextTester.create(new File(BASE_DIR));
  private OpenApiCpdAnalyzer cpdAnalyzer = new OpenApiCpdAnalyzer(context);

  @Test
  public void can_collect_tokens() {
    DefaultInputFile inputFile = inputFile("cpd.yaml");
    OpenApiVisitorContext visitorContext = TestOpenApiVisitorRunner.createContext(inputFile.path().toFile());
    cpdAnalyzer.pushCpdTokens(inputFile, visitorContext);

    List<TokensLine> lines = context.cpdTokens("moduleKey:cpd.yaml");
    assertThat(lines).hasSize(6);
    TokensLine line1 = lines.get(0);
    assertThat(line1.getStartLine()).isEqualTo(1);
    assertThat(line1.getEndLine()).isEqualTo(1);
    assertThat(line1.getStartUnit()).isEqualTo(1);
    assertThat(line1.getEndUnit()).isEqualTo(3);
    List<String> values = lines.stream().map(TokensLine::getValue).collect(Collectors.toList());
    assertThat(values).containsExactly(
        "openapi:3.0.1",
        "info:",
        "version:1.0.0",
        "title:Swagger Petstore",
        "paths:",
        "/pets:{}");
  }

  private DefaultInputFile inputFile(String fileName) {
    File file = new File(BASE_DIR, fileName);

    DefaultInputFile inputFile = TestInputFileBuilder.create("moduleKey", file.getName())
        .setModuleBaseDir(Paths.get(BASE_DIR))
        .setCharset(UTF_8)
        .setType(InputFile.Type.MAIN)
        .setLanguage(OpenApi.KEY)
        .initMetadata(TestUtils.fileContent(file, Charsets.UTF_8))
        .build();

    context.fileSystem().add(inputFile);

    return inputFile;
  }
}
