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
package org.apiaddicts.apitools.dosonarapi.api;

import java.io.File;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NoSonarCollectorTest {

  @Test
  public void can_disable_rules() {
    NoSonarCollector collector = new NoSonarCollector();

    TestOpenApiVisitorRunner.scanFile(new File(NoSonarCollectorTest.class.getResource("/nosonar-test.yaml").getFile()), collector);

    // Globally disable rules
    assertThat(collector.isEnabled("/info/license", "RuleId1")).isFalse();

    // Rules enabled when not quoted
    assertThat(collector.isEnabled("/info/license", "RuleId3")).isTrue();

    // Can re-enable locally
    assertThat(collector.isEnabled("/paths/~1pets/get", "RuleId1")).isTrue();

    // Can disable locally
    assertThat(collector.isEnabled("/paths/~1pets/get", "RuleId3")).isTrue();
    assertThat(collector.isEnabled("/paths/~1pets/get/parameters/1", "RuleId3")).isFalse();
  }
}
