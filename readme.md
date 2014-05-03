![Scalate][logo]
===============================

Scalate is a [Scala](http://www.scala-lang.org) based template engine which supports HAML, Mustache and JSP, Erb and Velocity style syntaxes.

The following template languages are supported:

  * [Mustache](http://scalate.fusesource.org/documentation/mustache.html#features): is a Mustache template language for Java/Scala
  * [Jade](http://scalate.fusesource.org/documentation/scaml-reference.html#jade): The Jade style of Haml/Scaml template lanaguage
  * [Scaml](http://scalate.fusesource.org/documentation/scaml-reference.html#features): Provides Haml style template lanaguage
  * [Ssp](http://scalate.fusesource.org/documentation/ssp-reference.html#syntax): Provides a JSP, Erb and Velocity style template language

In Scaml and SSP all expressions are typesafe and checked at edit/compile time to ensure you don't leave any mistakes in your pages.
Mustache uses dynamic typing and reflection; a trade off of hiding code and logic from inside the templates.

Building
--------

To build Scalate from the source please see the [building instructions](http://scalate.fusesource.org/building.html)

Current version
---------------

The current version of Scalate is 1.7.0, available for Scala 2.10.x 
and 2.11.x.

    libraryDependencies += "org.scalatra.scalate" %% "scalate-core" % "1.7.0"

Links
-----

* [Home](http://scalate.fusesource.org/)
* [Community](http://scalate.fusesource.org/community.html)
* [Documentation](http://scalate.fusesource.org/documentation/)
* [Issue Tracker](http://scalate.assembla.com/spaces/scalate/tickets)

[logo]: http://scalate.fusesource.org/images/project-logo.png "Scalate"
