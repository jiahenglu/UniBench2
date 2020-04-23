import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}

/**
 * Created by chzhang on 19/06/2017.
 */
object Vendor {
  def GenerateVendor(spark: SparkSession) = {
    // Define the schema of vendor (title,country,industry)
    val vendor_name = StructField("Vendor", DataTypes.StringType)
    val country_name = StructField("Country", DataTypes.StringType)
    val cv = StructField("cv", DataTypes.DoubleType)
    val industry = StructField("Industry", DataTypes.StringType)
    val schema_vendor = StructType(Array(vendor_name, country_name, cv, industry))

    // Table for vendor
    val VendorCatalog = "src/main/resources/PopularSportsBrandByCountry.csv"
    val VendorDF = spark.read.option("header", "false").option("delimiter", ",").option("mode", "DROPMALFORMED").schema(schema_vendor).csv(VendorCatalog)
    VendorDF.select("Vendor", "Country", "Industry").write.option("header", "true").csv("Table_Vendor")
  }
}
