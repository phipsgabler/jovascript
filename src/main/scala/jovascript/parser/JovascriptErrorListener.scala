package jovascript.parser

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer

import scala.collection.mutable.ListBuffer

class JovascriptErrorListener extends BaseErrorListener {
  private var errorBuilder: Option[ListBuffer[String]] = None
  private var errorCount: Int = 1

  def errors: Option[String] = errorBuilder.map(_.mkString("\n"))

  override def syntaxError(recognizer: Recognizer[_, _],
                           offender: Any,
                           line: Int, position: Int,
                           msg: String,
                           ex: RecognitionException) {

    val fullMessage = s"  $errorCount: $line:$position $msg"

    if (errorBuilder.isEmpty)
      errorBuilder = Some(ListBuffer(fullMessage))
    else
      errorBuilder.foreach(_ += fullMessage)

    errorCount += 1
  }
}
