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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.IssueLocation;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiCheck;
import org.apiaddicts.apitools.dosonarapi.api.PreciseIssue;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import static org.apiaddicts.apitools.dosonarapi.checks.PathMaskeradingCheck.ConflictMode.AMBIGUOUS;
import static org.apiaddicts.apitools.dosonarapi.checks.PathMaskeradingCheck.ConflictMode.MASKED;
import static org.apiaddicts.apitools.dosonarapi.checks.PathMaskeradingCheck.ConflictMode.NONE;
import static org.apiaddicts.apitools.dosonarapi.checks.PathMaskeradingCheck.ConflictMode.UNKNOWN;
import static org.apiaddicts.apitools.dosonarapi.api.PathUtils.isVariable;

@Rule(key = PathMaskeradingCheck.CHECK_KEY)
public class PathMaskeradingCheck extends OpenApiCheck {
  public static final String MASK_MESSAGE = "These paths are masking each other.";
  private static final String AMBIGUOUS_MESSAGE = "These paths are ambiguous.";
  public static final String CHECK_KEY = "PathMaskerading";

  @VisibleForTesting
  static String[] split(String source) {
    String s = strip(source);
    String[] split = s.split("/");
    if (source.endsWith("/")) {
      String[] result = new String[split.length + 1];
      System.arraycopy(split, 0, result, 0, split.length);
      result[split.length] = "";
      return result;
    } else {
      return split;
    }
  }

  private static String strip(String s) {
    if (s.startsWith("/")) {
      return s.substring(1);
    } else {
      return s;
    }
  }

  private static void ensureSize(List<List<Bucket>> list, int size) {
    if (list.size() < size) {
      for (int i = list.size(); i <= size; ++i) {
        list.add(new ArrayList<>());
      }
    }
  }

  @Override
  public Set<AstNodeType> subscribedKinds() {
    return ImmutableSet.of(OpenApi2Grammar.PATHS, OpenApi3Grammar.PATHS);
  }

  @Override
  public void visitNode(JsonNode node) {
    List<List<Bucket>> pathsByLength = sortPathsByLength(node.propertyMap().values());
    for (List<Bucket> buckets : pathsByLength) {
      for (int i = 0; i < buckets.size() - 1; ++i) {
        for (int j = i + 1; j < buckets.size(); ++j) {
          ConflictMode mode = new ConflictChecker().check(buckets.get(i).path, buckets.get(j).path);
          if (mode != NONE) {
            String message = mode == MASKED ? MASK_MESSAGE : AMBIGUOUS_MESSAGE;
            PreciseIssue issue = addIssue(message, buckets.get(i).node);
            issue.secondary(IssueLocation.preciseLocation(message, buckets.get(j).node));
          }
        }
      }
    }
  }

  private List<List<Bucket>> sortPathsByLength(Collection<JsonNode> properties) {
    ArrayList<List<Bucket>> result = new ArrayList<>();
    for (JsonNode property : properties) {
      JsonNode keyNode = property.key();
      String[] split = split(keyNode.getTokenValue());
      ensureSize(result, split.length);
      List<Bucket> list = result.get(split.length - 1);
      list.add(new Bucket(split, keyNode));
    }
    return result;
  }

  enum ConflictMode {
    UNKNOWN,
    NONE,
    AMBIGUOUS,
    MASKED
  }

  private static class Bucket {
    private final String[] path;
    private final JsonNode node;

    private Bucket(String[] path, JsonNode node) {
      this.path = path;
      this.node = node;
    }
  }

  @VisibleForTesting
  static class ConflictChecker {
    private int maskSource;

    public ConflictMode check(String[] first, String[] second) {
      if (first.length != second.length) {
        return NONE;
      }
      maskSource = 0; // source is the one who is not a parameter
      ConflictMode result = UNKNOWN;
      for (int i = 0; result != NONE && i < first.length; ++i) {
        if (onlyOneIsEmpty(first[i], second[i])) {
          return NONE;
        }
        result = updateResult(first[i], second[i], result);
      }
      return result;
    }

    private boolean onlyOneIsEmpty(String s, String s1) {
      return s.isEmpty() ^ s1.isEmpty();
    }

    /**
     * Checks if the 2 segments create more conflict or disambiguate the paths.
     * @return {@code true if the segments create a sufficient differentiation}
     */
    private ConflictMode updateResult(String segment1, String segment2, ConflictMode result) {
      boolean firstIsParam = isVariable(segment1);
      boolean secondIsParam = isVariable(segment2);
      if (firstIsParam && secondIsParam) {
        result = result != MASKED ? AMBIGUOUS : MASKED;
      } else if (firstIsParam ^ secondIsParam) {
        if (result == MASKED) {
          result = (maskSource == 1) ^ secondIsParam ? AMBIGUOUS : MASKED;
          maskSource = getMaskSource(firstIsParam);
        } else if (maskSource == 0) {
          result = MASKED;
          maskSource = getMaskSource(firstIsParam);
        }
        // else: already ambiguous by masking in several places, ambiguity is going on
      } else if (!segment1.equals(segment2)) {
        return NONE;
      }
      return result;
    }

    private int getMaskSource(boolean firstIsParam) {
      return firstIsParam ? 2 : 1;
    }
  }
}
