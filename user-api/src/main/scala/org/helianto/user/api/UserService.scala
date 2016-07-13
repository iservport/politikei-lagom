package org.helianto.user.api

import akka.NotUsed
import com.lightbend.lagom.javadsl.api.ScalaService._
import com.lightbend.lagom.javadsl.api.{Descriptor, Service, ServiceCall}

trait UserService extends Service {

  def getUser(id: String): ServiceCall[NotUsed, SimpleUser]

  def createUser(): ServiceCall[SimpleUser, NotUsed]

  def users(): ServiceCall[NotUsed, Seq[SimpleUser]]

  override def descriptor(): Descriptor = {
    named("userapi").withCalls(
      pathCall("/api/users/:id", getUser _),
      namedCall("/api/users", users _),
      namedCall("/api/users", createUser _)
    ).withAutoAcl(true)
  }

}
