package events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.knoldus.Netbanking;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Created by knoldus on 30/1/17.
 */
public interface NetbankingEvent extends Jsonable, AggregateEvent<NetbankingEvent> {

    @Override
    default AggregateEventTagger<NetbankingEvent> aggregateTag() {
        return NetbankingEventTag.INSTANCE;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class NetbankingCreated implements NetbankingEvent, CompressedJsonable {
        Netbanking netbanking;
        String entityId;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class NetbankingUpdated implements NetbankingEvent, CompressedJsonable {
        Netbanking netbanking;
        String entityId;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class NetbankingDeleted implements NetbankingEvent, CompressedJsonable {
        Netbanking netbanking;
        String entityId;
    }
}
