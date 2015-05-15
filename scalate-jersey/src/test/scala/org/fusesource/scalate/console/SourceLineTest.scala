/**
 * Copyright (C) 2009-2011 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fusesource.scalate.console

import org.fusesource.scalate.test.FunSuiteSupport

class SourceLineTest extends FunSuiteSupport {
  val line = SourceLine(1, "abcd")

  test("split line") {
    expect(("", "a", "bcd")) { line.splitOnCharacter(0) }
    expect(("a", "b", "cd")) { line.splitOnCharacter(1) }
    expect(("ab", "c", "d")) { line.splitOnCharacter(2) }
    expect(("abc", "d", "")) { line.splitOnCharacter(3) }
    expect(("abcd", "", "")) { line.splitOnCharacter(4) }
    expect(("abcd", "", "")) { line.splitOnCharacter(5) }
  }

}