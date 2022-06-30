import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

object Customer extends App {
  Logger.getLogger("org").setLevel(Level.ERROR)
  val sc=new SparkContext("local[*]","wordcount")
  val input = sc.textFile("C:\\Users\\Alekhya Reddy\\Downloads\\customerorders.csv")
  val mappedInput=input.map(x=>(x.split(",")(0),x.split(",")(2).toFloat))
  val totalAmtSpentByCustomer=mappedInput.reduceByKey((x,y)=>x+y)
  val res=totalAmtSpentByCustomer.sortBy(x=>x._2,false)
  res.take(10).foreach(println)
}
