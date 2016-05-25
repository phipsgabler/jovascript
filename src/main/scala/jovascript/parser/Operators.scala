package jovascript.parser

object UnaryOperator extends Enumeration {
  type UnaryOperator = Value

  val `!` = Value("!")
  val `+` = Value("+")
  val `-` = Value("-")

//  currently unsupported
//  val `~` = Value("~")
}

object BinaryOperator extends Enumeration {
  type BinaryOperator = Value

  val `+` = Value("+")
  val `-` = Value("-")
  val `*` = Value("*")
  val `/` = Value("/")
  val `%` = Value("%")

  val `<` = Value("<")
  val `<=` = Value("<=")
  val `>` = Value(">")
  val `>=` = Value(">=")
  val `==` = Value("==")
  val `!=` = Value("!=")

  val `&&` = Value("&&")
  val `||` = Value("||")

  // currently unsupported:
//  val `<<` = Value("<<")
//  val `>>` = Value(">>")
//  val `>>>` = Value(">>>")
//  val `&` = Value("&")
//  val `|` = Value("|")
//  val `^` = Value("^")
//  val `=` = Value("=")
//  val `*=` = Value("*=")
//  val `/=` = Value("/=")
//  val `+=` = Value("+=")
//  val `-=` = Value("-=")
//  val `%=` = Value("%=")
//  val `<<=` = Value("<<=")
//  val `>>=` = Value(">>=")
//  val `>>>=` = Value(">>>=")
//  val `&=` = Value("&=")
//  val `|=` = Value("|=")
//  val `^=` = Value("^=")
//
//  val `,` = Value(",")
}