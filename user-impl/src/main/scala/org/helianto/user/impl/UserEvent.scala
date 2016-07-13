package org.helianto.user.impl

import java.time.Instant

import com.lightbend.lagom.javadsl.persistence.{AggregateEvent, AggregateEventTag}
import com.lightbend.lagom.serialization.Jsonable

object UserEvent {
  val Tag = AggregateEventTag.of(classOf[UserEvent])
}
sealed trait UserEvent extends AggregateEvent[UserEvent] with Jsonable {
  override def aggregateTag(): AggregateEventTag[UserEvent] = UserEvent.Tag
}

case class UserCreated(
  userId: String,
  principal: String,
  displayName: String,
  firstName: String,
  lastName: String,
  gender: String,
  imageUrl: String,
  timestamp: Instant = Instant.now()
) extends UserEvent
