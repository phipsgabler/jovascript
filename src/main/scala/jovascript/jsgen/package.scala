package jovascript

package object jsgen {
  val indent: Int = 2

  def comma(expr1: JSExpr, expr2: JSExpr, rest: JSExpr*) = {
    rest.foldLeft(JSBinOpCall(JSBinaryOperator.`,`, expr1, expr2))(
      (result, e) => JSBinOpCall(JSBinaryOperator.`,`, result, e))
  }

  def `delete`(expr: JSExpr) = JSUnOpCall(JSUnaryOperator.`delete`, expr)
  def `void`(expr: JSExpr) = JSUnOpCall(JSUnaryOperator.`void`, expr)
  def `typeof`(expr: JSExpr) = JSUnOpCall(JSUnaryOperator.`typeof`, expr)

  private[jsgen] implicit class intOps(self: Int) {
    def spaces: String = " " * self
  }

  implicit def exprToStmt(expr: JSExpr): JSStmt = JSExprStmt(expr)
}


