package org.helianto.user.impl

import akka.Done
import com.lightbend.lagom.javadsl.persistence.PersistentEntity
import com.lightbend.lagom.serialization.Jsonable
import org.helianto.user.api.SimpleUser


sealed trait UserCommand extends Jsonable

case class CreateUser(simpleUser: SimpleUser) extends PersistentEntity.ReplyType[Done] with UserCommand

case class GetUser() extends PersistentEntity.ReplyType[GetUserReply] with UserCommand

case class GetUserReply(user: Option[SimpleUser]) extends Jsonable
