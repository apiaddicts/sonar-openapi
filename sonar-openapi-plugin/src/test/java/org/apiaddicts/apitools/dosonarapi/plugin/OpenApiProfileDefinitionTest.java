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
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.Context;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.BuiltInQualityProfile;
import org.apiaddicts.apitools.dosonarapi.checks.CheckList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.apiaddicts.apitools.dosonarapi.plugin.OpenApiProfileDefinition.SONAR_WAY_PROFILE;

public class OpenApiProfileDefinitionTest {

  @Test
  public void should_create_yaml_and_json_profiles() {
    OpenApiProfileDefinition definition = new OpenApiProfileDefinition();
    Context context = new BuiltInQualityProfilesDefinition.Context();

    definition.define(context);

    BuiltInQualityProfile yamlProfile = context.profile(CheckList.YAML_LANGUAGE, SONAR_WAY_PROFILE);
    assertThat(yamlProfile).isNotNull();
    assertThat(yamlProfile.language()).isEqualTo(CheckList.YAML_LANGUAGE);
    assertThat(yamlProfile.rules()).hasSizeGreaterThanOrEqualTo(2);
    assertThat(yamlProfile.rules()).allMatch(r -> r.repoKey().equals(CheckList.REPOSITORY_KEY));

    BuiltInQualityProfile jsonProfile = context.profile(CheckList.JSON_LANGUAGE, SONAR_WAY_PROFILE);
    assertThat(jsonProfile).isNotNull();
    assertThat(jsonProfile.language()).isEqualTo(CheckList.JSON_LANGUAGE);
    assertThat(jsonProfile.rules()).hasSizeGreaterThanOrEqualTo(2);
    assertThat(jsonProfile.rules()).allMatch(r -> r.repoKey().equals(CheckList.JSON_REPOSITORY_KEY));
  }
}
