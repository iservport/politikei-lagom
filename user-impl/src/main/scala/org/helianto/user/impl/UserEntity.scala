package org.helianto.user.impl

import java.util.Optional

import akka.Done
import com.lightbend.lagom.javadsl.persistence.PersistentEntity
import org.helianto.user.api.SimpleUser

import scala.collection.JavaConverters._
import scala.collection.immutable.Seq

class UserEntity extends PersistentEntity[UserCommand, UserEvent, UserState] {

  override def initialBehavior(snapshotState: Optional[UserState]): Behavior = {

    val b = newBehaviorBuilder(snapshotState.orElseGet(() => UserState(Option.empty)))

    b.setCommandHandler(classOf[CreateUser], (cmd: CreateUser, ctx: CommandContext[Done]) => {
      state.simpleUser match {
        case Some(_) =>
          ctx.invalidCommand(s"User $entityId is already created")
          ctx.done()
        case None =>
          val simpleUser = cmd.simpleUser
          val events = Seq(UserCreated(
            simpleUser.userId
            , simpleUser.principal
            , simpleUser.displayName
            , simpleUser.firstName
            , simpleUser.lastName
            , simpleUser.gender
            , simpleUser.imageUrl)
          )
          ctx.thenPersistAll(events.asJava, () => ctx.reply(Done))
      }
    })

    b.setEventHandler(classOf[UserCreated], (evt: UserCreated) =>
      UserState(new SimpleUser(
        evt.userId
        , evt.principal
        , evt.displayName
        , evt.firstName
        , evt.lastName
        , evt.gender
        , evt.imageUrl)
      ))

    b.setReadOnlyCommandHandler(classOf[GetUser], (cmd: GetUser, ctx: ReadOnlyCommandContext[GetUserReply]) =>
      ctx.reply(GetUserReply(state.simpleUser))
    )

    b.build()
  }
}
