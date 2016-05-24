package jovascript

import org.scalacheck.Gen

class ParserSpec extends PropertySpec {
  property("test") {
    val validInts = Gen.choose(0, Int.MaxValue / 2 - 1)

    forAll(validInts, validInts) { (i: Int, j: Int) =>
       i + j should (be >= i and be >= j)
    }
  }
}
