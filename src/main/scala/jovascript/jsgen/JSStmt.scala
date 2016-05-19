package jovascript.jsgen

// cf. https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements
sealed trait JSStmt extends PrettyPrintable

case class JSExprStmt(expr: JSExpr) extends JSStmt {
  override def prettyPrint(i: Int) = i.spaces + s"$expr;"
}

case class JSBlock(exprs: JSExpr*) extends JSStmt {
  override def prettyPrint(i: Int) =
    s"""{
        |${exprs.map(_.prettyPrint(i + indent)).mkString("\n")}
        |}
        |""".stripMargin
}


case object JSBreak extends JSStmt {
  override def prettyPrint(i: Int) = i.spaces + "break;\n"
}

case object JSContinue extends JSStmt {
  override def prettyPrint(i: Int) = i.spaces + "continue;\n"
}

case object JSEmptyStmt extends JSStmt {
  override def prettyPrint(i: Int) = ";" // NO SPACES HERE?
}

case class JSFunctionDecl(name: Symbol, args: Symbol*)(body: JSStmt*) extends JSStmt {
  override def prettyPrint(i: Int): String = {
    val space = i.spaces

    s"""${space}function ${name.name}(${args.map(_.name).mkString(", ")}) {
       |${body.map(_.prettyPrint(i + indent)).mkString("\n")}
       |${space}}
       |""".stripMargin
  }
}


case class JSIf(cond: JSExpr,
                trueCase: Seq[JSStmt],
                falseCase: Seq[JSStmt]) extends JSStmt {

  private def printElses(stmts: Seq[JSStmt], i: Int): String = {
    val space = i.spaces

    stmts.toList match {
      case Nil => "\n"
      case (e@JSIf(_, _, _)) :: rest => {
        val elseIf = s"""${space}else if (${e.cond}) {
                        |${e.trueCase.map(_.prettyPrint(i + indent)).mkString("\n")}
                        |${space}}""".stripMargin
        elseIf + printElses(e.falseCase, i)
      }
      case stmts =>  s"""${space}else {
                        |${stmts.map(_.prettyPrint(i + indent)).mkString("\n")}
                        |${space}}
                        |""".stripMargin
    }
  }

  override def prettyPrint(i: Int): String = {
    val space = i.spaces

    val theIf = s"""${space}if ($cond) {
                   |${trueCase.map(_.prettyPrint(i + indent)).mkString("\n")}
                   |${space}}""".stripMargin

    theIf + printElses(falseCase, i)
  }

  def elseIf(elseCond: JSExpr)(body: Seq[JSStmt]) = JSIf(cond, trueCase, falseCase :+ JSIf(elseCond, body, Seq()))

  def `else`(elseBody: Seq[JSStmt]) = JSIf(cond, trueCase, elseBody)
}



case class JSThrow(exception: JSExpr) extends JSStmt {
  override def prettyPrint(i: Int) = i.spaces + s"throw ($exception);"
}

case class JSTry (tryStmts: Seq[JSStmt],
                  catchStmts: Seq[(Symbol, Seq[JSStmt])],
                  finallyStmt: Option[Seq[JSStmt]] = None) extends JSStmt {
  def prettyPrint(i: Int): String = {
    val space = i.spaces

    val theTry = s"""${space}try {
                    |${tryStmts.map(_.prettyPrint(i + indent)).mkString("\n")}
                    |${space}}""".stripMargin

    val catches = catchStmts map {
      case (exception, body) => s"""${space}catch (${exception.name}) {
                                    |${body.map(_.prettyPrint(i + indent)).mkString("\n")}
                                    |${space}}""".stripMargin
    }

    val theFinally = finallyStmt match {
      case None => "\n"
      case Some(stmts) => s"""${space}finally {
                             |${stmts.map(_.prettyPrint(i + indent)).mkString("\n")}
                             |${space}}
                             |""".stripMargin
    }

    theTry + catches + theFinally
  }

  def `catch`(exception: Symbol)(body: JSStmt*) = JSTry(tryStmts, catchStmts :+ (exception, body), finallyStmt)
  def `finally`(body: JSStmt*) = JSTry(tryStmts, catchStmts, Some(body))
}

case class JSReturn(expr: JSExpr) extends JSStmt {
  def prettyPrint(i: Int) =  i.spaces + s"return ($expr);"
}


sealed trait JSDef extends JSStmt

case class JSVarDecl(name: Symbol) extends JSStmt {
  def prettyPrint(i: Int) =  i.spaces + s"var ${name.name};"
}

case class JSVarDef(name: Symbol, expr: JSExpr) extends JSDef {
  def prettyPrint(i: Int) =  i.spaces + s"var ${name.name} = $expr;"
}

case class JSLetDecl(name: Symbol) extends JSStmt {
  def prettyPrint(i: Int) =  i.spaces + s"let ${name.name};"
}

case class JSLetDef(name: Symbol, expr: JSExpr) extends JSDef {
  def prettyPrint(i: Int) =  i.spaces + s"let ${name.name} = $expr;"
}

case class JSConstDef(name: Symbol, expr: JSExpr) extends JSDef {
  def prettyPrint(i: Int) =  i.spaces + s"const ${name.name} = $expr;"
}

case class JSFor private[jsgen] (e1: Either[JSExpr, JSDef],
                                 e2: JSExpr,
                                 e3: JSExpr,
                                 body: Seq[JSStmt]) extends JSStmt {
  def this(e1: JSExpr, e2: JSExpr, e3: JSExpr)(body: JSStmt*) = this(Left(e1), e2, e3, body)
  def this(defn: JSDef, e2: JSExpr, e3: JSExpr)(body: JSStmt*) = this(Right(defn), e2, e3, body)

  def prettyPrint(i: Int) = {
    val space = i.spaces

    val e1Print = e1.fold(expr => expr.toString, defn => defn.toString.init)

    s"""${space}for ($e1Print; $e2; $e3) {
       |${body.map(_.prettyPrint(i + indent)).mkString("\n")}
       |${space}}
       |""".stripMargin
  }
}


case class JSForIn(name: Symbol, expr: JSExpr)(body: JSStmt*) extends JSStmt {
  def prettyPrint(i: Int) = {
    val space = i.spaces

    s"""${space}for (let ${name.name} in $expr) {
       |${body.map(_.prettyPrint(i + indent)).mkString("\n")}
       |${space}}
       |""".stripMargin
  }
}

case class JSForOf(name: Symbol, expr: JSExpr)(body: JSStmt*) extends JSStmt {
  def prettyPrint(i: Int) = {
    val space = i.spaces

    s"""${space}for (let ${name.name} of $expr) {
       |${body.map(_.prettyPrint(i + indent)).mkString("\n")}
       |${space}}
       |""".stripMargin
  }
}

case class JSWhile(cond: JSExpr)(body: JSStmt*) extends JSStmt {
  def prettyPrint(i: Int) = {
    val space = i.spaces

    s"""${space}while ($cond) {
       |${body.map(_.prettyPrint(i + indent)).mkString("\n")}
       |${space}}
       |""".stripMargin
  }
}

case class JSDo(body: JSStmt*)(cond: JSExpr) extends JSStmt {
  def prettyPrint(i: Int) = {
    val space = i.spaces

    s"""${space}do {
       |${body.map(_.prettyPrint(i + indent)).mkString("\n")}
       |${space}} while ($cond)
       |""".stripMargin
  }
}