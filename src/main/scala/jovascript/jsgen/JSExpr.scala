package jovascript.jsgen

import jovascript.jsgen.JSBinaryOperator._
import jovascript.jsgen.JSUnaryOperator._


sealed trait JSExpr extends PrettyPrintable {
  def unary_+ = JSUnOpCall(JSUnaryOperator.`+`, this)
  def unary_- = JSUnOpCall(JSUnaryOperator.`-`, this)
  def unary_! = JSUnOpCall(JSUnaryOperator.`!`, this)
  def unary_~ = JSUnOpCall(JSUnaryOperator.`~`, this)
  def typeof = JSUnOpCall(JSUnaryOperator.`typeof`, this)
  def `void` = JSUnOpCall(JSUnaryOperator.`void`, this)
  def `delete` = JSUnOpCall(JSUnaryOperator.`delete`, this)
  
  def +(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`+`, this, other)
  def -(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`-`, this, other)
  def *(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`*`, this, other)
  def /(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`/`, this, other)
  def %(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`%`, this, other)
  
  def `in`(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`in`, this, other)
  def `instanceof`(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`instanceof`, this, other)
  def >(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`>`, this, other)
  def <(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`<`, this, other)
  def >=(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`>=`, this, other)
  def <=(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`<=`, this, other)
  def ===(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`===`, this, other)
  def !==(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`!==`, this, other)
  
  def >>(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`>>`, this, other)
  def >>>(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`>>>`, this, other)
  def <<(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`<<`, this, other)
  def &(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`&`, this, other)
  def |(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`|`, this, other)
  def ^(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`^`, this, other)
  def &&(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`&&`, this, other)
  def ||(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`||`, this, other)
  
  def *=(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`*=`, this, other)
  def /=(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`/=`, this, other)
  def +=(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`+=`, this, other)
  def -=(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`-=`, this, other)
  def %=(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`%=`, this, other)
  def <<=(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`<<=`, this, other)
  def >>=(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`>>=`, this, other)
  def >>>=(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`>>>=`, this, other)
  def &=(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`&=`, this, other)
  def |=(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`|=`, this, other)
  def ^=(other: JSExpr) = JSBinOpCall(JSBinaryOperator.`^=`, this, other)

  def apply(args: JSExpr*) = JSCall(this, args)
  def index(args: JSExpr*) = JSIndex(this, args)
}

case object JSThis extends JSExpr {
  override def print(i: Int): String = i.spaces + "this"
}

case object JSEmptyArray extends JSExpr {
  override def print(i: Int): String = i.spaces + "[]"
}

case object JSEmptyObject extends JSExpr
{
  override def print(i: Int): String = i.spaces + "{}"
}

case object JSSuper extends JSExpr {
  override def print(i: Int): String = i.spaces + "super"
}

case class JSFunction(args: String*)(body: JSStmt*) extends JSExpr {
  override def print(i: Int): String = {
    val space = i.spaces
    s"""${space}function(${}) {
       |${body.map(_.print(i + indent)).mkString("\n")}
       |${space}}""".stripMargin
  }
}

case class JSNew(constructor: JSExpr, args: JSExpr*) extends JSExpr {
  override def print(i: Int): String = i.spaces + s"new $constructor(${args.mkString(", ")})"
}

case class JSCall(obj: JSExpr, args: JSExpr*) extends JSExpr {
  override def print(i: Int): String = i.spaces + s"$obj(${args.mkString(", ")})"
}

case class JSIndex(obj: JSExpr, args: JSExpr*) extends JSExpr {
  override def print(i: Int): String = i.spaces + s"$obj[${args.mkString(", ")}]"
}

case class JSUnOpCall(operator: JSUnaryOperator, obj: JSExpr) extends {
  override def print(i: Int): String = i.spaces + s"$operator($obj)"
}

case class JSBinOpCall(operator: JSBinaryOperator, lhs: JSExpr, rhs: JSExpr) extends JSExpr {
  override def print(i: Int): String = i.spaces + s"($lhs) $operator ($rhs)"
}

case class JSCondition(cond: JSExpr, trueCase: JSExpr, falseCase: JSExpr) extends JSExpr {
  override def print(i: Int): String = i.spaces + s"($cond) ? ($trueCase) : ($falseCase)"
}
