package jovascript.parser

import org.antlr.v4.runtime.tree.ParseTree

trait JovascriptVisitorExtended[T] extends jovascript.parser.gen.JovascriptBaseVisitor[T] {
  def apply(tree: ParseTree): T = visit(tree)
}
