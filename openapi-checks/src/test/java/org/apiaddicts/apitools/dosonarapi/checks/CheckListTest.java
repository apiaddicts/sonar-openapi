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

import java.util.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckListTest {

  @Test
  public void returns_all_check_classes() {
    List<Class<?>> checks = CheckList.getChecks();
    assertThat(checks).isNotEmpty();
    assertThat(checks).contains(
      PathMaskeradingCheck.class,
      MediaTypeCheck.class,
      ParsingErrorCheck.class,
      DefaultResponseCheck.class,
      DefinedResponseCheck.class,
      DeclaredTagCheck.class,
      DocumentedTagCheck.class,
      AtMostOneBodyParameterCheck.class,
      NoUnusedDefinitionCheck.class,
      NoContentIn204Check.class,
      ProvideOpSummaryCheck.class,
      ContactValidEmailCheck.class,
      DescriptionDiffersSummaryCheck.class
    );
  }

  @Test
  public void constants_have_expected_values() {
    assertThat(CheckList.YAML_REPOSITORY_KEY).isEqualTo("openapi-yaml");
    assertThat(CheckList.JSON_REPOSITORY_KEY).isEqualTo("openapi-json");
    assertThat(CheckList.YAML_LANGUAGE).isEqualTo("yaml");
    assertThat(CheckList.JSON_LANGUAGE).isEqualTo("json");
  }
}
