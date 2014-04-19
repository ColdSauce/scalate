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

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ConfigMap, BeforeAndAfterAll, FunSuite}
import java.io.File
import java.lang.String
import collection.immutable.Map
import util.Log
import scuery.XmlHelper._
import xml.NodeSeq
import org.slf4j.LoggerFactory

/**
 * @version $Revision : 1.1 $
 */
@RunWith(classOf[JUnitRunner])
abstract class FunSuiteSupport extends FunSuite with Log with  BeforeAndAfterAll {

  protected var _basedir = "."

  /**
   * Returns the base directory of the current project
   */
  def baseDir = new File(_basedir)


  override protected def beforeAll(map: ConfigMap): Unit = {
    _basedir = map.get("basedir") match {
      case Some(basedir) => basedir.toString
      case _ => System.getProperty("basedir", ".")
    }
    debug("using basedir: %s", _basedir)
  }


  def assertSize(selector: String, result: NodeSeq, expected: Int): Unit = {
    expect(expected, "number of elements matching: " + selector) {result.$(selector).size}
  }

  /**
   * Asserts that the text value of the given selector matches the expected string
   */
  def assertText(selector: String, result: NodeSeq, expected: String): Unit = {
    expect(expected, "text of elements matching: " + selector) {result.$(selector).text}
  }

  def assertType(anyRef: AnyRef, expectedClass: Class[_]): Unit = {
    assert(anyRef != null, "expected instance of " + expectedClass.getName)
    expect(expectedClass) {anyRef.getClass}
  }
}