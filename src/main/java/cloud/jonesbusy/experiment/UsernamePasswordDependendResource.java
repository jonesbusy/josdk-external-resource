package cloud.jonesbusy.experiment;

import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.external.PerResourcePollingDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import java.time.Duration;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KubernetesDependent
public class UsernamePasswordDependendResource
        extends PerResourcePollingDependentResource<UsernamePassword, MySuperCR2> {

    public static final Logger LOG = LoggerFactory.getLogger(UsernamePasswordDependendResource.class);

    public UsernamePasswordDependendResource() {
        super(UsernamePassword.class, Duration.ofSeconds(1));
    }

    @Override
    public Set<UsernamePassword> fetchResources(MySuperCR2 mySuperCR2) {
        String username = "my-username";
        String password = "my-password";
        LOG.info(
                "Fetching UsernamePassword resources for MySuperCR2: {}",
                mySuperCR2.getMetadata().getName());
        return Set.of(new UsernamePassword(username, password));
    }

    @Override
    protected UsernamePassword desired(MySuperCR2 primary, Context<MySuperCR2> context) {
        String username = "my-username";
        String password = "my-password";
        LOG.info(
                "Desired UsernamePassword for MySuperCR2: {}",
                primary.getMetadata().getName());
        return new UsernamePassword(username, password);
    }
}
