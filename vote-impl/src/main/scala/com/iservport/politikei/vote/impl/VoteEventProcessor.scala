package com.iservport.politikei.vote.impl

import scala.collection.JavaConverters._
import scala.collection.immutable.Seq
import scala.compat.java8.FutureConverters._
import java.util.{Optional, UUID}
import java.util.concurrent.CompletionStage
import javax.inject.Inject

import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag
import com.lightbend.lagom.javadsl.persistence.cassandra.{CassandraReadSideProcessor, CassandraSession}

import scala.concurrent.ExecutionContext

class VoteEventProcessor @Inject()(implicit ec: ExecutionContext) extends CassandraReadSideProcessor[VoteEvent] {

  // Needed to convert some Scala types to Java
  import converter.CompletionStageConverters._

  @volatile private var writeVotes: PreparedStatement = null // initialized in prepare

  private def setWriteFollowers(writeVotes: PreparedStatement): Unit =
    this.writeVotes = writeVotes

  override def aggregateTag: AggregateEventTag[VoteEvent] = VoteEvent.Tag

  override def prepare(session: CassandraSession): CompletionStage[Optional[UUID]] = {
    prepareCreateTables(session)
  }

  private def prepareCreateTables(session: CassandraSession) = {
    session.executeCreateTable(
      "CREATE TABLE IF NOT EXISTS vote ("
        + "voteId text, documentId text, voteAs int"
        + "PRIMARY KEY (voteId, documentId))")
  }

  private def prepareWriteFollowers(session: CassandraSession) = {
    val statement = session.prepare("INSERT INTO vote (voteId, documentId, votedAs) VALUES (?, ?, ?)")
    statement.map(ps => {
      setWriteFollowers(ps)
      Done
    })
  }

  override def defineEventHandlers(builder: EventHandlersBuilder): EventHandlers = {
    builder.setEventHandler(classOf[VoteAdded], processVoteChanged)
    builder.build()
  }

  private def processVoteChanged(event: VoteAdded, offset: UUID) = {
    val bindWriteFollowers = writeVotes.bind()
    bindWriteFollowers.setString("voteId", event.voteId)
    bindWriteFollowers.setString("documentId", event.documentId)
    bindWriteFollowers.setInt("votedAs", event.votedAs)
    val bindWriteOffset = writeOffset.bind(offset)
    completedStatements(Seq(bindWriteFollowers, bindWriteOffset).asJava)
  }

}
