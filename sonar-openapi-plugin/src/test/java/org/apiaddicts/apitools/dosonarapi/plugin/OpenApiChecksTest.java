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

import java.util.List;
import org.junit.Test;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.rule.RuleKey;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiCustomRuleRepository;
import org.apiaddicts.apitools.dosonarapi.checks.CheckList;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenApiChecksTest {

  private OpenApiCustomRuleRepository repoWithKey(String key) {
    return new OpenApiCustomRuleRepository() {
      @Override public String repositoryKey() { return key; }
      @Override public List<Class<?>> checkClasses() { return CheckList.getChecks(); }
    };
  }

  private CheckFactory factoryWithRule(String repoKey, String ruleKey) {
    ActiveRules rules = new ActiveRulesBuilder()
      .create(RuleKey.of(repoKey, ruleKey)).activate()
      .build();
    return new CheckFactory(rules);
  }

  @Test
  public void addCustomYamlChecks_adds_non_json_repo() {
    CheckFactory factory = factoryWithRule("my-custom-yaml", "PathMaskerading");
    OpenApiChecks checks = OpenApiChecks.createOpenApiCheck(factory)
      .addCustomYamlChecks(new OpenApiCustomRuleRepository[]{repoWithKey("my-custom-yaml")});
    assertThat(checks.all()).isNotEmpty();
  }

  @Test
  public void addCustomYamlChecks_skips_json_repo() {
    CheckFactory factory = factoryWithRule("my-custom-json", "PathMaskerading");
    OpenApiChecks checks = OpenApiChecks.createOpenApiCheck(factory)
      .addCustomYamlChecks(new OpenApiCustomRuleRepository[]{repoWithKey("my-custom-json")});
    assertThat(checks.all()).isEmpty();
  }

  @Test
  public void addCustomYamlChecks_skips_base_yaml_repo() {
    CheckFactory factory = factoryWithRule(CheckList.YAML_REPOSITORY_KEY, "PathMaskerading");
    OpenApiChecks checks = OpenApiChecks.createOpenApiCheck(factory)
      .addCustomYamlChecks(new OpenApiCustomRuleRepository[]{repoWithKey(CheckList.YAML_REPOSITORY_KEY)});
    assertThat(checks.all()).isEmpty();
  }

  @Test
  public void addCustomJsonChecks_adds_json_repo() {
    CheckFactory factory = factoryWithRule("my-custom-json", "PathMaskerading");
    OpenApiChecks checks = OpenApiChecks.createOpenApiCheck(factory)
      .addCustomJsonChecks(new OpenApiCustomRuleRepository[]{repoWithKey("my-custom-json")});
    assertThat(checks.all()).isNotEmpty();
  }

  @Test
  public void addCustomJsonChecks_skips_non_json_repo() {
    CheckFactory factory = factoryWithRule("my-custom-yaml", "PathMaskerading");
    OpenApiChecks checks = OpenApiChecks.createOpenApiCheck(factory)
      .addCustomJsonChecks(new OpenApiCustomRuleRepository[]{repoWithKey("my-custom-yaml")});
    assertThat(checks.all()).isEmpty();
  }

  @Test
  public void addCustomJsonChecks_skips_base_json_repo() {
    CheckFactory factory = factoryWithRule(CheckList.JSON_REPOSITORY_KEY, "PathMaskerading");
    OpenApiChecks checks = OpenApiChecks.createOpenApiCheck(factory)
      .addCustomJsonChecks(new OpenApiCustomRuleRepository[]{repoWithKey(CheckList.JSON_REPOSITORY_KEY)});
    assertThat(checks.all()).isEmpty();
  }

  @Test
  public void addCustomChecks_handles_null() {
    ActiveRules rules = new ActiveRulesBuilder().build();
    CheckFactory factory = new CheckFactory(rules);
    OpenApiChecks checks = OpenApiChecks.createOpenApiCheck(factory)
      .addCustomYamlChecks(null)
      .addCustomJsonChecks(null);
    assertThat(checks.all()).isEmpty();
  }

  @Test
  public void addCustomChecks_adds_non_built_in_repo() {
    CheckFactory factory = factoryWithRule("my-custom-repo", "PathMaskerading");
    OpenApiChecks checks = OpenApiChecks.createOpenApiCheck(factory)
      .addCustomChecks(new OpenApiCustomRuleRepository[]{repoWithKey("my-custom-repo")});
    assertThat(checks.all()).isNotEmpty();
  }

  @Test
  public void addCustomChecks_skips_yaml_built_in_repo() {
    CheckFactory factory = factoryWithRule(CheckList.YAML_REPOSITORY_KEY, "PathMaskerading");
    OpenApiChecks checks = OpenApiChecks.createOpenApiCheck(factory)
      .addCustomChecks(new OpenApiCustomRuleRepository[]{repoWithKey(CheckList.YAML_REPOSITORY_KEY)});
    assertThat(checks.all()).isEmpty();
  }

  @Test
  public void addCustomChecks_skips_json_built_in_repo() {
    CheckFactory factory = factoryWithRule(CheckList.JSON_REPOSITORY_KEY, "PathMaskerading");
    OpenApiChecks checks = OpenApiChecks.createOpenApiCheck(factory)
      .addCustomChecks(new OpenApiCustomRuleRepository[]{repoWithKey(CheckList.JSON_REPOSITORY_KEY)});
    assertThat(checks.all()).isEmpty();
  }

  @Test
  public void addCustomChecks_handles_null_input() {
    ActiveRules rules = new ActiveRulesBuilder().build();
    CheckFactory factory = new CheckFactory(rules);
    OpenApiChecks checks = OpenApiChecks.createOpenApiCheck(factory)
      .addCustomChecks(null);
    assertThat(checks.all()).isEmpty();
  }

  @Test
  public void ruleKeyFor_returns_null_when_not_found() {
    ActiveRules rules = new ActiveRulesBuilder().build();
    CheckFactory factory = new CheckFactory(rules);
    OpenApiChecks checks = OpenApiChecks.createOpenApiCheck(factory);
    assertThat(checks.ruleKeyFor(new org.apiaddicts.apitools.dosonarapi.checks.PathMaskeradingCheck())).isNull();
  }
}
