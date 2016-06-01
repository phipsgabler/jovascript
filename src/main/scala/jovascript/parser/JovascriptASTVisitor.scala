package jovascript.parser

import jovascript.parser.gen.JovascriptParser._
import jovascript.parser.gen.{JovascriptBaseVisitor => _, _}

import scala.collection.JavaConversions._

object JovascriptASTVisitor extends JovascriptVisitorExtended[AST] {
  override def visitProgram(ctx: ProgramContext): AST = {
    programVisitor(ctx)
  }
}

private[parser] object programVisitor extends JovascriptVisitorExtended[ProgramNode] {
  override def visitProgram(ctx: ProgramContext): ProgramNode = {
    ProgramNode(ctx.definition.map(definitionVisitor.visit).toSeq)
  }
}

private[parser] object definitionVisitor extends JovascriptVisitorExtended[DefinitionNode] {
  override def visitValueDefinition(ctx: ValueDefinitionContext): DefinitionNode = {
    ValueDefinitionNode(
      Symbol(ctx.ident.getText),
      typeExpressionVisitor(ctx.typ),
      expressionVisitor(ctx.expr))
  }
}

private[parser] object expressionVisitor extends JovascriptVisitorExtended[ExpressionNode] {
  override def visitNumberLiteral(ctx: NumberLiteralContext): ExpressionNode = {
    NumberLiteralNode(ctx.NUMBER_LITERAL().getText.toInt)
  }

  override def visitIdentifierLiteral(ctx: IdentifierLiteralContext): ExpressionNode = {
    IdentifierNode(Symbol(ctx.IDENTIFIER().getText))
  }

  override def visitLambdaExpression(ctx: LambdaExpressionContext): ExpressionNode = {
    lambdaParameterVisitor(ctx.parameter) match {
      case (name, typ) => LambdaNode(name, typ, expressionVisitor(ctx.expression))
    }
  }

//  override def visitOperatorExpression(ctx: OperatorExpressionContext): ExpressionNode = {
//    // TODO: implement this
//    IdentifierNode('?)
//  }

  override def visitParenthesizedExpression(ctx: ParenthesizedExpressionContext): ExpressionNode = {
    visit(ctx.expression)
  }
}

private[parser] object lambdaParameterVisitor extends JovascriptVisitorExtended[(Symbol, TypeNode)] {
  override def visitLambdaArgument(ctx: LambdaArgumentContext): (Symbol, TypeNode) = {
    val name = ctx.ident.getText
    val typ = typeExpressionVisitor(ctx.typ)
    (Symbol(name), typ)
  }

  override def visitParenthesizedLambdaParam(ctx: ParenthesizedLambdaParamContext): (Symbol, TypeNode) = {
    visit(ctx.lambdaArgument)
  }

  override def visitLambdaParam(ctx: LambdaParamContext): (Symbol, TypeNode) = {
    visit(ctx.lambdaArgument)
  }
}

private[parser] object typeExpressionVisitor extends JovascriptVisitorExtended[TypeNode] {
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
