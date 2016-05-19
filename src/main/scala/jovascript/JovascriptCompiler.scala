package jovascript

import jovascript.parser.{JovascriptASTVisitor, JovascriptParser, JovascriptLexer}
import org.antlr.v4.runtime._

object JovascriptCompiler {
  def main(args: Array[String]): Unit = {

    val inputText = "def x: Number = 1;"

    val input = new ANTLRInputStream(inputText)

    val lexer = new JovascriptLexer(input)

    val tokens = new CommonTokenStream(lexer)

    val parser = new JovascriptParser(tokens)

    val astVisitor = new JovascriptASTVisitor
    val ast = astVisitor.visit(parser.program())

    println(ast)
  }
}
