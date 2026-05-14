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
package org.apiaddicts.apitools.dosonarapi.api;

import java.io.File;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestOpenApiVisitorRunnerTest {

  private static final File V3_FILE = new File(
      TestOpenApiVisitorRunnerTest.class.getResource("/petstore.yaml").getFile());
  private static final File V2_FILE = new File(
      TestOpenApiVisitorRunnerTest.class.getResource("/models/v2/pet-store.yaml").getFile());
  private static final File V31_FILE = new File(
      TestOpenApiVisitorRunnerTest.class.getResource("/petstore-v31.yaml").getFile());
  private static final File V32_FILE = new File(
      TestOpenApiVisitorRunnerTest.class.getResource("/petstore-v32.yaml").getFile());

  @Test
  public void create_context_with_default_args_uses_v3_parser() {
    OpenApiVisitorContext ctx = TestOpenApiVisitorRunner.createContext(V3_FILE);
    assertThat(ctx.rootTree()).isNotNull();
  }

  @Test
  public void create_context_with_v2_flag_uses_v2_parser() {
    OpenApiVisitorContext ctx = TestOpenApiVisitorRunner.createContext(V2_FILE, true);
    assertThat(ctx.rootTree()).isNotNull();
  }

  @Test
  public void create_context_with_three_flags_false_uses_v3_parser() {
    OpenApiVisitorContext ctx = TestOpenApiVisitorRunner.createContext(V3_FILE, false, false, false);
    assertThat(ctx.rootTree()).isNotNull();
  }

  @Test
  public void create_context_with_v31_flag_uses_v31_parser() {
    OpenApiVisitorContext ctx = TestOpenApiVisitorRunner.createContext(V31_FILE, false, false, true);
    assertThat(ctx.rootTree()).isNotNull();
  }

  @Test
  public void create_context_with_v32_flag_uses_v32_parser() {
    OpenApiVisitorContext ctx = TestOpenApiVisitorRunner.createContext(V32_FILE, false, false, false, true);
    assertThat(ctx.rootTree()).isNotNull();
  }

  @Test
  public void scan_file_for_comments_with_three_flags() {
    TestOpenApiVisitorRunner.scanFileForComments(V3_FILE, false, false, false);
  }

  @Test
  public void scan_file_for_comments_with_v32_flag() {
    TestOpenApiVisitorRunner.scanFileForComments(V32_FILE, false, false, false, true);
  }

  @Test
  public void scan_file_with_visitors() {
    TestOpenApiVisitorRunner.scanFile(V3_FILE, new OpenApiVisitor());
  }
}
