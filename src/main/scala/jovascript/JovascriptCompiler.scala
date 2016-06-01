package jovascript

import parser.JovascriptParser

object JovascriptCompiler {
  def main(args: Array[String]): Unit = {

    val inputText = "def x: Number = 1;"

    JovascriptParser.parse(inputText) match {
      case Left(error) => println(error)
      case Right(ast) => println(ast)
    }
  }
}
