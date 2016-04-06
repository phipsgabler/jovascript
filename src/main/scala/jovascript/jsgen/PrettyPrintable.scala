package jovascript.jsgen

trait PrettyPrintable {
  def print(indent: Int): String

  override def toString: String = print(0)
}