/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.iservport.politikei.vote.impl

import com.google.inject.AbstractModule
import com.iservport.politikei.vote.api.VotingService
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport

class VoteModule extends AbstractModule with ServiceGuiceSupport {

  override protected def configure(): Unit = {
    bindServices(serviceBinding(classOf[VotingService], classOf[VotingServiceImpl]))
  }

}
