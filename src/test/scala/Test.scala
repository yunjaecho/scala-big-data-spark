object Test extends App {
  val lines = List("a", "b", "c")

  val lineTrans = lines.foldLeft(Map.empty[String, Int]) {
    (count, word) =>
      count + (word -> (count.getOrElse(word, 0) + 1))
  }


  val mapVal = Map.empty[String, Integer]
  val mapVal2 = mapVal + ("a" -> 1)
  println(mapVal2)

}
