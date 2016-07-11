package com.iservport.politikei.vote.impl

import com.lightbend.lagom.serialization.Jsonable

/**
  * Created by mauriciofernandesdecastro on 10/07/16.
  */
case class VoteState() extends Jsonable {

  def addVote(friendUserId: String): VoteState = user match {
    case None => throw new IllegalStateException("friend can't be added before user is created")
    case Some(user) =>
      val newFriends = user.friends :+ friendUserId
      FriendState(Some(user.copy(friends = newFriends)))
  }
}
