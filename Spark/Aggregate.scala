import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

object Aggregate extends App{
  Logger.getLogger("org").setLevel(Level.ERROR)
  val sc=new SparkContext("local[*]","wordcount")
  val input=sc.textFile("C:\\Users\\Alekhya Reddy\\Downloads\\search_data.txt")
  val words=input.flatMap(x=>x.split(" "))
  val wordMap=words.map(x=>(x,1))
  //alphabetical order of keys with their count
  //val aggregate=wordMap.reduceByKey((x,y)=>x+y).sortByKey()
  //to swap keys with values and then sort
  val aggregate=wordMap.reduceByKey((x,y)=>x+y).map(x=>(x._2,x._1)).sortByKey().map(x=>(x._2,x._1))
  //to get in descending order of values
  //val aggregate=wordMap.reduceByKey((x,y)=>x+y).map(x=>(x._2,x._1)).sortByKey(false).map(x=>(x._2,x._1))
  aggregate.collect.foreach(println)
}
