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
