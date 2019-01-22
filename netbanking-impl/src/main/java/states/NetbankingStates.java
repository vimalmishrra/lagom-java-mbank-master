package states;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.knoldus.Netbanking;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import java.util.Optional;

@Value
@Builder
@JsonDeserialize
@AllArgsConstructor
public class NetbankingStates implements CompressedJsonable {
    Optional<Netbanking> netbanking;
    String timestamp;
}
