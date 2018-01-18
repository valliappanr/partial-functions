package partial.function.lib

import scala.runtime.BoxedUnit

object AbstractFlow {
  final class ReceiveFlow(val onMessage: PartialFunction[Any, BoxedUnit]) {
    def orElse(other: ReceiveFlow): ReceiveFlow = new ReceiveFlow(onMessage.orElse(other.onMessage))
  }


}

trait Flow {
  type ReceiveFlow = PartialFunction[Any, Unit]
  def receive: ReceiveFlow
}
object Flow {
  type ReceiveFlow = PartialFunction[Any, Unit]
}

abstract class AbstractFlow extends Flow {
  def createReceiveFlow(): AbstractFlow.ReceiveFlow

  override def receive: PartialFunction[Any, Unit] =
    createReceiveFlow().onMessage.asInstanceOf[PartialFunction[Any, Unit]]
}