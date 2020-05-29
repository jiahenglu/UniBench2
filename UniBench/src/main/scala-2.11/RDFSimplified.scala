import org.apache.spark.sql.SparkSession

object RDFSimplified {
  def Create(spark: SparkSession): Unit = {
    val TOTAL_LEVELS = 3

    var collected = spark.sparkContext.emptyRDD[String]

    for (level <- 0 until TOTAL_LEVELS) {
      val p = scala.math.pow(0.5, level)

      val nodes = spark.sparkContext
        .textFile(s"src/main/resources/dbpedia/level$level.txt.bz2")
        .sample(false, p)
        .collect
        .toSet

      println(s"level $level has sampled ${nodes.size} nodes")

      var triples = spark.sparkContext
        .textFile(s"src/main/resources/dbpedia/level$level.nt.*")
        .filter(l => {
          val seg = l.split(" ", 2)
          nodes.contains(seg(0).substring(1, seg(0).length - 1))
        })

      println(s"level $level has sampled ${triples.count} triples")

      collected = collected.union(triples)
    }

    collected
      .repartition(1)
      .saveAsTextFile(spark.conf.get("rdf"))
  }
}