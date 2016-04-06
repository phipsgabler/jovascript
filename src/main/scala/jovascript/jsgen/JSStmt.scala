package jovascript.jsgen

// cf. https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements
sealed trait JSStmt extends PrettyPrintable

case class JSExprStmt(expr: JSExpr) extends JSStmt {
  override def print(i: Int) = expr.print(i) + ";"
}

case class JSBlock(exprs: JSExpr*) extends JSStmt {
  override def print(i: Int) =
    s"""{
        |${exprs.map(_.print(i + indent)).mkString("\n")}
        |}
        |""".stripMargin
}


case object JSBreak extends JSStmt {
  override def print(i: Int) = i.spaces + "break;\n"
}

case object JSContinue extends JSStmt {
  override def print(i: Int) = i.spaces + "continue;\n"
}

case object JSEmptyStmt extends JSStmt {
  override def print(i: Int) = ";" // NO SPACES HERE?
}

case class JSFunctionDecl(name: String, args: String*)(body: JSStmt*) extends JSStmt {
  override def print(i: Int): String = {
    val space = i.spaces
    s"""${space}function $name(${}) {
       |${body.map(_.print(i + indent)).mkString("\n")}
       |${space}}
       |""".stripMargin
  }
}

class JSIfElse private (cond: JSExpr, trueCase: Seq[JSStmt],
                        elifCases: Seq[(JSExpr, Seq[JSStmt])],
                        elseCase: Seq[JSStmt]) extends JSStmt {
  override def print(i: Int): String = {
    val space = i.spaces

    val theIf = s"""${space}if ($cond) {
                   |${trueCase.map(_.print(i + indent)).mkString("\n")}
                   |${space}}""".stripMargin
    val elseIfs = elifCases map {
      case (cond, body) => s""" else if ($cond) {
                               |${body.map(_.print(i + indent)).mkString("\n")}
                               |${space}}""".stripMargin
    }

    val theElse = s""" else {
                   |${elseCase.map(_.print(i + indent)).mkString("\n")}
                   |${space}}
                   |""".stripMargin

    theIf + elseIfs + theElse
  }
}

class JSIf private (cond: JSExpr, trueCase: Seq[JSStmt],
                    elifCases: Seq[(JSExpr, Seq[JSStmt])]) extends JSStmt {

  def elseIf(elseCond: JSExpr)(body: Seq[JSStmt]) = new JSIf(cond, trueCase, elifCases :+ (elseCond, body))

  def `else`(elseBody: Seq[JSStmt]) = new JSIfElse(cond, trueCase, elifCases, elseBody)

  def print(i: Int): String = {
    val space = i.spaces

    val theIf = s"""${space}if ($cond) {
                   |${trueCase.map(_.print(i + indent)).mkString("\n")}
                   |${space}}""".stripMargin
    val elseIfs = elifCases map {
      case (cond, body) => s""" else if ($cond) {
                               |${body.map(_.print(i + indent)).mkString("\n")}
                               |${space}}
                               |""".stripMargin
    }

    theIf + elseIfs
  }
}

object JSIf {
  def apply(cond: JSExpr)(trueCases: Seq[JSStmt]) = new JSIf(cond, trueCases, Seq())
}


case class JSThrow(exception: JSExpr) extends JSStmt {
  override def print(i: Int) = i.spaces + s"throw ($exception);"
}

class JSTry private (tryStmts: Seq[JSStmt],
                     catchStmts: Seq[(String, Seq[JSStmt])],
                     finallyStmts: Seq[JSStmt]) extends JSStmt {
  def print(i: Int): String = {
    val space = i.spaces

    val theTry = s"""${space}try {
                    |${tryStmts.map(_.print(i + indent)).mkString("\n")}
                    |${space}}""".stripMargin
    val catches = catchStmts map {
      case (exception, body) => s""" catch ($exception) {
                                    |${body.map(_.print(i + indent)).mkString("\n")}
                                    |${space}}""".stripMargin
    }
    val theFinally = s""" finally {
                    |${finallyStmts.map(_.print(i + indent)).mkString("\n")}
                    |${space}}
                    |""".stripMargin

    theTry + catches + theFinally
  }
}

class JSTryBuilder private (tryStmts: Seq[JSStmt],
                            catchStmts: Seq[(String, Seq[JSStmt])]) extends JSTry{

  def `catch`(exception: String)(body: JSStmt*) = new JSTryBuilder(tryStmts, catchStmts :+ (exception, body))
  def `finally`(body: JSStmt*) = new JSTry(tryStmts, catchStmts, body)

  override def print(i: Int): String = {
    val space = i.spaces

    val theTry = s"""${space}try {
                   |${tryStmts.map(_.print(i + indent)).mkString("\n")}
                   |${space}}""".stripMargin
    val catches = catchStmts map {
      case (exception, body) => s""" catch ($exception) {
                               |${body.map(_.print(i + indent)).mkString("\n")}
                               |${space}}
                               |""".stripMargin
    }

    theTry + catches
  }
}

object JSTry {
  def apply(body: JSStmt*) = new JSTryBuilder(body, Seq(), Seq())
}

case class JSReturn(expr: JSExpr) extends JSStmt {
  def print(i: Int) =  i.spaces + s"return ($expr);"
}


case class JSVarDecl(name: String) extends JSStmt {
  def print(i: Int) =  i.spaces + s"var $name;"
}

case class JSVar(name: String, expr: JSExpr) extends JSStmt {
  def print(i: Int) =  i.spaces + s"var $name = $expr;"
}

case class JSLetDecl(name: String) extends JSStmt {
  def print(i: Int) =  i.spaces + s"let $name;"
}

case class JSLet(name: String, expr: JSExpr) extends JSStmt {
  def print(i: Int) =  i.spaces + s"let $name = $expr;"
}

case class JSConst(name: String, expr: JSExpr) extends JSStmt {
  def print(i: Int) =  i.spaces + s"const $name = $expr;"
}

case class JSFor(e1: JSExpr, e2: JSExpr, e3: JSExpr)(body: JSStmt*) extends JSStmt {
  def print(i: Int) = {
    val space = i.spaces

    s"""${space}for ($e1; $e2; $e3) {
       |${body.map(_.print(i + indent)).mkString("\n")}
       |${space}}
       |""".stripMargin
  }
}


case class JSForIn(name: String, expr: JSExpr)(body: JSStmt*) extends JSStmt {
  def print(i: Int) = {
    val space = i.spaces

    s"""${space}for (let $name in $expr) {
       |${body.map(_.print(i + indent)).mkString("\n")}
       |${space}}
       |""".stripMargin
  }
}

case class JSForOf(name: String, expr: JSExpr)(body: JSStmt*) extends JSStmt {
  def print(i: Int) = {
    val space = i.spaces

    s"""${space}for (let $name of $expr) {
       |${body.map(_.print(i + indent)).mkString("\n")}
       |${space}}
       |""".stripMargin
  }
}

case class JSWhile(cond: JSExpr)(body: JSStmt*) extends JSStmt {
  def print(i: Int) = {
    val space = i.spaces

    s"""${space}while ($cond) {
       |${body.map(_.print(i + indent)).mkString("\n")}
       |${space}}
       |""".stripMargin
  }
}

case class JSDo(body: JSStmt*)(cond: JSExpr) extends JSStmt {
  def print(i: Int) = {
    val space = i.spaces

    s"""${space}do {
       |${body.map(_.print(i + indent)).mkString("\n")}
       |${space}} while ($cond)
       |""".stripMargin
  }
}