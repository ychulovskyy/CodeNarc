/*
 * Copyright 2014 the original author or authors.
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
package org.codenarc.rule.formatting

import org.codehaus.groovy.ast.PackageNode
import org.codenarc.rule.AbstractRule
import org.codenarc.source.SourceCode
import org.codenarc.util.MultilineCommentChecker

/**
 * Makes sure there are no blank lines before the package declaration of a source code file.
 *
 * @author Joe Sondow
 */
class BlankLineBeforePackageRule extends AbstractRule {

    String name = 'BlankLineBeforePackage'
    int priority = 3

    @Override
    void applyTo(SourceCode sourceCode, List violations) {

        PackageNode packageNode = sourceCode.ast?.package
        if (packageNode) {
            MultilineCommentChecker multilineCommentChecker = new MultilineCommentChecker()

            for (int index = 0; index < packageNode.lineNumber; index++) {
                multilineCommentChecker.processLine(sourceCode.line(index))

                if (sourceCode.line(index).isEmpty() && !multilineCommentChecker.inMultilineComment) {
                    violations.add(createViolation(index, sourceCode.line(index),
                        "Blank line precedes package declaration in file $sourceCode.name"))
                }
            }
        }
    }
}
