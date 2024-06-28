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
package org.apiaddicts.apitools.dosonarapi.checks;

import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.OpenApiCheckVerifier;

public class DescriptionDiffersSummaryCheckTest {
  @Test
  public void verify_in_v2() {
    OpenApiCheckVerifier.verify("src/test/resources/checks/v2/different-description.yaml", new DescriptionDiffersSummaryCheck(), true, false, false);
  }

  @Test
  public void verify_in_v3() {
    OpenApiCheckVerifier.verify("src/test/resources/checks/v3/different-description.yaml", new DescriptionDiffersSummaryCheck(), false, true, false);
  }
}
