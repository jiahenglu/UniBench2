import net.sansa_stack.rdf.spark.io._
import net.sansa_stack.rdf.spark.model._
import org.apache.jena.riot.Lang
import org.apache.spark.sql.SparkSession

object RDF {
  def Create(spark: SparkSession) {
    val baseGraph = spark.rdf(Lang.NTRIPLES)("src/main/resources/dbpedia/triple-base.nt")
    val extraGraph = spark.rdf(Lang.NTRIPLES)("src/main/resources/dbpedia/triple-extra.nt.[0-9]*.gz")

    println(baseGraph.getSubjects().count())
    println(extraGraph.getSubjects().count())
  }
}