package jovascript.jsgen

object JSUnaryOperator extends Enumeration {
  type JSUnaryOperator = Value
  val `delete` = Value("delete")
  val `void` = Value("void")
  val `typeof` = Value("typeof")
  val `!` = Value("!")
  val `~` = Value("~")
  val `+` = Value("+")
  val `-` = Value("-")
}

object JSBinaryOperator extends Enumeration {
  type JSBinaryOperator = Value

  val `+` = Value("+")
  val `-` = Value("-")
  val `*` = Value("*")
  val `/` = Value("/")
  val `%` = Value("%")

  val `in` = Value("in")
  val `instanceof` = Value("instanceof")
  val `<` = Value("<")
  val `<=` = Value("<=")
  val `>` = Value(">")
  val `>=` = Value(">=")
  val `==` = Value("==")
  val `===` = Value("===")
  val `!=` = Value("!=")
  val `!==` = Value("!==")

  val `<<` = Value("<<")
  val `>>` = Value(">>")
  val `>>>` = Value(">>>")
  val `&` = Value("&")
  val `|` = Value("|")
  val `^` = Value("^")
  val `&&` = Value("&&")
  val `||` = Value("||")

  val `=` = Value("=")
  val `*=` = Value("*=")
  val `/=` = Value("/=")
  val `+=` = Value("+=")
  val `-=` = Value("-=")
  val `%=` = Value("%=")
  val `<<=` = Value("<<=")
  val `>>=` = Value(">>=")
  val `>>>=` = Value(">>>=")
  val `&=` = Value("&=")
  val `|=` = Value("|=")
  val `^=` = Value("^=")

  val `,` = Value(",")
}