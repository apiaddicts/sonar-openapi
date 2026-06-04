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

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.apiaddicts.apitools.dosonarapi.checks.CheckList;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenApiRulesDefinitionTest {

  @Test
  public void defines_yaml_json_and_openapi_repositories() {
    OpenApiRulesDefinition definition = new OpenApiRulesDefinition();
    RulesDefinition.Context context = new RulesDefinition.Context();

    definition.define(context);

    RulesDefinition.Repository yamlRepo = context.repository(CheckList.YAML_REPOSITORY_KEY);
    assertThat(yamlRepo).isNotNull();
    assertThat(yamlRepo.language()).isEqualTo(CheckList.YAML_LANGUAGE);
    assertThat(yamlRepo.rules()).hasSizeGreaterThanOrEqualTo(1);

    RulesDefinition.Repository jsonRepo = context.repository(CheckList.JSON_REPOSITORY_KEY);
    assertThat(jsonRepo).isNotNull();
    assertThat(jsonRepo.language()).isEqualTo(CheckList.JSON_LANGUAGE);
    assertThat(jsonRepo.rules()).hasSizeGreaterThanOrEqualTo(1);

    RulesDefinition.Repository openapiRepo = context.repository(CheckList.OPENAPI_REPOSITORY_KEY);
    assertThat(openapiRepo).isNotNull();
    assertThat(openapiRepo.language()).isEqualTo(CheckList.OPENAPI_LANGUAGE);
    assertThat(openapiRepo.rules()).hasSizeGreaterThanOrEqualTo(1);
  }

  @Test
  public void repository_key_returns_yaml_key() {
    OpenApiRulesDefinition definition = new OpenApiRulesDefinition();
    assertThat(definition.repositoryKey()).isEqualTo(CheckList.YAML_REPOSITORY_KEY);
  }

  @Test
  public void check_classes_returns_all_checks() {
    OpenApiRulesDefinition definition = new OpenApiRulesDefinition();
    assertThat(definition.checkClasses()).isEqualTo(CheckList.getChecks());
  }

  @Test
  public void all_repos_have_same_rules() {
    OpenApiRulesDefinition definition = new OpenApiRulesDefinition();
    RulesDefinition.Context context = new RulesDefinition.Context();
    definition.define(context);

    RulesDefinition.Repository yamlRepo = context.repository(CheckList.YAML_REPOSITORY_KEY);
    RulesDefinition.Repository jsonRepo = context.repository(CheckList.JSON_REPOSITORY_KEY);
    RulesDefinition.Repository openapiRepo = context.repository(CheckList.OPENAPI_REPOSITORY_KEY);
    assertThat(yamlRepo.rules()).hasSameSizeAs(jsonRepo.rules());
    assertThat(yamlRepo.rules()).hasSameSizeAs(openapiRepo.rules());
  }
}
