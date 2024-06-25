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
package org.apiaddicts.apitools.dosonarapi.api.v31;

import org.apiaddicts.apitools.dosonarapi.openapi.BaseNodeTest;
import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class FileUploadPayloadsTest extends BaseNodeTest<OpenApi31Grammar> {

  @Test
  public void can_parse_upload_binary_post() {
    JsonNode node = parseResource(OpenApi31Grammar.PATHS, "/models/v31/binarypost.yaml");

    assertPropertyKeys(node).containsOnly("/upload");

  }

  @Test
  public void can_parse_upload_img64() {
    JsonNode node = parseResource(OpenApi31Grammar.PATHS, "/models/v31/imgbase64.yaml");

    assertPropertyKeys(node).containsOnly("/upload-image");

  }

  @Test
  public void can_parse_upload_multipart() {
    JsonNode node = parseResource(OpenApi31Grammar.PATHS, "/models/v31/multipart.yaml");

    assertPropertyKeys(node).containsOnly("/upload-multipart");

  }

}
