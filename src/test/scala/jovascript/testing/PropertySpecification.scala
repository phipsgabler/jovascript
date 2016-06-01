package jovascript.testing

import org.scalatest.PropSpec
import org.scalatest.prop.GeneratorDrivenPropertyChecks

abstract class PropertySpecification
  extends PropSpec
    with GeneratorDrivenPropertyChecks
