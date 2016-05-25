package jovascript.parser

import jovascript.parser.gen._
import org.antlr.v4.runtime._

object JovascriptParser {
  def parse(input: String): Either[String, AST] = {
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
