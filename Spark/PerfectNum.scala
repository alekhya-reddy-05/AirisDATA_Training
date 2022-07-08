object PerfectNum {
  def isPerfectSquare(n:Int):Boolean ={
    for(i<-1 to n/2){
      if(i*i==n)
        return true
    }
    false
  }
  def main(args: Array[String]) {
    println("Enter size of the array: ")
    val n= scala.io.StdIn.readInt()
    var cnt=0
    println("Enter array elements")
    var str=scala.io.StdIn.readLine()
    var a1 :Array[String]=str.split(" ")
    var a:Array[Int]=a1.map(x=>x.toInt)
    for(i<-a) {
      if(isPerfectSquare(i))
        cnt+=1
    }
    println("The number of customers that will be given discount are "+cnt)
  }
}
