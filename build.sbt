


name := "scala-big-data-spark"
version := "0.1"
organization := "com.yunjae"
scalaVersion := "2.11.12"

resolvers ++= Seq(
  "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases" at "http://oss.sonatype.org/content/repositories/releases",
  "maven central" at "http://central.maven.org/maven2/"
)

scalacOptions in Compile ++= Seq(
  "-encoding", "UTF-8",
  "-target:jvm-1.8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlog-reflective-calls",
  "-Xlint")

javacOptions in Compile ++= Seq(
  "-source", "1.8",
  "-target", "1.8",
  "-Xlint:unchecked",
  "-Xlint:deprecation")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.apache.spark" %% "spark-core" % "2.3.0" % "provided",
  "commons-logging" % "commons-logging" % "1.2"
) map (_.excludeAll(
  ExclusionRule(organization = "org.slf4j"),
  ExclusionRule(organization = "log4j")//,
  //ExclusionRule(organization = "javax.servlet")
))

//libraryDependencies ++= Seq(
//  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
//  "org.apache.spark" %% "spark-core" % "2.2.1" % "provided",
//  "org.apache.spark" %% "spark-streaming" % "2.3.0" % "provided",
//  "org.apache.spark" %% "spark-streaming-kinesis-asl" % "2.3.0" % "provided",
//  "org.apache.spark" %% "spark-sql" % "2.3.0" % "provided",
//  //"com.holdenkarau" %% "spark-testing-base" % "2.3.0_0.9.0" % Test,
//  "commons-logging" % "commons-logging" % "1.2"
//) map (_.excludeAll(
//  ExclusionRule(organization = "org.slf4j"),
//  ExclusionRule(organization = "log4j"),
//  ExclusionRule(organization = "javax.servlet")
//))


libraryDependencies ++= Seq(("org.slf4j" % "slf4j-log4j12" % "1.7.25")
  .excludeAll(ExclusionRule(organization = "log4j")))

libraryDependencies += "log4j" % "log4j" % "1.2.17" % "test"

val excludedFiles = Seq("pom.xml", "pom.properties", "manifest.mf", "package-info.class")

assemblyMergeStrategy in assembly := {
  case PathList("org","aopalliance", xs @ _*) => MergeStrategy.last
  case PathList("javax", "inject", xs @ _*) => MergeStrategy.last
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
  case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}



/* including scala bloats your assembly jar unnecessarily, and may interfere with
   spark runtime */
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

assemblyJarName in assembly := "scala-big-data-spark.jar"

/* you need to be able to undo the "provided" annotation on the deps when running your spark
 programs locally i.e. from sbt; this bit reincludes the full classpaths in the compile and run tasks. */
fullClasspath in Runtime := (fullClasspath in (Compile, run)).value

mainClass in assembly := Some("com.yunjae.spark.task.FindTopPhrasesJob")

