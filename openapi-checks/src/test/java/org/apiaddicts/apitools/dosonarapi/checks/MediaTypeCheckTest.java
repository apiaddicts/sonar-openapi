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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.apiaddicts.apitools.dosonarapi.checks.MediaTypeCheck.MEDIA_RANGE_PATTERN;
import static org.apiaddicts.apitools.dosonarapi.checks.MediaTypeCheck.MIME_TYPE_PATTERN;

public class MediaTypeCheckTest {
  @Test
  public void verify_media_range_in_v3() {
    OpenApiCheckVerifier.verify("src/test/resources/checks/v3/media-type.yaml", new MediaTypeCheck(), false, true, false);
  }

  @Test
  public void verify_media_type_in_v2() {
    OpenApiCheckVerifier.verify("src/test/resources/checks/v2/media-type.yaml", new MediaTypeCheck(), true, false, false);
  }

  @Test
  public void can_detect_media_types() {
    assertTrue(MIME_TYPE_PATTERN.matcher("text/plain; charset=utf-8").matches());
    assertTrue(MIME_TYPE_PATTERN.matcher("application/json").matches());
    assertTrue(MIME_TYPE_PATTERN.matcher("application/vnd.github+json").matches());
    assertTrue(MIME_TYPE_PATTERN.matcher("application/vnd.github.v3+json").matches());
    assertTrue(MIME_TYPE_PATTERN.matcher("application/vnd.github.v3.raw+json").matches());
    assertTrue(MIME_TYPE_PATTERN.matcher("application/vnd.github.v3.text+json").matches());
    assertTrue(MIME_TYPE_PATTERN.matcher("application/vnd.github.v3.html+json").matches());
    assertTrue(MIME_TYPE_PATTERN.matcher("application/vnd.github.v3.diff").matches());
    assertTrue(MIME_TYPE_PATTERN.matcher("application/vnd.github.v3.patch").matches());
  }

  @Test
  public void can_report_incorrect_media_type() {
    assertFalse(MIME_TYPE_PATTERN.matcher("application").matches());
  }

  @Test
  public void can_detect_media_ranges() {
    assertTrue(MEDIA_RANGE_PATTERN.matcher("text/plain; charset=utf-8").matches());
    assertTrue(MEDIA_RANGE_PATTERN.matcher("application/json").matches());
    assertTrue(MEDIA_RANGE_PATTERN.matcher("application/vnd.github+json").matches());
    assertTrue(MEDIA_RANGE_PATTERN.matcher("application/vnd.github.v3+json").matches());
    assertTrue(MEDIA_RANGE_PATTERN.matcher("application/vnd.github.v3.raw+json").matches());
    assertTrue(MEDIA_RANGE_PATTERN.matcher("application/vnd.github.v3.text+json").matches());
    assertTrue(MEDIA_RANGE_PATTERN.matcher("application/vnd.github.v3.html+json").matches());
    assertTrue(MEDIA_RANGE_PATTERN.matcher("application/vnd.github.v3.diff").matches());
    assertTrue(MEDIA_RANGE_PATTERN.matcher("application/vnd.github.v3.patch").matches());
    assertTrue(MEDIA_RANGE_PATTERN.matcher("application/*").matches());
  }

  @Test
  public void can_report_incorrect_media_ranges() {
    assertFalse(MEDIA_RANGE_PATTERN.matcher("application").matches());
  }
}
