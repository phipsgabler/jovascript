package jovascript.parser

import jovascript.parser.UnaryOperator.UnaryOperator
import jovascript.parser.BinaryOperator.BinaryOperator

sealed trait AST

case class ProgramNode(declarations: Seq[DefinitionNode]) extends AST

sealed trait DefinitionNode extends AST
case class ValueDefinitionNode(name: Symbol, typ: TypeNode, body: ExpressionNode) extends DefinitionNode
//case class TypeDefinitionNode(name: Symbol, typ: TypeNode, body: ExpressionNode) extends AST

sealed trait TypeNode extends AST
case class TypeNameNode(name: Symbol) extends TypeNode
case class FunctionTypeNode(domain: TypeNode, codomain: TypeNode) extends TypeNode


sealed trait ExpressionNode extends AST

sealed trait LiteralNode extends ExpressionNode
case class NumberLiteralNode(literal: Int) extends LiteralNode
case class BoolLiteralNode(literal: Boolean) extends LiteralNode
case class StringLiteralNode(literal: String) extends LiteralNode
case class IdentifierNode(name: Symbol) extends LiteralNode
case class WildcardNode(typ: TypeNode) extends LiteralNode

case class MemberAccessNode(value: ExpressionNode, memberName: Symbol) extends ExpressionNode
case class FunctionCallNode(value: ExpressionNode, arguments: Seq[ExpressionNode]) extends ExpressionNode
case class UnaryOperatorCallNode(operator: UnaryOperator, argument: ExpressionNode) extends ExpressionNode
case class BinaryOperatorCallNode(operator: BinaryOperator, lhs: ExpressionNode, rhs: ExpressionNode) extends ExpressionNode
case class LambdaNode(param: Symbol, typ: TypeNode, body: ExpressionNode) extends ExpressionNode
