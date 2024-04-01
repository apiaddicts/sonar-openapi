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
 * Provides metrics definitions to the SonarQube engine for AsyncAPI documents.
 */
public class AsyncApiMetrics implements Metrics {

  public static final Metric<Integer> CHANNELS_COUNT = new Metric.Builder("channels_count", "Channels Count", Metric.ValueType.INT)
      .setDescription("Number of channels in the AsyncAPI document")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(false)
      .setDomain(CoreMetrics.DOMAIN_SIZE)
      .create();

  public static final Metric<Integer> MESSAGES_COUNT = new Metric.Builder("messages_count", "Messages Count", Metric.ValueType.INT)
      .setDescription("Number of messages in the AsyncAPI document")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(false)
      .setDomain(CoreMetrics.DOMAIN_SIZE)
      .create();

  public static final Metric<Integer> COMPONENTS_COUNT = new Metric.Builder("components_count", "Components Count", Metric.ValueType.INT)
      .setDescription("Number of components in the AsyncAPI document")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(false)
      .setDomain(CoreMetrics.DOMAIN_SIZE)
      .create();

  @Override
  public List<Metric> getMetrics() {
    return asList(CHANNELS_COUNT, MESSAGES_COUNT, COMPONENTS_COUNT);
  }
}
