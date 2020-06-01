import net.sansa_stack.rdf.spark.io._
import net.sansa_stack.rdf.spark.model._
import org.apache.jena.graph
import org.apache.jena.graph.NodeFactory
import org.apache.jena.riot.Lang
import org.apache.spark.sql.SparkSession
import org.json4s.JsonAST.{JObject, JString}
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

object RDFSimplified {
  def Create(spark: SparkSession): Unit = {
    val TOTAL_LEVELS = 3

    var collected = spark.sparkContext.emptyRDD[graph.Triple]

    for (level <- 0 until TOTAL_LEVELS) {
      val p = scala.math.pow(0.5, level)

      val nodes = spark.sparkContext
        .textFile(s"src/main/resources/dbpedia/level$level.txt.bz2")
        .sample(false, p)
        .collect
        .toSet

      println(s"level $level has sampled ${nodes.size} nodes")

      var triples =
        spark.rdf(Lang.NTRIPLES)(s"src/main/resources/dbpedia/level$level.nt.*")
          .filterSubjects(n => nodes.contains(n.getURI))

      println(s"level $level has sampled ${triples.count} triples")

      collected = collected.union(triples)
    }

    // output N-TRIPLES directly

    //collected
    //  .repartition(1)
    //  .saveAsTextFile(spark.conf.get("rdf"))
    // return

    // output JSON

    val nodes = collected
      .getSubjects()
      .filter(_.isURI)
      .union(collected.getObjects().filter(_.isURI))
      .distinct()
      .zipWithUniqueId()
      .map(n => new graph.Triple(n._1, NodeFactory.createURI("http://schema.org/identifier"), NodeFactory.createLiteral(n._2.toString)))

    val attributes = collected
      .union(nodes)
      .filter(t => !t.getObject.isURI)
      .groupBy(_.getSubject)
      .mapValues(_.groupBy(_.getPredicate))
      .mapValues(_.mapValues(_.map(_.getMatchObject)))

    val edges = collected
      .filter(t => t.getObject.isURI)
      .groupBy(_.getSubject)
      .mapValues(_.groupBy(_.getPredicate))
      .mapValues(_.mapValues(_.map(_.getMatchObject)))


    val json_attributes = attributes
      .map {
        case (sub, attrs) =>
          var j = new JObject(List(("@id", JString(sub.toString))))

          attrs.foreach(attr =>
            if (attr._2.size == 1)
              j ~= (attr._1.getURI, attr._2.head.getLiteralLexicalForm)
            else
              j ~= (attr._1.getURI, attr._2.map(o => o.getLiteralLexicalForm))
          )

          compact(render(j))
      }

    val json_edges =
      edges.flatMap {
        case (from, egs) =>
          egs.flatMap {
            case (pred, tos) =>
              tos.map(to => compact(render(("@subject", from.getURI) ~ ("@object", to.getURI) ~ ("@predicate", pred.getURI))))
          }
      }

    json_attributes.repartition(1).saveAsTextFile(spark.conf.get("rdf") + "_nodes")
    json_edges.repartition(1).saveAsTextFile(spark.conf.get("rdf") + "_edges")
  }
}