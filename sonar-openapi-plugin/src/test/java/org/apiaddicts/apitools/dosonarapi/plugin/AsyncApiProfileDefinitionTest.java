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
import org.mockito.Mockito;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.Context;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.NewBuiltInQualityProfile;
import org.apiaddicts.apitools.dosonarapi.checks.AsyncApiCheckList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.apiaddicts.apitools.dosonarapi.plugin.AsyncApiProfileDefinition.SONAR_WAY_PROFILE;

public class AsyncApiProfileDefinitionTest {
  private static Context context(NewBuiltInQualityProfile profile) {
    Context context = mock(Context.class);
    when(context.createBuiltInQualityProfile(anyString(), anyString())).thenReturn(profile);
    return context;
  }

  @Test
  public void should_create_async_api_way_profile() {
    AsyncApiProfileDefinition definition = new AsyncApiProfileDefinition();
    NewBuiltInQualityProfile profile = mock(NewBuiltInQualityProfile.class);
    Context context = context(profile);

    definition.define(context);

    verify(context).createBuiltInQualityProfile(SONAR_WAY_PROFILE, AsyncApi.KEY);
    verify(profile).setDefault(true);
    verify(profile, Mockito.atLeast(2)).activateRule(eq(AsyncApiCheckList.REPOSITORY_KEY), anyString());
  }
}

