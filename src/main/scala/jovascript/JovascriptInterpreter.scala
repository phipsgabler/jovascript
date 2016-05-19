package jovascript

import jovascript.parser.{JovascriptASTVisitor, JovascriptLexer, JovascriptParser}
import org.antlr.v4.runtime._

object JovascriptInterpreter {
  def main(args: Array[String]): Unit = {

    scala.io.Source.stdin.getLines().foreach {
      input => {
        val inputStream = new ANTLRInputStream(input)
        val lexer = new JovascriptLexer(inputStream)
        val tokens = new CommonTokenStream(lexer)
        val parser = new JovascriptParser(tokens)
        val ast = JovascriptASTVisitor.visit(parser.program())

        println(ast)
      }
    }
  }
}
