package com.iservport.politikei.vote.impl

import com.iservport.politikei.vote.api.Vote
import com.lightbend.lagom.serialization.Jsonable

/**
  * Created by mauriciofernandesdecastro on 10/07/16.
  */
case class VoteState(vote: Vote) extends Jsonable
