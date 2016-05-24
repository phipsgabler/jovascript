package jovascript

import org.scalatest._
import org.scalatest.prop.GeneratorDrivenPropertyChecks

abstract class PropertySpec extends PropSpec
  with GeneratorDrivenPropertyChecks
  with DiagrammedAssertions
  with ShouldMatchers
