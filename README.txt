This is Logdoc, a tool for managing log messages for Java code, in a contract-
first manner. 

This software is available under the terms of a BSD-style license, see
the accompanied COPYRIGHT file.


EXAMPLE

Example Logdoc log.xml file:

   <?xml version="1.0"?>
   <log since="MyLibrary 5.5.3" domain="com.znerd.mylibrary" public="true">
      <translation-bundle locale="en_US" />
      <group id="transactions" name="Transactions">
         <entry id="1102" level="NOTICE">
            <description>Transaction starting.</description>
            <param name="id"          type="int32" nullable="false"  />
            <param name="description" type="text"  nullable="true"  />
         </entry>
         <entry id="1103" level="ERROR" exception="true">
            <description>Transaction failed.</description>
            <param name="id" type="int32" nullable="false"  />
         </entry>
      </group>
   </log>

Example associated translation-bundle-en_US.xml file:

   <?xml version="1.0"?>
   <translation-bundle>
      <translation entry="1102">Starting transaction <value-of-param name="id" />. Description: <value-of-param name="description" format="quoted" />.</translation>
      <translation entry="1103">Transaction <value-of-param name="id" /> failed.</translation>
   </log>

Such input files will then be used by Logdoc to generate Java source code and
HTML documentation.


USAGE WITH APACHE ANT

To trigger Logdoc from your Ant build script, first define the tasks:

   <taskdef resource="org/znerd/logdoc/ant/antlib.xml">
      <classpath>
          <pathelement path="lib/logdoc-core.jar" />
          <pathelement path="lib/logdoc-ant-tasks.jar" />
      </classpath>
   </taskdef>

Then the tasks can, for example, be used as follows:

   <logdoc-code in="src/logdoc" out="src/java" />
   <logdoc-doc in="src/logdoc" out="build/logdoc-htdocs" />


USAGE WITH APACHE MAVEN

To trigger the Maven plugin from your Maven-based project, declare the plugin in your POM:

  <build>
    <plugins>
      <plugin>
        <groupId>org.znerd</groupId>
        <artifactId>logdoc-maven-plugin</artifactId>
        <version>0.20</version>
        <executions>
          <execution>
            <phase>generate-sources</phase>
            <goals>
              <goal>java</goal>
            </goals>
            <configuration>
              <loggingFramework>log4j</loggingFramework>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

Instead of 'log4j', you can also configure 'atg' as a log framework backend.


MODULES

Logdoc consists of the following modules:

   logdoc-core         - Logdoc base classes, in the org.znerd.logdoc package.

   logdoc-ant-tasks    - Apache Ant tasks that make the Logdoc functionality
                         available for Ant build scripts.

   logdoc-maven-plugin - Maven plugins that make the Logdoc functionality
                         available for Maven projects.


BUILDING LOGDOC

To build Logdoc self, use Maven and execute:

   mvn package

Under the target/ directories (of the respective modules) files will be
generated.


HISTORY

Logdoc used to be part of XINS, see:

   http://www.xins.org/

It has been factored out of XINS by Ernst de Haan.


BUG REPORTS AND FEATURE REQUESTS

If you want to file a bug report or a feature request, please do so here:

   http://github.com/znerd/logdoc/issues
