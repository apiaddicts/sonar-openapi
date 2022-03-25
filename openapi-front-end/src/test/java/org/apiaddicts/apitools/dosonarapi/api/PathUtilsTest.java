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

import org.apiaddicts.apitools.dosonarapi.api.PathUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.apiaddicts.apitools.dosonarapi.api.PathUtils.checkPath;
import static org.apiaddicts.apitools.dosonarapi.api.PathUtils.isResourcePath;
import static org.apiaddicts.apitools.dosonarapi.api.PathUtils.terminalSegment;
import static org.apiaddicts.apitools.dosonarapi.api.PathUtils.trimTrailingSlash;

public class PathUtilsTest {
  @Test
  public void detects_nonspinal() {
    assertTrue(checkPath("/some/naive/path", PathUtils::isSpinalCase));
    assertTrue(checkPath("/some/naive/path-with-dashes", PathUtils::isSpinalCase));
    assertTrue(checkPath("/multi-spinal/path-example", PathUtils::isSpinalCase));
    assertTrue(checkPath("/multi-spinal/path-example/with-0123", PathUtils::isSpinalCase));

    assertFalse(checkPath("/Some/naive/Path", PathUtils::isSpinalCase));
    assertFalse(checkPath("/some_other/path", PathUtils::isSpinalCase));
    assertFalse(checkPath("/yetAnother/path", PathUtils::isSpinalCase));
    assertFalse(checkPath("/some/1234/weirdPath", PathUtils::isSpinalCase));
  }

  @Test
  public void ignores_path_variables() {
    assertTrue(checkPath("/pets/{someVariable}", PathUtils::isSpinalCase));
  }

  @Test
  public void detects_resoure_paths() {
    assertTrue(isResourcePath("/some/parrots"));
    assertTrue(isResourcePath("/"));
    assertTrue(isResourcePath("/some/parrots/"));
    assertFalse(isResourcePath("/some/parrots/{parrotId}"));
    assertFalse(isResourcePath("/some/parrots/{parrotId}/"));
    assertTrue(isResourcePath("/some/parrots/{parrotId}/feather"));
    assertTrue(isResourcePath("/some/parrots/{parrotId}/feather/"));
  }

  @Test
  public void trims_slashes() {
    assertThat(trimTrailingSlash("/")).isEqualTo("");
    assertThat(trimTrailingSlash("/something")).isEqualTo("/something");
    assertThat(trimTrailingSlash("/something/")).isEqualTo("/something");
  }

  @Test
  public void extracts_terminal_segments() {
    assertThat(terminalSegment("/")).isEqualTo("");
    assertThat(terminalSegment("/something")).isEqualTo("something");
    assertThat(terminalSegment("/something/")).isEqualTo("something");
    assertThat(terminalSegment("/something/else")).isEqualTo("else");
  }
}
