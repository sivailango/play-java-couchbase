name := """play-java-couchbase-sample"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "com.smartcoders" % "play-java-couchbase_2.11" % "1.0",
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "3.3.4",
  "org.webjars" % "jquery" % "1.9.1"
)

resolvers += Resolver.url("PlayJavaCouch Wrapper", url("http://sivailango.github.io/releases/"))(Resolver.ivyStylePatterns)
