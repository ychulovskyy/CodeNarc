/*
 * Copyright 2009 the original author or authors.
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
package org.codenarc.rule.naming

import org.codehaus.groovy.ast.expr.DeclarationExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule

/**
 * Rule that verifies that the name of each variable matches a regular expression. By default it checks that
 * non-<code>final</code> variable names start with a lowercase letter and contains only letters or numbers.
 * By default, <code>final</code> variable names start with an uppercase letter and contain only uppercase
 * letters, numbers and underscores.
 * <p/>
 * The <code>regex</code> property specifies the default regular expression to validate a variable name.
 * It is required and cannot be null or empty. It defaults to '[a-z][a-zA-Z0-9]*'.
 * <p/>
 * The <code>finalRegex</code> property specifies the regular expression to validate <code>final</code>
 * variable names. It is optional but defaults to '[A-Z][A-Z0-9_]*'. If not set, then <code>regex</code> is
 * used to validate <code>final</code> variables.  
 *
 * @author Chris Mair
 * @version $Revision$ - $Date$
 */
class VariableNameRule extends AbstractAstVisitorRule {
    String name = 'VariableName'
    int priority = 2
    String regex = DEFAULT_VAR_NAME
    String finalRegex = DEFAULT_CONST_NAME

    Class astVisitorClass = VariableNameAstVisitor
}

class VariableNameAstVisitor extends AbstractAstVisitor  {
    void visitDeclarationExpression(DeclarationExpression declarationExpression) {
        assert rule.regex
        if (!isAlreadyVisited(declarationExpression)) {
            def leftExpression = declarationExpression.leftExpression
            def varExpressions = leftExpression.properties['expressions'] ?: [leftExpression]
            def re = rule.finalRegex && isFinal(declarationExpression, varExpressions[0]) ? rule.finalRegex : rule.regex

            varExpressions.each { varExpression ->
                if (!(varExpression.name ==~ re)) {
                    def msg = varExpressions.size() > 1 ? "Variable name: [$varExpression.name]" : null
                    addViolation(declarationExpression, msg)
                }
            }
            registerAsVisited(declarationExpression)
        }
        super.visitDeclarationExpression(declarationExpression)
    }

    /**
     * NOTE: THIS IS A WORKAROUND.
     * There does not seem to be an easy way to determine whether the 'final' modifier has been
     * specified for a variable declaration. Return true if the 'final' is present before the variable name.
     */
    private boolean isFinal(declarationExpression, variableExpression) {
        def sourceLine = sourceCode.lines[variableExpression.lineNumber-1]
        def modifiers = sourceLine[declarationExpression.columnNumber-1..variableExpression.columnNumber-2]
        return modifiers.contains('final')
    }

}