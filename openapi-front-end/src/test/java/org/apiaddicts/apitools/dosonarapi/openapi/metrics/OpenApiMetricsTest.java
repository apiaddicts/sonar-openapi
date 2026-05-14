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

import java.util.List;
import org.junit.Test;
import org.sonar.api.measures.Metric;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenApiMetricsTest {

  @Test
  public void provides_three_metrics() {
    OpenApiMetrics openApiMetrics = new OpenApiMetrics();
    List<Metric> metrics = openApiMetrics.getMetrics();
    assertThat(metrics).hasSize(3);
    assertThat(metrics).contains(OpenApiMetrics.OPERATIONS_COUNT, OpenApiMetrics.PATHS_COUNT, OpenApiMetrics.SCHEMAS_COUNT);
  }

  @Test
  public void operations_count_metric_has_correct_key() {
    assertThat(OpenApiMetrics.OPERATIONS_COUNT.getKey()).isEqualTo("operations_count");
    assertThat(OpenApiMetrics.OPERATIONS_COUNT.getName()).isEqualTo("Operations Count");
  }

  @Test
  public void paths_count_metric_has_correct_key() {
    assertThat(OpenApiMetrics.PATHS_COUNT.getKey()).isEqualTo("paths_count");
    assertThat(OpenApiMetrics.PATHS_COUNT.getName()).isEqualTo("Paths Count");
  }

  @Test
  public void schemas_count_metric_has_correct_key() {
    assertThat(OpenApiMetrics.SCHEMAS_COUNT.getKey()).isEqualTo("schemas_count");
    assertThat(OpenApiMetrics.SCHEMAS_COUNT.getName()).isEqualTo("Schemas Count");
  }
}
