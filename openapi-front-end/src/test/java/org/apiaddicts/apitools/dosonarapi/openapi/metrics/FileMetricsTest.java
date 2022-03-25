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

import java.io.File;

import org.apiaddicts.apitools.dosonarapi.openapi.metrics.FileMetrics;
import org.junit.Test;
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

  private FileMetrics metrics(String fileName) {
    File baseDir = new File("src/test/resources/metrics/");
    File file = new File(baseDir, fileName);
    return new FileMetrics(TestOpenApiVisitorRunner.createContext(file));
  }
}
