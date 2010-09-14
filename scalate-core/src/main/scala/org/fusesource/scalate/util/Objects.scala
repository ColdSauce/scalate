/**
 * Copyright (C) 2009-2010 the original author or authors.
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

package org.fusesource.scalate.util

import java.lang.reflect.Constructor

/**
 * Helper object for working with objects using reflection
 */
object Objects extends Logging {

  /**
   * Instantiates the given object class using the possible list of values to be injected.
   *
   * Implements a really simple IoC mechanism. Ideally we'd improve this to support JSR330 style
   * better injection with annotated injection points or such like
   */
  def instantiate[T](clazz: Class[T], injectionValues: List[AnyRef] = Nil): T = {
    def argumentValue(paramType: Class[_]): Option[AnyRef] =
      injectionValues.find(_.getClass.isAssignableFrom(paramType))

    def create(c: Constructor[_], args: Array[AnyRef] = Array()): T = {
      val answer = if (args.isEmpty) {
        clazz.newInstance
      } else {
        debug("About to call constructor: " + c + " on " + clazz.getName + " with args: " + args.toList)
        c.newInstance(args:_*)
      }
      answer.asInstanceOf[T]
    }

    def tryCreate(c: Constructor[_]): Option[T] = {
      val options = c.getParameterTypes.map(argumentValue(_))
      if (options.forall(_.isDefined)) {
        val args = options.map(_.get).toArray
        Some(create(c, args))
      } else {
        None
      }
    }

    val constructors = clazz.getConstructors.sortBy(_.getParameterTypes.size * -1)
    constructors.view.map(c => tryCreate(c)).find(_.isDefined) match {
      case Some(Some(answer)) => answer
      case _ =>
        // lets default to zero arg
        clazz.newInstance.asInstanceOf[T]
    }
  }
}