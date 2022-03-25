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
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import static java.util.Arrays.asList;

/**
 * Provides metrics definitions to the SonarQube engine.
 */
public class OpenApiMetrics implements Metrics {

  public static final Metric<Integer> OPERATIONS_COUNT = new Metric.Builder("operations_count", "Operations Count", Metric.ValueType.INT)
      .setDescription("Number of operations in the contract")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(false)
      .setDomain(CoreMetrics.DOMAIN_SIZE)
      .create();

  public static final Metric<Integer> PATHS_COUNT = new Metric.Builder("paths_count", "Paths Count", Metric.ValueType.INT)
      .setDescription("Number of paths in the contract")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(false)
      .setDomain(CoreMetrics.DOMAIN_SIZE)
      .create();

  public static final Metric<Integer> SCHEMAS_COUNT = new Metric.Builder("schemas_count", "Schemas Count", Metric.ValueType.INT)
      .setDescription("Number of schemas in the contract")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(false)
      .setDomain(CoreMetrics.DOMAIN_SIZE)
      .create();

  @Override
  public List<Metric> getMetrics() {
    return asList(OPERATIONS_COUNT, PATHS_COUNT, SCHEMAS_COUNT);
  }}
