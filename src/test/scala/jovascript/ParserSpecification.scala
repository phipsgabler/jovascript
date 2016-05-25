package jovascript

import jovascript.parser.BinaryOperator.BinaryOperator
import jovascript.parser.UnaryOperator.UnaryOperator
import jovascript.parser.{AST, JovascriptParser}
import jovascript.testing.ArbitraryInstances._
import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Shapeless._

class ParserSpecification extends Properties("JovascriptParser") {

  property("test") = forAll(Gen.choose(0, Int.MaxValue / 2), Gen.choose(0, Int.MaxValue / 2)) {
    (i: Int, j: Int) => i + j >= i && i + j >= j
  }

  val x = implicitly[Arbitrary[Symbol]]
  val x2 = implicitly[Arbitrary[(UnaryOperator, BinaryOperator)]]

//  property("parsing works") {
//    forAll { (ast: Arbitrary[AST]) =>
//      JovascriptParser.parse(ast.toString) shouldEqual Right(ast)
//    }
//  }
}
