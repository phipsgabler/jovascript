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
    LambdaParameterVisitor.visit(ctx.parameter) match {
      case (name, typ) => LambdaNode(name, typ, ExpressionVisitor.visit(ctx.expression))
    }
  }

  override def visitOperatorExpression(ctx: OperatorExpressionContext): ExpressionNode = {
    IdentifierNode('?)
  }

  override def visitParenthesizedExpression(ctx: ParenthesizedExpressionContext): ExpressionNode = {
    visit(ctx.expression)
  }
}

object LambdaParameterVisitor extends JovascriptBaseVisitor[(Symbol, TypeNode)] {
  override def visitLambdaArgument(ctx: LambdaArgumentContext): (Symbol, TypeNode) = {
    val name = ctx.ident.getText
    val typ = TypeExpressionVisitor.visit(ctx.typ)
    (Symbol(name), typ)
  }

  override def visitParenthesizedLambdaParam(ctx: ParenthesizedLambdaParamContext): (Symbol, TypeNode) = {
    visit(ctx.lambdaArgument)
  }

  override def visitLambdaParam(ctx: LambdaParamContext): (Symbol, TypeNode) = {
    visit(ctx.lambdaArgument)
  }
}

object TypeExpressionVisitor extends JovascriptBaseVisitor[TypeNode] {
  override def visitFunctionType(ctx: FunctionTypeContext): TypeNode = {
    FunctionTypeNode(visit(ctx.dom), visit(ctx.cod))
  }

  override def visitTypeName(ctx: TypeNameContext): TypeNode = {
    TypeNameNode(Symbol(ctx.TYPENAME().getText))
  }

  override def visitParenthesizedTypeExpression(ctx: ParenthesizedTypeExpressionContext): TypeNode = {
    visit(ctx.typeExpression)
  }
}
