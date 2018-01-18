/**
 * Copyright (C) 2009-2017 Lightbend Inc. <http://www.lightbend.com>
 */

package partial.function.lib

import partial.function.lib.FI.{Apply, Predicate, UnitApply}

private object CaseStatement {
  def empty[F, T](): PartialFunction[F, T] = PartialFunction.empty
}

private class CaseStatement[-F, +P, T](predicate: Predicate, apply: Apply[P, T])
  extends PartialFunction[F, T] {

  override def isDefinedAt(o: F) = predicate.defined(o)

  override def apply(o: F) = apply.apply(o.asInstanceOf[P])
}

private class UnitCaseStatement[F, P](predicate: Predicate, apply: UnitApply[P])
  extends PartialFunction[F, Unit] {

  override def isDefinedAt(o: F) = predicate.defined(o)

  override def apply(o: F) = apply.apply(o.asInstanceOf[P])
}
