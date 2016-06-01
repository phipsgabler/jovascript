package jovascript.parser

import jovascript.parser.gen.{JovascriptParser => Parser, JovascriptLexer}
import org.antlr.v4.runtime._

object JovascriptParser {
  def parse(input: String): Either[String, AST] = {
    println(input)

    val inputStream = new ANTLRInputStream(input)
    val lexer = new JovascriptLexer(inputStream)
    val tokens = new CommonTokenStream(lexer)
    val parser = new Parser(tokens)

    println("adf")

    val errorListener = new JovascriptErrorListener()
    parser.removeErrorListeners()
    parser.addErrorListener(errorListener)
    lexer.removeErrorListeners()
    lexer.addErrorListener(errorListener)

    println("adf")

    val parseTree = parser.program()

    println("adf")

    errorListener.errors match {
      case None => Right(JovascriptASTVisitor.visit(parseTree))
      case Some(errorMessage) => Left(errorMessage)
    }
  }
}
