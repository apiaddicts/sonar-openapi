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

import java.util.Set;

import org.apache.commons.validator.routines.EmailValidator;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiCheck;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;

@Rule(key = ContactValidEmailCheck.CHECK_KEY)
public class ContactValidEmailCheck extends OpenApiCheck {
    protected static final String CHECK_KEY = "ContactValidEmail";

    protected static final String MESSAGE = "There should only be a valid email address in contact.";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.CONTACT, OpenApi3Grammar.CONTACT);
    }

    @Override
    protected void visitNode(JsonNode node) {
        JsonNode email = node.get("email");
        if (isValidString(email.getTokenValue()) && !EmailValidator.getInstance().isValid(email.getTokenValue())) {
            addIssue(MESSAGE, email);
        }
    }

    private boolean isValidString(String s) {
        return s != null && !s.trim().isEmpty() && !s.equals("null");
    }
}
