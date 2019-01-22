import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import commands.NetbankingCommand;
import events.NetbankingEvent;
import states.NetbankingStates;

import java.time.LocalDateTime;
import java.util.Optional;

public class NetbankingEntity  extends PersistentEntity<NetbankingCommand, NetbankingEvent, NetbankingStates> {
    /**
     *
     * @param snapshotState
     * @return
     */
    @Override
    public Behavior initialBehavior(Optional<NetbankingStates> snapshotState) {

        // initial behaviour of netbanking
        BehaviorBuilder behaviorBuilder = newBehaviorBuilder(
                NetbankingStates.builder().netbanking(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(NetbankingCommand.CreateNetbanking.class, (cmd, ctx) ->
                ctx.thenPersist(NetbankingEvent.NetbankingCreated.builder().netbanking(cmd.getNetbanking())
                        .entityId(entityId()).build(), evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(NetbankingEvent.NetbankingCreated.class, evt ->
                NetbankingStates.builder().netbanking(Optional.of(evt.getNetbanking()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(NetbankingCommand.UpdateNetbanking.class, (cmd, ctx) ->
                ctx.thenPersist(NetbankingEvent.NetbankingUpdated.builder().netbanking(cmd.getNetbanking()).entityId(entityId()).build()
                        , evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(NetbankingEvent.NetbankingUpdated.class, evt ->
                NetbankingStates.builder().netbanking(Optional.of(evt.getNetbanking()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(NetbankingCommand.DeleteNetbanking.class, (cmd, ctx) ->
                ctx.thenPersist(NetbankingEvent.NetbankingDeleted.builder().netbanking(cmd.getNetbanking()).entityId(entityId()).build(),
                        evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(NetbankingEvent.NetbankingDeleted.class, evt ->
                NetbankingStates.builder().netbanking(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setReadOnlyCommandHandler(NetbankingCommand.NetbankingCurrentState.class, (cmd, ctx) ->
                ctx.reply(state().getNetbanking())
        );

        return behaviorBuilder.build();
    }
}
