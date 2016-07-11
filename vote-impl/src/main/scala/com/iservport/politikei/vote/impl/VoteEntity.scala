package com.iservport.politikei.vote.impl

import java.util.Optional

import akka.Done
import com.lightbend.lagom.javadsl.persistence.PersistentEntity

class VoteEntity extends PersistentEntity[VoteCommand, VoteEvent, VoteState] {

  override def initialBehavior(snapshotState: Optional[VoteState]): Behavior = {
    val b = newBehaviorBuilder(snapshotState.orElseGet(() => VoteState()))

    // Cast vote command handler
    b.setCommandHandler(classOf[CastVote], (cmd: CastVote, ctx: CommandContext[Done]) => {
      state.user match {
        case None =>
          ctx.invalidCommand(s"User ${entityId} is not  created")
          ctx.done()
//        case Some(user) if user.friends.contains(cmd.friendUserId) =>
//          ctx.reply(Done)
//          ctx.done()
        case Some(user) =>
          ctx.thenPersist(VoteAdded(user.userId, cmd.friendUserId), (evt: VoteAdded) => ctx.reply(Done))
      }
    })

    b.setEventHandler(classOf[VoteAdded], (evt: VoteAdded) => state.addVote(evt.friendId))

    b.build()
  }

}
