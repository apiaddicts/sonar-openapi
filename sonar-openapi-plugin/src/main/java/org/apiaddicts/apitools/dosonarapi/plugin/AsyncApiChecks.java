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
import org.apiaddicts.apitools.dosonarapi.checks.AsyncApiCheckList;
import org.apiaddicts.apitools.dosonarapi.api.AsyncApiCheck;
import org.apiaddicts.apitools.dosonarapi.api.AsyncApiCustomRuleRepository;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper class to facilitate the usage of checks for AsyncAPI.
 */
public class AsyncApiChecks {
  private final CheckFactory checkFactory;
  private Set<Checks<AsyncApiCheck>> checksByRepository = new HashSet<>();

  private AsyncApiChecks(CheckFactory checkFactory) {
    this.checkFactory = checkFactory;
  }

  public static AsyncApiChecks createAsyncApiCheck(CheckFactory checkFactory) {
    return new AsyncApiChecks(checkFactory);
  }

  public AsyncApiChecks addChecks(String repositoryKey, Iterable<Class<?>> checkClass) {
    checksByRepository.add(checkFactory
      .<AsyncApiCheck>create(repositoryKey)
      .addAnnotatedChecks(checkClass));

    return this;
  }

  public AsyncApiChecks addCustomChecks(@Nullable AsyncApiCustomRuleRepository[] customRuleRepositories) {
    if (customRuleRepositories != null) {
      for (AsyncApiCustomRuleRepository ruleRepository : customRuleRepositories) {
        if (!ruleRepository.repositoryKey().equals(AsyncApiCheckList.REPOSITORY_KEY)) {
          addChecks(ruleRepository.repositoryKey(), new ArrayList<>(ruleRepository.checkClasses()));
        }
      }
    }
    return this;
  }

  public List<AsyncApiCheck> all() {
    List<AsyncApiCheck> allChecks = new ArrayList<>();
    for (Checks<AsyncApiCheck> checks : checksByRepository) {
      allChecks.addAll(checks.all());
    }
    return allChecks;
  }

  @Nullable
  public RuleKey ruleKeyFor(AsyncApiCheck check) {
    for (Checks<AsyncApiCheck> checks : checksByRepository) {
      RuleKey ruleKey = checks.ruleKey(check);
      if (ruleKey != null) {
        return ruleKey;
      }
    }
    return null;
  }
}
