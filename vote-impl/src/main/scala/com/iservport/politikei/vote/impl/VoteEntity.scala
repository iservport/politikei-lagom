package com.iservport.politikei.vote.impl

import java.util.Optional

import akka.Done
import com.lightbend.lagom.javadsl.persistence.PersistentEntity

class VoteEntity extends PersistentEntity[VoteCommand, VoteEvent, VoteState] {

  override def initialBehavior(snapshotState: Optional[VoteState]): Behavior = {
    val b = newBehaviorBuilder(snapshotState.orElseGet(() => VoteState()))

    // Cast vote command handler
    b.setCommandHandler(classOf[CastVote], (cmd: CastVote, ctx: CommandContext[Done]) => {
      ctx.thenPersist(VoteAdded(cmd.vote.docId, cmd.vote.userId, cmd.vote.voteAs), (evt: VoteAdded) => ctx.reply(Done))
    })

    b.setEventHandler(classOf[VoteAdded], (evt: VoteAdded) => state.addVote(evt.friendId))

    b.build()
  }

}
