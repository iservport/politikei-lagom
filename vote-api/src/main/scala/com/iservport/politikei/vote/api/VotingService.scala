package com.iservport.politikei.vote.api

import akka.NotUsed
import com.lightbend.lagom.javadsl.api.ScalaService._
import com.lightbend.lagom.javadsl.api.{Descriptor, Service, ServiceCall}

/**
  * Voting service
  */
trait VotingService extends Service {

  /**
    * Service call for voting.
    *
    * The request message is the Vote.
    */
  def castVote(): ServiceCall[Vote, NotUsed]

  /**
    * Lagom service interfaces must mix-in a descriptor.
    */
  override def descriptor(): Descriptor = {
    named("votingservice").withCalls(
      namedCall("/api/vote", castVote _)
    )
  }

}
