package jovascript.jsgen

trait PrettyPrintable {
  def prettyPrint(indent: Int): String

  override def toString: String = prettyPrint(0)
}