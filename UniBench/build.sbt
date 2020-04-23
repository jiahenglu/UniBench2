name := "test"

version := "1.0"

scalaVersion := "2.11.12"

val sparkVersion ="2.1.3"
resolvers += "Spark Packages Repo" at "https://dl.bintray.com/spark-packages/maven"
resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += "deps of spark-indexedrdd" at "https://github.com/ankurdave/maven-repo/raw/master"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "harsha2010" % "magellan" % "1.0.4-s_2.11",
  "org.scalanlp" %% "breeze-natives" % "0.11.2",
  "org.scalanlp" %% "breeze-viz" % "0.11.2",
  "com.databricks" %% "spark-xml" % "0.4.1",
  "com.github.bruneli.scalaopt" % "scalaopt-core_2.10" % "0.1",
  "amplab" % "spark-indexedrdd" % "0.3"
)

