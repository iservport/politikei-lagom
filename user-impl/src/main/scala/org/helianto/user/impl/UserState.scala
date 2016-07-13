package org.helianto.user.impl

import com.lightbend.lagom.serialization.Jsonable
import org.helianto.user.api.SimpleUser

case class UserState(simpleUser: Option[SimpleUser]) extends Jsonable

object UserState {
  def apply(simpleUser: SimpleUser): UserState = UserState(Option(simpleUser))
}