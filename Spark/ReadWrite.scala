//SPARK SESSION
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object ReadWrite extends App{
  var sparkConf=new SparkConf()
  sparkConf.setAppName("First App")
  sparkConf.setMaster("local[2]")

  var spark=SparkSession.builder()
    .config(sparkConf)
//    .enableHiveSupport()
    .getOrCreate()

  //READ
  val df=spark.read
    .format("csv")
    .option("inferSchema",true)
    .option("header",true)
    .option("path","C:\\Users\\Alekhya Reddy\\Desktop\\readData.csv")
    .load()
  df.printSchema()
  df.show();
  //WRITE
  df.write
    .format("csv")
    .option("header",true)
    .save("C:\\Users\\Alekhya Reddy\\Desktop\\write")
 spark.stop()
}
