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

import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.rule.RuleKey;
import org.apiaddicts.apitools.dosonarapi.checks.CheckList;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiCheck;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiCustomRuleRepository;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper class to facilitate the usage of checks.
 */
public class OpenApiChecks {
  private final CheckFactory checkFactory;
  private Set<Checks<OpenApiCheck>> checksByRepository = new HashSet<>();

  private OpenApiChecks(CheckFactory checkFactory) {
    this.checkFactory = checkFactory;
  }

  public static OpenApiChecks createOpenApiCheck(CheckFactory checkFactory) {
    return new OpenApiChecks(checkFactory);
  }

  public OpenApiChecks addChecks(String repositoryKey, Iterable<Class<?>> checkClass) {
    checksByRepository.add(checkFactory
      .<OpenApiCheck>create(repositoryKey)
      .addAnnotatedChecks(checkClass));

    return this;
  }

  public OpenApiChecks addCustomChecks(@Nullable OpenApiCustomRuleRepository[] customRuleRepositories) {
    if (customRuleRepositories != null) {

      for (OpenApiCustomRuleRepository ruleRepository : customRuleRepositories) {
        if (!ruleRepository.repositoryKey().equals(CheckList.REPOSITORY_KEY)) {
          addChecks(ruleRepository.repositoryKey(), new ArrayList<>(ruleRepository.checkClasses()));
        }
      }
    }

    return this;
  }

  public List<OpenApiCheck> all() {
    List<OpenApiCheck> allVisitors = new ArrayList<>();

    for (Checks<OpenApiCheck> checks : checksByRepository) {
      allVisitors.addAll(checks.all());
    }

    return allVisitors;
  }

  @Nullable
  public RuleKey ruleKeyFor(OpenApiCheck check) {
    RuleKey ruleKey;

    for (Checks<OpenApiCheck> checks : checksByRepository) {
      ruleKey = checks.ruleKey(check);

      if (ruleKey != null) {
        return ruleKey;
      }
    }
    return null;
  }
}
