package jovascript

import jovascript.parser._
import jovascript.testing.PropertySpecification
import jovascript.testing.ArbitraryInstances._  // necessary for shapeless derivation to work
import org.scalacheck.Shapeless._
import org.scalacheck._


class ParserSpecification extends PropertySpecification {
  property("test") {
    forAll(Gen.choose(0, Int.MaxValue / 2), Gen.choose(0, Int.MaxValue / 2)) {
      (i: Int, j: Int) => i + j >= i && i + j >= j
    }
  }

  property("parsing works") {
    forAll { (ast: AST) =>
      JovascriptParser.parse(ast.toString) == Right(ast)
    }
  }
}
