name := """Watson Demo"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "3.3.4",
  "mysql" % "mysql-connector-java" % "5.1.35",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.webjars" % "font-awesome" % "4.3.0-1",
  "edu.stanford.nlp" % "stanford-parser" % "3.5.0",
  "edu.stanford.nlp" % "stanford-parser" % "3.5.0" classifier "models",
  "com.google.inject" % "guice" % "4.0-beta5",
  "commons-collections" % "commons-collections" % "3.2.1",
  "org.apache.lucene" % "lucene-snowball" % "3.0.3"
)

resolvers ++= Seq(
    "jBCrypt Repository" at "http://repo1.maven.org/maven2/org/"
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)

