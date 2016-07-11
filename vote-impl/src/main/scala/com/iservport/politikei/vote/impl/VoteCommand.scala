package com.iservport.politikei.vote.impl

import akka.Done
import com.iservport.politikei.vote.api.Vote
import com.lightbend.lagom.javadsl.persistence.PersistentEntity
import com.lightbend.lagom.serialization.Jsonable

sealed trait VoteCommand extends Jsonable

case class CastVote(vote: Vote) extends PersistentEntity.ReplyType[Done] with VoteCommand
