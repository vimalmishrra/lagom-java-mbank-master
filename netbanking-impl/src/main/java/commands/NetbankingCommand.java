package commands;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.knoldus.Netbanking;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

/**
 * Created by knoldus on 30/1/17.
 */
public interface NetbankingCommand extends Jsonable {

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class CreateNetbanking implements NetbankingCommand, PersistentEntity.ReplyType<Done> {
        Netbanking netbanking;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class UpdateNetbanking implements NetbankingCommand, PersistentEntity.ReplyType<Done> {
        Netbanking netbanking;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class DeleteNetbanking implements NetbankingCommand, PersistentEntity.ReplyType<Done> {
        Netbanking netbanking;
    }

    @JsonDeserialize
    final class NetbankingCurrentState implements NetbankingCommand, PersistentEntity.ReplyType<Optional<Netbanking>> {}
}
