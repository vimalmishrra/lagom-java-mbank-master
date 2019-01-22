import akka.Done;
import akka.NotUsed;
import com.knoldus.Netbanking;
import com.knoldus.NetbankingService;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import commands.NetbankingCommand;
import events.NetbankingEventProcessor;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class NetbankingServiceImpl implements NetbankingService {

    private final PersistentEntityRegistry persistentEntityRegistry;
    private final CassandraSession session;

    /**
     * @param registry
     * @param readSide
     * @param session
     */
    @Inject
    public NetbankingServiceImpl(final PersistentEntityRegistry registry, ReadSide readSide, CassandraSession session) {
        this.persistentEntityRegistry = registry;
        this.session = session;

        persistentEntityRegistry.register(NetbankingEntity.class);
        readSide.register(NetbankingEventProcessor.class);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<NotUsed, Optional<Netbanking>> netbanking(String id) {
        return request -> {
            CompletionStage<Optional<Netbanking>> netbankingFuture =
                    session.selectAll("SELECT * FROM Netbankings WHERE id = ?", id)
                            .thenApply(rows ->
                                    rows.stream()
                                            .map(row -> Netbanking.builder().id(row.getString("id"))
                                                    .name(row.getString("name")).genre(row.getString("genre"))
                                                    .build()
                                            )
                                            .findFirst()
                            );
            return netbankingFuture;
        };
    }

    /**
     * @return
     */
    @Override
    public ServiceCall<Netbanking, Done> newNetbanking() {
        return netbanking -> {
            PersistentEntityRef<NetbankingCommand> ref = netbankingEntityRef(netbanking);
            return ref.ask(NetbankingCommand.CreateNetbanking.builder().netbanking(netbanking).build());
        };
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<Netbanking, Done> updateNetbanking(String id) {
        return netbanking -> {
            PersistentEntityRef<NetbankingCommand> ref = netbankingEntityRef(netbanking);
            return ref.ask(NetbankingCommand.UpdateNetbanking.builder().netbanking(netbanking).build());
        };
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<NotUsed, Done> deleteNetbanking(String id) {
        return request -> {
            Netbanking netbanking = Netbanking.builder().id(id).build();
            PersistentEntityRef<NetbankingCommand> ref = netbankingEntityRef(netbanking);
            return ref.ask(NetbankingCommand.DeleteNetbanking.builder().netbanking(netbanking).build());
        };
    }

    /**
     * @param netbanking
     * @return
     */
    private PersistentEntityRef<NetbankingCommand> netbankingEntityRef(Netbanking netbanking) {
        return persistentEntityRegistry.refFor(NetbankingEntity.class, netbanking.getId());
    }
}

