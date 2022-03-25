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
package org.apiaddicts.apitools.dosonarapi.checks;

import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.OpenApiCheckVerifier;

import static org.junit.Assert.assertEquals;
import static org.apiaddicts.apitools.dosonarapi.checks.PathMaskeradingCheck.ConflictMode.AMBIGUOUS;
import static org.apiaddicts.apitools.dosonarapi.checks.PathMaskeradingCheck.ConflictMode.MASKED;
import static org.apiaddicts.apitools.dosonarapi.checks.PathMaskeradingCheck.ConflictMode.NONE;
import static org.apiaddicts.apitools.dosonarapi.checks.PathMaskeradingCheck.split;

public class PathMaskeradingCheckTest {

  private static PathMaskeradingCheck.ConflictMode hasConflicts(String tested, String reference) {
    String[] testedParts = split(tested);
    String[] referenceParts = split(reference);
    return new PathMaskeradingCheck.ConflictChecker().check(testedParts, referenceParts);
  }

  @Test
  public void verify_path_maskerading_in_v2() {
    OpenApiCheckVerifier.verify("src/test/resources/checks/v2/path-maskerading.yaml", new PathMaskeradingCheck(), true);
  }

  @Test
  public void verify_path_maskerading_in_v3() {
    OpenApiCheckVerifier.verify("src/test/resources/checks/v3/path-maskerading.yaml", new PathMaskeradingCheck(), false);
  }

  @Test
  public void paths_of_different_length_dont_conflict() {
    assertEquals(NONE, hasConflicts("/a/{b}/c", "/a/b"));
    assertEquals(NONE, hasConflicts("/a/{b}", "/a/b/c"));
    assertEquals(NONE, hasConflicts("/a/{b}", "/a/b/"));
    assertEquals(NONE, hasConflicts("/a/b", "/a/b/"));
    assertEquals(NONE, hasConflicts("/a/{b}", "/a/{b}/"));
    assertEquals(NONE, hasConflicts("/a/", "/a/{b}"));
  }

  @Test
  public void different_paths_of_same_length_dont_conflict() {
    assertEquals(NONE, hasConflicts("/a/b", "/a/c"));
  }

  @Test
  public void paths_with_parameter_leaves_are_ambiguous() {
    assertEquals(AMBIGUOUS, hasConflicts("/a/{b}", "/a/{b}"));
    assertEquals(AMBIGUOUS, hasConflicts("/a/{b}", "/a/{o}"));
  }

  @Test
  public void paths_with_mixed_parameters_are_ambiguous() {
    assertEquals(AMBIGUOUS, hasConflicts("/a/{b}", "/{a}/b"));
    assertEquals(AMBIGUOUS, hasConflicts("/{a}/b/c", "/a/b/{c}"));
    assertEquals(AMBIGUOUS, hasConflicts("/{a}/b/{c}", "/a/{b}/c"));
  }

  @Test
  public void paths_with_shared_parameters_are_not_ambiguous() {
    assertEquals(NONE, hasConflicts("/a/{b}/c/", "/a/{b}/c/{d}"));
  }

  @Test
  public void parameter_is_masked_by_defined_segment() {
    assertEquals(MASKED, hasConflicts("/a/b/{c}", "/a/b/c"));
    assertEquals(MASKED, hasConflicts("/{a}/b/c", "/a/b/c"));
    assertEquals(MASKED, hasConflicts("/{a}/{b}", "/{o}/b"));
  }

}
