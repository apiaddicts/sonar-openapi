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

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.api.utils.AnnotationUtils;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.checks.CheckList;

public class OpenApiProfileDefinition implements BuiltInQualityProfilesDefinition {
  public static final String SONAR_WAY_PROFILE = "Sonar way";

  @Override
  public void define(BuiltInQualityProfilesDefinition.Context context) {
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(SONAR_WAY_PROFILE, OpenApi.KEY);
    profile.setDefault(true);
    for (Class<?> check : CheckList.getChecks()) {
      Rule annotation = AnnotationUtils.getAnnotation(check, Rule.class);
      profile.activateRule(CheckList.REPOSITORY_KEY, annotation.key());
    }
    profile.done();
  }
}
