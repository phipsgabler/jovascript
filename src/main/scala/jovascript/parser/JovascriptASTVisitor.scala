package jovascript.parser

import jovascript.parser.AST

class JovascriptASTVisitor extends JovascriptBaseVisitor[AST] {
  override def visitProgram(ctx: JovascriptParser.ProgramContext): AST = {
    ProgramNode(Seq())
  }
}
