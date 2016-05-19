package jovascript.parser

import jovascript.parser.JovascriptParser._

import scala.collection.JavaConversions._

object JovascriptASTVisitor extends JovascriptBaseVisitor[AST] {
  override def visitProgram(ctx: JovascriptParser.ProgramContext): AST = {
    ProgramVisitor.visitProgram(ctx)
  }
}

object ProgramVisitor extends JovascriptBaseVisitor[ProgramNode] {
  override def visitProgram(ctx: JovascriptParser.ProgramContext): ProgramNode = {
    ProgramNode(ctx.definition.map(DefinitionVisitor.visit).toSeq)
  }
}

object DefinitionVisitor extends JovascriptBaseVisitor[DefinitionNode] {
  override def visitValueDefinition(ctx: ValueDefinitionContext): DefinitionNode = {
    ValueDefinitionNode(
      Symbol(ctx.ident.getText),
      TypeExpressionVisitor.visit(ctx.typ),
      ExpressionVisitor.visit(ctx.expr))
  }
}

object ExpressionVisitor extends JovascriptBaseVisitor[ExpressionNode] {
  override def visitNumberLiteral(ctx: NumberLiteralContext): ExpressionNode = {
    NumberLiteralNode(ctx.NUMBER_LITERAL().getText.toInt)
  }

  override def visitIdentifierExpression(ctx: IdentifierExpressionContext): ExpressionNode = {
    IdentifierNode(Symbol(ctx.IDENTIFIER().getText))
  }

  override def visitLambda(ctx: LambdaContext): ExpressionNode = {
    IdentifierNode('?)
  }

  override def visitOperatorExpression(ctx: OperatorExpressionContext): ExpressionNode = {
    IdentifierNode('?)
  }

  override def visitParenthesizedExpression(ctx: ParenthesizedExpressionContext): ExpressionNode = {
    IdentifierNode('?)
  }
}

object TypeExpressionVisitor extends JovascriptBaseVisitor[TypeNode] {

}
