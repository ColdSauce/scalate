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
package org.fusesource.scalate
package filter

import servlet.ServletRenderContext

/**
 * Represents a request to filter content.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
case class FilterRequest(filter: String, content: Any)

/**
 * Filters transform content at a given URI.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
trait Filter {
  def filter(context: RenderContext, content: String): String
}

/**
 * A useful filter for wrapping other filters as a Pipeline (a top level processor of stand alone resources)
 */
case class NoLayoutFilter(val next: Filter, contentType: String) extends Filter {
  def filter(context: RenderContext, content: String) = {
    context.attributes("layout") = "" // disable the layout
    context match {
      case x: ServletRenderContext => x.response.setContentType(contentType)
      case _ =>
    }
    next.filter(context, content)
  }
}
