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

import com.sonar.sslr.api.RecognitionException;
import java.io.File;

import org.apiaddicts.apitools.dosonarapi.api.OpenApiFile;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiVisitorContext;
import org.apiaddicts.apitools.dosonarapi.api.TestOpenApiVisitorRunner;

import static org.assertj.core.api.Assertions.assertThat;


public class FileMetricsTest {
  @Test
  public void operations() {
    assertThat(metrics("operations.yaml").numberOfOperations()).isEqualTo(2);
  }

  @Test
  public void paths() {
    assertThat(metrics("paths.yaml").numberOfPaths()).isEqualTo(1);
  }

  @Test
  public void schemas() {
    assertThat(metrics("schemas.yaml").numberOfSchemas()).isEqualTo(2);
  }

  @Test
  public void complexity() {
    assertThat(metrics("complexity.yaml").complexity()).isEqualTo(7);
  }

  @Test
  public void null_root_tree_yields_zero_counts() {
    OpenApiFile file = new OpenApiFile() {
      @Override public String content() { return ""; }
      @Override public String fileName() { return "dummy.yaml"; }
    };
    OpenApiVisitorContext context = new OpenApiVisitorContext(file, new RecognitionException(0, "parse error"));
    FileMetrics fileMetrics = new FileMetrics(context);

    assertThat(fileMetrics.numberOfOperations()).isEqualTo(0);
    assertThat(fileMetrics.numberOfPaths()).isEqualTo(0);
    assertThat(fileMetrics.numberOfSchemas()).isEqualTo(0);
    assertThat(fileMetrics.complexity()).isEqualTo(0);
  }

  private FileMetrics metrics(String fileName) {
    File baseDir = new File("src/test/resources/metrics/");
    File file = new File(baseDir, fileName);
    return new FileMetrics(TestOpenApiVisitorRunner.createContext(file));
  }
}
