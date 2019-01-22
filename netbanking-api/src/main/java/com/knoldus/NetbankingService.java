package com.knoldus;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.util.Optional;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.transport.Method.*;

public interface NetbankingService extends Service {
    /**
     * @param id
     * @return
     */
    ServiceCall<NotUsed, Optional<Netbanking>> netbanking(String id);

    /**
     * @return
     */
    ServiceCall<Netbanking, Done> newNetbanking();

    /**
     * @param id
     * @return
     */
    ServiceCall<Netbanking, Done> updateNetbanking(String id);

    /**
     * @param id
     * @return
     */
    ServiceCall<NotUsed, Done> deleteNetbanking(String id);


    /**
     * @return
     */
    @Override
    default Descriptor descriptor() {

        return named("netbanking").withCalls(
                restCall(GET, "/api/netbanking/:id", this::netbanking),
                restCall(POST, "/api/new-netbanking", this::newNetbanking),
                restCall(PUT, "/api/update-netbanking/:id", this::updateNetbanking),
                restCall(DELETE, "/api/delete-netbanking/:id", this::deleteNetbanking)
                //  restCall(GET, "/api/user/get-all-netbanking", this::getAllMovie)
        ).withAutoAcl(true);
    }
}
