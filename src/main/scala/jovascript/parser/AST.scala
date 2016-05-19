package jovascript.parser


sealed trait AST

case class ProgramNode(declarations: Seq[DefinitionNode]) extends AST

sealed trait DefinitionNode extends AST
case class ValueDefinitionNode(name: Symbol, typ: TypeNode, body: ExpressionNode) extends DefinitionNode
//case class TypeDefinitionNode(name: Symbol, typ: TypeNode, body: ExpressionNode) extends AST

sealed trait TypeNode extends AST
case class TypeNameNode(name: Symbol) extends TypeNode


sealed trait ExpressionNode extends AST
case class NumberLiteralNode(literal: Int) extends ExpressionNode
case class IdentifierNode(name: Symbol) extends ExpressionNode
case class LambdaNode(param: Symbol, typ: TypeNode, body: ExpressionNode) extends ExpressionNode
case class WildcardNode(typ: TypeNode) extends ExpressionNode
