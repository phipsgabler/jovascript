package jovascript

import jovascript.parser.JovascriptASTVisitor
import jovascript.parser.gen.{JovascriptParser, JovascriptLexer}
import org.antlr.v4.runtime._

object JovascriptCompiler {
  def main(args: Array[String]): Unit = {

    val inputText = "def x: Number = 1;"

    val input = new ANTLRInputStream(inputText)

    val lexer = new JovascriptLexer(input)

    val tokens = new CommonTokenStream(lexer)

    val parser = new JovascriptParser(tokens)

    val ast = JovascriptASTVisitor.visit(parser.program())

    println(ast)
  }
}
