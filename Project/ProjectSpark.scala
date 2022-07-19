import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
case class Rental_data(id : Int, property_type : String, room_type : String,bathrooms: Double , bedrooms : Int ,minimum_nights : Int, location_id : Int,price : Int)
case class Location_data(id:Int , city:String , state:String , country:String , pincode:Int)
object ProjectSpark extends App {

  def getDiscount(price :Int):Double ={
    //To calculate discount
    if(price>=200 && price<300){
      return "%06.2f".format(price*0.05).toDouble
    }else if(price>=300 && price<500){
      return "%06.2f".format(price*0.10).toDouble
    }else if(price>=500 && price<1000){
      return "%06.2f".format(price*0.15).toDouble
    }else if(price>=1000){
     return "%06.2f".format(price*0.20).toDouble
    }else{
     return 0.0;
    }
  }
  val sparkConf=new SparkConf()
  sparkConf.setAppName("First App")
  sparkConf.setMaster("local[2]")

  val spark=SparkSession.builder()
    .config(sparkConf)
    //  .enableHiveSupport()
    .getOrCreate()

  val rentalSchema = new StructType()
    .add("id",IntegerType,nullable = false)
    .add("property_type",StringType,nullable = false)
    .add("room_type",StringType,nullable = false)
    .add("bathrooms",DoubleType,nullable = false)
    .add("bedrooms",IntegerType,nullable = false)
    .add("minimum_nights",IntegerType,nullable = false)
    .add("location_id",IntegerType,nullable = false)
    .add("price",IntegerType,nullable = false)

  //Reading rental_data file
  val df_rental=spark.read
    .format("csv")
    .schema(rentalSchema)
    .option("path","C:\\Users\\Alekhya Reddy\\Downloads\\Rental_data.csv")
    .load()

  //Adding column names
  val df_columns_rental = df_rental.toDF("id","property_type","room_type","bathrooms","bedrooms","minimum_nights","location_id","price")
  df_columns_rental.printSchema()

  //Reading location file
  val df_location=spark.read
    .format("csv")
    .option("inferSchema",true)
    .option("path","C:\\Users\\Alekhya Reddy\\Downloads\\Location.csv")
    .load()

  //Adding column names
  val df_columns_location = df_location.toDF("id","city","state","country","pincode")
  df_columns_location.printSchema();


  //creating string expression udf for calculating discount
  spark.udf.register("discountFunction",getDiscount(_:Int):Double)
  val df_rental_discount=df_columns_rental.withColumn("Discount",expr("discountFunction(price)"))
  df_rental_discount.show()

  //Adding column price after discount
  val df_rental_final=df_rental_discount.withColumn("FinalPrice",col("price")-col("Discount"))
  df_rental_final.show()

  //Dataframe to Dataset
  import spark.implicits._
  val ds_rental=df_columns_rental.as[Rental_data1]
  ds_rental.show();
  ds_rental.filter(x=>x.minimum_nights>15).show()

  //Query to find the states that have rentals in more than 1 city
  df_columns_location.createOrReplaceTempView("loc")
  spark.sql("select state,count(state) as no_of_cities from loc group by state having count(state)>1 ").show()

  //JOINS-  Displaying minimum, maximum and average price, city of rentals in each location.
  df_rental_final.createOrReplaceTempView("rental")
  spark.sql("select r.location_id,l.city,l.state,l.pincode,count(r.location_id) from rental r join loc l on r.location_id=l.id group by r.location_id,l.city,l.state,l.pincode order by r.location_id").show()

  spark.sql("select id,property_type,room_type,price,Discount,FinalPrice from rental where Discount>=100").show()


}
