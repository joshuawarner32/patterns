package pattern;

import pattern.reducer.simple.SimpleReducer;

val reducer = SimpleReducer()
 
fun main(args: Array<String>) {
  val ns = Namespace()
  val reader = LineReader()

  while(true) {
    val line = reader.readLine()
    try {
      val value = Parser.parse(ns, line)
      val res = reducer.reduce(value)
      print(res)
    } catch(e : ParseException) {
      print("Parse error: ${e.getMessage()}")
    }
  }
}