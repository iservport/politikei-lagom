package com.iservport.politikei.vote.impl

import javax.inject.Inject

import akka.{Done, NotUsed}
import com.iservport.politikei.vote.api.{Vote, VotingService}
import com.lightbend.lagom.javadsl.api.ServiceCall
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext

/**
  * Voting service implementation
  */
class VotingServiceImpl @Inject() (persistentEntities: PersistentEntityRegistry)
  (implicit ec: ExecutionContext)
  extends VotingService {

  persistentEntities.register(classOf[VoteEntity])

  override def castVote(): ServiceCall[Vote, NotUsed] = {
    request =>
      voteEntityRef(request.docId).ask[Done, CastVote](CastVote(request))
  }

  /**
    * Commands are sent to [[VoteEntity]] using a `PersistentEntityRef`.
    *
    * @param docId
    */
  private def voteEntityRef(docId: String) =
    persistentEntities.refFor(classOf[VoteEntity], docId)

}
