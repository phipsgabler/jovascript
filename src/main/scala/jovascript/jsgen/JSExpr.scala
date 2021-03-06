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

  def apply(args: JSExpr*) = JSCall(this, args: _*)
  def index(args: JSExpr*) = JSIndex(this, args: _*)
  def get(property: Symbol) = JSGetProperty(this, property)
}

case class JSFunctionExpr(args: Symbol*)(body: JSStmt*) extends JSExpr {
  override def prettyPrint(i: Int): String = {
    val space = i.spaces
    s"""${space}function(${args.map(_.name).mkString(", ")}) {
       |${body.map(_.prettyPrint(i + indent)).mkString("\n")}
       |${space}}""".stripMargin
  }
}

case class JSLambda(args: Symbol*)(body: JSExpr) extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + s"(${args.map(_.name).mkString(", ")}) => $body"
}

case class JSNew(constructor: JSExpr, args: JSExpr*) extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + s"new $constructor(${args.mkString(", ")})"
}

case class JSCall(obj: JSExpr, args: JSExpr*) extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + s"$obj(${args.mkString(", ")})"
}

case class JSIndex(obj: JSExpr, args: JSExpr*) extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + s"$obj[${args.mkString(", ")}]"
}

case class JSGetProperty(obj: JSExpr, property: Symbol) extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + s"$obj.${property.name}"
}

case class JSUnOpCall(operator: JSUnaryOperator, obj: JSExpr) extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + s"$operator($obj)"
}

case class JSBinOpCall(operator: JSBinaryOperator, lhs: JSExpr, rhs: JSExpr) extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + s"($lhs) $operator ($rhs)"
}

case class JSConditional(cond: JSExpr, trueCase: JSExpr, falseCase: JSExpr) extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + s"($cond) ? ($trueCase) : ($falseCase)"
}

case class JSAssign(lhs: JSExpr, rhs: JSExpr) extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + s"$lhs = $rhs"
}

// LITERALS
case class JSIdentifier(name: Symbol) extends JSExpr {
  override def prettyPrint(i: Int) : String = i.spaces + name.name
}

case object JSThis extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + "this"
}

case object JSSuper extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + "super"
}

case object JSEmptyArray extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + "[]"
}

case object JSEmptyObject extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + "{}"
}

case object JSNull extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + "null"
}

case class JSBoolean(value: Boolean) extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + value.toString
}

case class JSNumber(value: Int) extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + value.toString
}

case class JSString(value: String) extends JSExpr {
  override def prettyPrint(i: Int): String = i.spaces + s"'$value'"
}