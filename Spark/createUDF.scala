import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object createUDF extends App{

  def checkAge(age:Int)={
    if(age>18 ) "Y" else "N"
  }
  var sparkConf=new SparkConf()
  sparkConf.setAppName("First App")
  sparkConf.setMaster("local[2]")

  var spark=SparkSession.builder()
    .config(sparkConf)
    //    .enableHiveSupport()
    .getOrCreate()

  //READ
  val df1=spark.read
    .format("csv")
    .option("inferSchema",true)
    .option("header",true)
    .option("path","C:\\Users\\Alekhya Reddy\\Desktop\\readData.csv")
    .load()
  df1.printSchema()
  df1.show();

  //object expression
  val parseAgeFunc=udf(checkAge(_:Int):String)
  val df2=df1.withColumn("Adult(Y/N)",parseAgeFunc(col("Age")))
  df2.show()

  //String/SQL expression
  spark.udf.register("parseAgeFunction",checkAge(_:Int):String)
  val df3=df1.withColumn("Adult(Y/N)",expr("parseAgeFunction(age)"))
  df3.show()

  spark.catalog.listFunctions().filter(x=>x.name=="parseAgeFunction").show()
  df1.createOrReplaceTempView("People")
  spark.sql("select *,parseAgeFunction(age) as adult from People").show()

}
