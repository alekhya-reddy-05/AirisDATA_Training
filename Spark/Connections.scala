import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

object Connections extends App{
  def extract(row:String)={
    val (a,b)=(row.split(",")(2).toInt,row.split(",")(3).toInt)
    (a,(b,1))
  }
  Logger.getLogger("org").setLevel(Level.ERROR)
  val sc=new SparkContext("local[*]","wcc")
  val input = sc.textFile("C:\\Users\\Alekhya Reddy\\Downloads\\friendsdataNew.txt")
  val mappedInput=input.map(extract)
  val totalConnByAge=mappedInput.reduceByKey((x,y)=> (x._1+y._1,x._2+y._2))
  val avgByAge=totalConnByAge.mapValues(x=>(x._1/x._2))
  avgByAge.collect.foreach(println)

}
