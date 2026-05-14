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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiFile;
import org.apiaddicts.apitools.dosonarapi.checks.CheckList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SonarQubeOpenApiFileTest {

  private static final File BASE_DIR = Paths.get("src/test/resources/sensor").toAbsolutePath().toFile();

  @Test
  public void returns_filename() {
    InputFile inputFile = TestInputFileBuilder.create("moduleKey", "file1.yaml")
      .setModuleBaseDir(BASE_DIR.toPath())
      .setCharset(StandardCharsets.UTF_8)
      .setType(InputFile.Type.MAIN)
      .setLanguage(CheckList.YAML_LANGUAGE)
      .initMetadata(TestUtils.fileContent(new File(BASE_DIR, "file1.yaml"), StandardCharsets.UTF_8))
      .build();

    OpenApiFile openApiFile = SonarQubeOpenApiFile.create(inputFile);
    assertThat(openApiFile.fileName()).isEqualTo("file1.yaml");
  }

  @Test
  public void returns_content() {
    InputFile inputFile = TestInputFileBuilder.create("moduleKey", "file1.yaml")
      .setModuleBaseDir(BASE_DIR.toPath())
      .setCharset(StandardCharsets.UTF_8)
      .setType(InputFile.Type.MAIN)
      .setLanguage(CheckList.YAML_LANGUAGE)
      .initMetadata(TestUtils.fileContent(new File(BASE_DIR, "file1.yaml"), StandardCharsets.UTF_8))
      .build();

    OpenApiFile openApiFile = SonarQubeOpenApiFile.create(inputFile);
    assertThat(openApiFile.content()).isNotEmpty();
  }

  @Test
  public void throws_illegal_state_on_io_exception() throws IOException {
    InputFile inputFile = mock(InputFile.class);
    when(inputFile.filename()).thenReturn("test.yaml");
    when(inputFile.contents()).thenThrow(new IOException("read error"));

    OpenApiFile openApiFile = SonarQubeOpenApiFile.create(inputFile);
    assertThatThrownBy(openApiFile::content)
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("Could not read content");
  }
}
