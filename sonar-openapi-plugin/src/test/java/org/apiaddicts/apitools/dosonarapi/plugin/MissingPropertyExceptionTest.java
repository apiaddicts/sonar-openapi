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

import static org.assertj.core.api.Assertions.assertThat;

public class MissingPropertyExceptionTest {

  @Test
  public void stores_property_name_and_message() {
    MissingPropertyException ex = new MissingPropertyException("my.property");
    assertThat(ex.getPropertyName()).isEqualTo("my.property");
    assertThat(ex.getMessage()).isEqualTo("Property my.property is not defined!");
  }

  @Test
  public void is_runtime_exception() {
    MissingPropertyException ex = new MissingPropertyException("foo");
    assertThat(ex).isInstanceOf(RuntimeException.class);
  }
}
