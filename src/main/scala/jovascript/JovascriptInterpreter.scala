package jovascript

import jovascript.parser._
import jovascript.parser.gen._
import org.antlr.v4.runtime._

object JovascriptInterpreter {
  def main(args: Array[String]): Unit = scala.io.Source.stdin.getLines().foreach {
    input => parse(input).fold(
      error => println(error),
      ast => println(ast))
  }

  private def parse(input: String): Either[String, AST] = {

    val inputStream = new ANTLRInputStream(input)
    val lexer = new JovascriptLexer(inputStream)
    val tokens = new CommonTokenStream(lexer)
    val parser = new JovascriptParser(tokens)

    val errorListener = new JovascriptErrorListener()
    parser.removeErrorListeners()
    parser.addErrorListener(errorListener)
    lexer.removeErrorListeners()
    lexer.addErrorListener(errorListener)

    val parseTree = parser.program()

    errorListener.errors match {
      case None => Right(JovascriptASTVisitor.visit(parseTree))
      case Some(errorMessage) => Left(errorMessage)
    }
  }
}
