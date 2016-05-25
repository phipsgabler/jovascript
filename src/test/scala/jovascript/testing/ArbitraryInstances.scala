package jovascript.testing

import jovascript.parser._
import org.scalacheck.{Gen, Arbitrary}

object ArbitraryInstances {
  // make this a pull request for arbitrary?
  implicit lazy val arbSymbol: Arbitrary[Symbol] =
    Arbitrary(Arbitrary.arbitrary[String].map(Symbol(_)))

  implicit lazy val arbUnaryOperator: Arbitrary[UnaryOperator.UnaryOperator] =
    Arbitrary(Gen.oneOf(UnaryOperator.values.toSeq))

  implicit lazy val arbBinaryOperator: Arbitrary[BinaryOperator.BinaryOperator] =
    Arbitrary(Gen.oneOf(BinaryOperator.values.toSeq))

//  implicit lazy val typeNodeIsArbitrary: Arbitrary[TypeNode] = Arbitrary {
//    val typeName = Arbitrary.arbitrary[Symbol].map(TypeNameNode)
//
//    def functionType(size: Int): Gen[FunctionTypeNode] = for {
//      dom <- sizedType(size / 2)
//      cod <- sizedType(size / 2)
//    } yield FunctionTypeNode(dom, cod)
//
//    def sizedType(size: Int): Gen[TypeNode] = {
//      if (size <= 0) typeName
//      else Gen.frequency((3, functionType(size)), (1, typeName))
//
//    }
//
//    Gen.sized(sizedType)
//  }
//
//  implicit lazy val expressionNodeIsArbitrary: Arbitrary[ExpressionNode] = Arbitrary {
//    val numberLiteral = Arbitrary.arbitrary[Int].map(NumberLiteralNode)
//    val boolLiteral = Arbitrary.arbitrary[Boolean].map(BoolLiteralNode)
//    val stringLiteral = Arbitrary.arbitrary[String].map(StringLiteralNode)
//    val identifierLiteral = Arbitrary.arbitrary[Symbol].map(IdentifierNode)
////    val wildcardLiteral = Arbitrary.arbitrary[TypeNode].map(WildcardNode)
//
//    val literal = Gen.oneOf(numberLiteral, boolLiteral, stringLiteral, identifierLiteral)
//
//    def sizedMemberAccessNode(size: Int): Gen[MemberAccessNode] = for {
//      value <- sizedExpressionNode(size / 2)
//      name <- Arbitrary.arbitrary[Symbol]
//    } yield MemberAccessNode(value, name)
//
//    def sizedFunctionCallNode(size: Int): Gen[FunctionCallNode] = for {
//      value <- sizedExpressionNode(size / 2)
//      n <- Gen.choose(size / 3, size / 2)
//      args <- Gen.listOfN(n, sizedExpressionNode(size / 2))
//    } yield FunctionCallNode(value, args)
//
//    def sizedUnaryOperatorNode(size: Int): Gen[UnaryOperatorCallNode] = for {
//      operator <- unaryOperator
//      argument <- sizedExpressionNode(size / 2)
//    } yield UnaryOperatorCallNode(operator, argument)
//
//    def sizedBinaryOperatorNode(size: Int): Gen[BinaryOperatorCallNode] = for {
//      operator <- binaryOperator
//      lhs <- sizedExpressionNode(size / 2)
//      rhs <- sizedExpressionNode(size / 2)
//    } yield BinaryOperatorCallNode(operator, lhs, rhs)
//
//    def sizedLambdaNode(size: Int): Gen[LambdaNode] = for {
//      param <- Arbitrary.arbitrary[Symbol]
//      typ <- Arbitrary.arbitrary[TypeNode]
//      body <- sizedExpressionNode(size / 2)
//    } yield LambdaNode(param, typ, body)
//
//    def sizedComplexExpression(size: Int) = Gen.oneOf(
//      sizedMemberAccessNode(size),
//      sizedFunctionCallNode(size),
//      sizedUnaryOperatorNode(size),
//      sizedBinaryOperatorNode(size),
//      sizedLambdaNode(size)
//    )
//
//    def sizedExpressionNode(size: Int): Gen[ExpressionNode] = {
//      if (size <= 0) literal
//      else Gen.frequency((3, sizedComplexExpression(size)), (1, literal))
//    }
//
//    Gen.sized(sizedExpressionNode)
//  }
}