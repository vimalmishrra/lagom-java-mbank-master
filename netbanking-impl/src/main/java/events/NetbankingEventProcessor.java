package events;

import akka.Done;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import events.NetbankingEvent.*;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;


public class NetbankingEventProcessor extends ReadSideProcessor<NetbankingEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetbankingEventProcessor.class);

    private final CassandraSession session;
    private final CassandraReadSide readSide;

    private PreparedStatement writeNetbankings;
    private PreparedStatement deleteNetbankings;

    /**
     *
     * @param session
     * @param readSide
     */
    @Inject
    public NetbankingEventProcessor(final CassandraSession session, final CassandraReadSide readSide) {
        this.session = session;
        this.readSide = readSide;
    }

    /**
     *
     * @return
     */
    @Override
    public PSequence<AggregateEventTag<NetbankingEvent>> aggregateTags() {
        LOGGER.info(" aggregateTags method ... ");
        return TreePVector.singleton(NetbankingEventTag.INSTANCE);
    }

    /**
     *
     * @return
     */
    @Override
    public ReadSideHandler<NetbankingEvent> buildHandler() {
        LOGGER.info(" buildHandler method ... ");
        return readSide.<NetbankingEvent>builder("Netbankings_offset")
                .setGlobalPrepare(this::createTable)
                .setPrepare(evtTag -> prepareWriteNetbanking()
                        .thenCombine(prepareDeleteNetbanking(), (d1, d2) -> Done.getInstance())
                )
                .setEventHandler(NetbankingCreated.class, this::processPostAdded)
                .setEventHandler(NetbankingUpdated.class, this::processPostUpdated)
                .setEventHandler(NetbankingDeleted.class, this::processPostDeleted)
                .build();
    }

    /**
     *
     * @return
     */
    // Execute only once while application is start
    private CompletionStage<Done> createTable() {
        return session.executeCreateTable(
                "CREATE TABLE IF NOT EXISTS Netbankings ( " +
                        "id TEXT, name TEXT, genre TEXT, PRIMARY KEY(id))"
        );
    }

    /*
    * START: Prepare statement for insert Netbanking values into Netbankings table.
    * This is just creation of prepared statement, we will map this statement with our event
    */

    /**
     *
     * @return
     */
    private CompletionStage<Done> prepareWriteNetbanking() {
        return session.prepare(
                "INSERT INTO Netbankings (id, name, genre) VALUES (?, ?, ?)"
        ).thenApply(ps -> {
            setWriteNetbankings(ps);
            return Done.getInstance();
        });
    }

    /**
     *
     * @param statement
     */
    private void setWriteNetbankings(PreparedStatement statement) {
        this.writeNetbankings = statement;
    }

    // Bind prepare statement while NetbankingCreate event is executed

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostAdded(NetbankingCreated event) {
        BoundStatement bindWriteNetbanking = writeNetbankings.bind();
        bindWriteNetbanking.setString("id", event.getNetbanking().getId());
        bindWriteNetbanking.setString("name", event.getNetbanking().getName());
        bindWriteNetbanking.setString("genre", event.getNetbanking().getGenre());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteNetbanking));
    }
    /* ******************* END ****************************/

    /* START: Prepare statement for update the data in Netbankings table.
    * This is just creation of prepared statement, we will map this statement with our event
    */

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostUpdated(NetbankingUpdated event) {
        BoundStatement bindWriteNetbanking = writeNetbankings.bind();
        bindWriteNetbanking.setString("id", event.getNetbanking().getId());
        bindWriteNetbanking.setString("name", event.getNetbanking().getName());
        bindWriteNetbanking.setString("genre", event.getNetbanking().getGenre());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteNetbanking));
    }
    /* ******************* END ****************************/

    /* START: Prepare statement for delete the the Netbanking from table.
    * This is just creation of prepared statement, we will map this statement with our event
    */

    /**
     *
     * @return
     */
    private CompletionStage<Done> prepareDeleteNetbanking() {
        return session.prepare(
                "DELETE FROM Netbankings WHERE id=?"
        ).thenApply(ps -> {
            setDeleteNetbankings(ps);
            return Done.getInstance();
        });
    }

    /**
     *
     * @param deleteNetbankings
     */
    private void setDeleteNetbankings(PreparedStatement deleteNetbankings) {
        this.deleteNetbankings = deleteNetbankings;
    }

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostDeleted(NetbankingDeleted event) {
        BoundStatement bindWriteNetbanking = deleteNetbankings.bind();
        bindWriteNetbanking.setString("id", event.getNetbanking().getId());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteNetbanking));
    }
    /* ******************* END ****************************/
}
