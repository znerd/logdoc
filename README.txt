This is Logdoc, a tool for managing log messages for Java code, in a contract-
first manner. 

This software is available under the terms of a BSD-style license, see
the accompanied COPYRIGHT file.


EXAMPLE

Example Logdoc log.xml file:

   <log since="MyLibrary 5.5.3" domain="com.znerd.mylibrary" public="true">
      <translation-bundle locale="en_US" />
      <group id="transactions" name="Transactions">
         <entry id="102" level="NOTICE">
            <description>Transaction starting.</description>
            <param name="id"          type="int32" nullable="false"  />
            <param name="description" type="text"  nullable="true"  />
         </entry>
         <entry id="103" level="INFO">
            <description>Transaction fully started.</description>
            <param name="id" type="int32" nullable="false"  />
         </entry>
      </group>
   </log>

Input files can be used to generate Java source code and HTML documentation.


USAGE WITH APACHE ANT

To trigger Logdoc from your Ant build script, first define the tasks:

   <taskdef resource="org/znerd/logdoc/ant/antlib.xml" classpath="lib/logdoc.jar" />

Then the tasks can, for example, be used as follows:

   <logdoc-code in="src/logdoc" out="src/java" />
   <logdoc-html in="src/logdoc" out="build/logdoc-htdocs" />


USAGE WITH APACHE MAVEN

(TODO: Write this part)


MODULES

Logdoc consists of the following modules:

   base          - Logdoc base classes, in the org.znerd.logdoc package.

   ant-tasks     - Apache Ant tasks that make the Logdoc functionality
                   available for Ant build scripts.

   maven-plugins - Maven plugins that make the Logdoc functionality available
                   for Maven projects.


BUILDING LOGDOC

To build Logdoc self, use Maven and execute:

   mvn package

Under the target/ directories (of the respective modules) files will be
generated.


HISTORY

Logdoc used to be part of XINS, see:

   http://www.xins.org/

It has been factored out of XINS by me.


BUG REPORTS AND FEATURE REQUESTS

If you want to file a bug report or a feature request, please do so here:

   http://github.com/znerd/logdoc/issues


--
Ernst de Haan
ernst@ernstdehaan.com
