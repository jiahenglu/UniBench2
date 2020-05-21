name := "test"

version := "1.0"

scalaVersion := "2.11.11"
val sparkVersion = "2.4.3"

val varscalaVersion = "2.11.11"
val varscalaBinaryVersion = "2.11"

// SANSA
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.8.7"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.7"
dependencyOverrides += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.7"
dependencyOverrides += "org.apache.jena" % "jena-osgi" % "3.11.0"

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

// | SANSA Layers
resolvers ++= Seq(
  "AKSW Maven Releases" at "https://maven.aksw.org/archiva/repository/internal",
  "AKSW Maven Snapshots" at "https://maven.aksw.org/archiva/repository/snapshots",
  "oss-sonatype" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Apache repository (snapshots)" at "https://repository.apache.org/content/repositories/snapshots/",
  "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  ("NetBeans" at "http://bits.netbeans.org/nexus/content/groups/netbeans/").withAllowInsecureProtocol(true),
  "gephi" at "https://raw.github.com/gephi/gephi/mvn-thirdparty-repo/",
  Resolver.defaultLocal,
  Resolver.mavenLocal,
  "Apache Staging" at "https://repository.apache.org/content/repositories/staging/",
  "jitpack" at "https://jitpack.io"
)

libraryDependencies ++= Seq(
  "net.sansa-stack" %% "sansa-rdf-spark" % "0.6.0",
  "net.sansa-stack" %% "sansa-owl-spark" % "0.6.0",
  "net.sansa-stack" %% "sansa-inference-spark" % "0.6.0",
  "net.sansa-stack" %% "sansa-query-spark" % "0.6.0",
  "net.sansa-stack" %% "sansa-ml-spark" % "0.6.0",
)