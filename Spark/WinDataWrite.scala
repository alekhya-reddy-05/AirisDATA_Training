import org.apache.spark.sql.{SaveMode, SparkSession}
object WinDataWrite extends App{

  case class Customer(country:String,weeknum:Int,numinvoices:Int,totalquantity:Int,invoicevalue:Double)
  val spark = SparkSession
    .builder.appName("Window Data")
    .master("local[*]")
    .getOrCreate()

  val df1 = spark.read
    .option("inferSchema","true")
    .csv("C:\\Users\\Alekhya Reddy\\Downloads\\windowdata.csv")

  // adding column names to the data
  val dfwithheaders = df1.toDF("country", "weeknum", "numinvoices", "totalquantity", "invoicevalue");

  // Now displaying the data and schema
  dfwithheaders.show(10,false);
  dfwithheaders.printSchema();

  // Reading the data with headers
  val dfHeader = spark.read
    .option("header","true")
    .option("inferSchema","true")
    .csv("C:\\Users\\Alekhya Reddy\\Downloads\\windowdata_withHeaders.csv")

  //convert dataframe to ds
  import spark.implicits._
  val dsHeader = dfHeader.as[Customer]

  dsHeader.show(10,false);

  // Each folder country, week num combination - parquet
  dfwithheaders.write
    .mode("overwrite")
    .format("parquet")
    .partitionBy("country","weeknum")
    .option("path","C:\\Users\\Alekhya Reddy\\Desktop\\Sample1")
    .save()

  //Each folder country -Avro format
  dfwithheaders.write
    .mode("overwrite")
    .format("avro")
    .partitionBy("country")
    .option("path","C:\\Users\\Alekhya Reddy\\Desktop\\Sample2")
    .save()
  spark.close();
}