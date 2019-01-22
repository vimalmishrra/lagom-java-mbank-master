import com.google.inject.AbstractModule;
import com.knoldus.NetbankingService;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

public class NetbankingModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(serviceBinding(NetbankingService.class,NetbankingServiceImpl.class));
    }
}
