package com.iservport.politikei.vote.impl

import java.time.Instant
import java.util.Date

import com.lightbend.lagom.javadsl.persistence.{AggregateEvent, AggregateEventTag}
import com.lightbend.lagom.serialization.Jsonable

object VoteEvent {
  val Tag = AggregateEventTag.of(classOf[VoteEvent])
}
class VoteEvent extends AggregateEvent[VoteEvent] with Jsonable {
  override def aggregateTag: AggregateEventTag[VoteEvent] = VoteEvent.Tag
}
case class VoteAdded(docId: String, userId: String, voted: Int, visited: Date = new Date()) extends VoteEvent
