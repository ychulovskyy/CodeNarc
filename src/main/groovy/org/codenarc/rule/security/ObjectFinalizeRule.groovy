/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenarc.rule.security

import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.rule.AbstractMethodCallExpressionVisitor
import org.codenarc.util.AstUtil

/**
 * The finalize() method should only be called by the JVM after the object has been garbage collected.
 *
 * @author 'Hamlet D'Arcy'
  */
class ObjectFinalizeRule extends AbstractAstVisitorRule {
    String name = 'ObjectFinalize'
    int priority = 2
    Class astVisitorClass = ObjectFinalizeAstVisitor
}

class ObjectFinalizeAstVisitor extends AbstractMethodCallExpressionVisitor {

    @Override
    void visitMethodCallExpression(MethodCallExpression call) {

        if (AstUtil.isMethodNamed(call, 'finalize', 0)) {
            addViolation(call, 'The finalize() method should only be called by the JVM after the object has been garbage collected')
        }
    }
}
