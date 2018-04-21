# Spack Server Application packagging and loading

## build.sbt setting
### scala option 
scalacOptions in Compile ++= Seq(
  "-encoding", "UTF-8",
  "-target:jvm-1.8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlog-reflective-calls",
  "-Xlint")
  
### java option
javacOptions in Compile ++= Seq(
  "-source", "1.8",
  "-target", "1.8",
  "-Xlint:unchecked",
  "-Xlint:deprecation")
  
### library dependenceies  
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",   // test execution 
  "org.apache.spark" %% "spark-core" % "2.3.0" % "provided",  
  "commons-logging" % "commons-logging" % "1.2"
) map (_.excludeAll(
  ExclusionRule(organization = "org.slf4j"),
  ExclusionRule(organization = "log4j"),
  //ExclusionRule(organization = "javax.servlet")   // it is necessary for that is test code execution environment
))  

libraryDependencies ++= Seq(("org.slf4j" % "slf4j-log4j12" % "1.7.25")
  .excludeAll(ExclusionRule(organization = "log4j")))

libraryDependencies += "log4j" % "log4j" % "1.2.17" % "test"

### execution environment
val excludedFiles = Seq("pom.xml", "pom.properties", "manifest.mf", "package-info.class")
mainClass in assembly := Some("com.yunjae.spark.task.FindTopPhrasesJob")

## assembly environment
// library class duplicated prevent
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

## add sbt-assembly as a dependency in project/plugins.sbt
resolvers += Resolver.url("artifactory", url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.eed3si9n" %% "sbt-assembly" % "0.14.5")

## spark application packagging
sbt assembly

## sparks-submit shell
#!/bin/bash

usage(){
	echo "Usage: $0 --pathsToTextToAnalyze [path_1] [path_2] [path_n-1] [path_n]"
	exit 1
}

if [[ "$#" -lt 2 ]]; then
    usage
fi

yarn_queue=$2

echo "starting job for yarn_queue : $yarn_queue"

spark-submit \
--class com.yunjae.spark.task.FindTopPhrasesJob \
--master local[*] \
target/scala-2.11/scala-big-data-spark*.jar $@


