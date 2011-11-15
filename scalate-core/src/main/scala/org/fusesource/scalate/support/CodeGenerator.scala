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
package org.fusesource.scalate.support

import _root_.scala.util.parsing.input.{OffsetPosition}
import collection.immutable.TreeMap
import org.fusesource.scalate.{TemplateSource, Binding, TemplateEngine}

case class Code(className: String, source: String, dependencies: Set[String], positions: TreeMap[OffsetPosition, OffsetPosition])

trait CodeGenerator {
  def generate(engine: TemplateEngine, source: TemplateSource, bindings: Traversable[Binding]): Code

  val stratumName: String

}
