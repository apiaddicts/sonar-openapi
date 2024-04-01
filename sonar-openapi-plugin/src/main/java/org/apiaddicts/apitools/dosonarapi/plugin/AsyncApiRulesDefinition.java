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

import org.sonar.api.server.rule.RulesDefinition;
import org.apiaddicts.apitools.dosonarapi.checks.AsyncApiCheckList; 
import org.apiaddicts.apitools.dosonarapi.api.AsyncApiCustomRuleRepository; 
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AsyncApiRulesDefinition implements RulesDefinition, AsyncApiCustomRuleRepository {
  private static final String REPOSITORY_NAME = "SonarAnalyzer for AsyncAPI";
  private static final String RESOURCE_FOLDER = "org.sonar/l10n/asyncapi/rules/asyncapi"; 
  private static final Set<String> TEMPLATE_RULE_KEYS = new HashSet<>(); 

  private static RuleMetadataLoader getRuleMetadataLoader() {
    return new RuleMetadataLoader(RESOURCE_FOLDER);
  }

  @Override
  public void define(Context context) {
    NewRepository repository = context
      .createRepository(AsyncApiCheckList.REPOSITORY_KEY, AsyncApi.KEY) 
      .setName(REPOSITORY_NAME);

    getRuleMetadataLoader().addRulesByAnnotatedClass(repository, checkClasses());

    repository.rules().stream()
      .filter(rule -> TEMPLATE_RULE_KEYS.contains(rule.key()))
      .forEach(rule -> rule.setTemplate(true));

    repository.done();
  }

  @Override
  public String repositoryKey() {
    return AsyncApiCheckList.REPOSITORY_KEY; 
  }

  @Override
  public List<Class<?>> checkClasses() {
    return AsyncApiCheckList.getChecks(); 
  }
}
