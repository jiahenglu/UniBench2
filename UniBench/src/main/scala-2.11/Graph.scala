import org.apache.spark.sql.SparkSession

/**
 * Created by chzhang on 19/06/2017.
 */
object Graph {
  // Define the schema of product('asin','title','categories','price','imgUrl','description')
  def CreateGraph(spark: SparkSession) = {
    val KnowsGraph = "../ldbc_snb_datagen/test_data/social_network/person_knows_person_0_0.csv"
    val Post = "../ldbc_snb_datagen/test_data/social_network/post_0_0"
    //val Post_hasCreateor_person="../ldbc_snb_datagen/test_data/social_network/person_knows_person_0_0.csv"
    val Person_likes_Post = "../ldbc_snb_datagen/test_data/social_network/person_likes_post_0_0.csv"
    val post_hasTag_tag = "../ldbc_snb_datagen/test_data/social_network/post_hasTag_tag_0_0.csv"

    val GraphDF = spark.read.option("header", "true").option("delimiter", "|").csv(KnowsGraph)
    GraphDF
  }
}
