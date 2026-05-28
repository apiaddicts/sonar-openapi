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
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.BuiltInQualityProfile;
import org.apiaddicts.apitools.dosonarapi.checks.CheckList;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenApiProfileDefinitionTest {

  @Test
  public void defines_sonar_way_profile_for_openapi_language() {
    OpenApiProfileDefinition definition = new OpenApiProfileDefinition();
    BuiltInQualityProfilesDefinition.Context context = new BuiltInQualityProfilesDefinition.Context();

    definition.define(context);

    BuiltInQualityProfile profile = context.profile(CheckList.OPENAPI_LANGUAGE, OpenApiProfileDefinition.SONAR_WAY_PROFILE);
    assertThat(profile).isNotNull();
    assertThat(profile.language()).isEqualTo(CheckList.OPENAPI_LANGUAGE);
    assertThat(profile.name()).isEqualTo(OpenApiProfileDefinition.SONAR_WAY_PROFILE);
    assertThat(profile.rules()).hasSize(CheckList.getChecks().size());
  }

  @Test
  public void profile_rules_belong_to_openapi_repository() {
    OpenApiProfileDefinition definition = new OpenApiProfileDefinition();
    BuiltInQualityProfilesDefinition.Context context = new BuiltInQualityProfilesDefinition.Context();

    definition.define(context);

    BuiltInQualityProfile profile = context.profile(CheckList.OPENAPI_LANGUAGE, OpenApiProfileDefinition.SONAR_WAY_PROFILE);
    assertThat(profile.rules()).isNotEmpty();
    assertThat(profile.rules())
      .allMatch(r -> r.repoKey().equals(CheckList.OPENAPI_REPOSITORY_KEY));
  }

  @Test
  public void only_openapi_language_profile_is_created() {
    OpenApiProfileDefinition definition = new OpenApiProfileDefinition();
    BuiltInQualityProfilesDefinition.Context context = new BuiltInQualityProfilesDefinition.Context();

    definition.define(context);

    assertThat(context.profile(CheckList.YAML_LANGUAGE, OpenApiProfileDefinition.SONAR_WAY_PROFILE)).isNull();
    assertThat(context.profile(CheckList.JSON_LANGUAGE, OpenApiProfileDefinition.SONAR_WAY_PROFILE)).isNull();
    assertThat(context.profile(CheckList.OPENAPI_LANGUAGE, OpenApiProfileDefinition.SONAR_WAY_PROFILE)).isNotNull();
  }
}
