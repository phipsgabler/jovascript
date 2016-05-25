package jovascript

import jovascript.parser.JovascriptParser

object JovascriptInterpreter {
  def main(args: Array[String]): Unit = scala.io.Source.stdin.getLines().foreach {
    input => JovascriptParser.parse(input).fold(
      error => println(error),
      ast => println(ast))
  }
}
