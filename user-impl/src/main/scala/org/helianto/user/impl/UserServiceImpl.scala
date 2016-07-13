package org.helianto.user.impl

import akka.{Done, NotUsed}
import com.google.inject.Inject
import com.lightbend.lagom.javadsl.api.ServiceCall
import com.lightbend.lagom.javadsl.api.transport.NotFound
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession
import converter.ServiceCallConverter._
import org.helianto.user.api.{SimpleUser, UserService}

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext

// To access an entity from a service implementation you first need to inject the PersistentEntityRegistry
class UserServiceImpl @Inject()(persistentEntities: PersistentEntityRegistry,
                                db: CassandraSession)(implicit ec: ExecutionContext) extends UserService {

  persistentEntities.register(classOf[UserEntity])

  override def getUser(id: String): ServiceCall[NotUsed, SimpleUser] = {
    request =>
      println(s"Calling GET /api/users/$id")
      userEntityRef(id).ask[GetUserReply, GetUser](GetUser())
        .map(_.user.getOrElse(throw new NotFound(s"user $id not found")))
  }

  override def createUser(): ServiceCall[SimpleUser, NotUsed] = { request =>
    println(s"Calling POST /api/users")
    userEntityRef(request.userId).ask[Done, CreateUser](CreateUser(request))
  }

  private def userEntityRef(userId: String) =
    persistentEntities.refFor(classOf[UserEntity], userId.toString)

  override def users() = { request =>
    db.selectAll("SELECT userId, principal, displayName, firstName, lastName, gender, imageUrl FROM users;").map { jrows =>
      val rows = jrows.asScala.toVector
      rows.map { row =>
        SimpleUser(
          row.getString("userId")
          , row.getString("principal")
          , row.getString("displayName")
          , row.getString("firstName")
          , row.getString("lastName")
          , row.getString("gender")
          , row.getString("imageUrl")
        )
      }
    }
  }
}
