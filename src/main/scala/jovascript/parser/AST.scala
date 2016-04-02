package jovascript.parser


sealed trait AST
case class ProgramNode(declarations: Seq[AST]) extends AST

case class DefinitionNode(name: String, typ: TypeNode, body: ExpressionNode) extends AST

trait TypeNode extends AST
case class TypeNameNode(name: String) extends TypeNode

trait ExpressionNode extends AST
case class NumberLiteralNode(literal: String) extends ExpressionNode
