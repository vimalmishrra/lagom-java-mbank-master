package events;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

public class NetbankingEventTag {

    public static final AggregateEventTag<NetbankingEvent> INSTANCE = AggregateEventTag.of(NetbankingEvent.class);
}
