package cloud.jonesbusy.experiment;

import com.oracle.svm.core.annotate.Delete;
import io.fabric8.kubernetes.api.model.Secret;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Deleter;
import io.javaoperatorsdk.operator.api.reconciler.dependent.managed.ConfiguredDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.Creator;
import io.javaoperatorsdk.operator.processing.dependent.external.PerResourcePollingDependentResource;
import io.javaoperatorsdk.operator.processing.event.ResourceID;
import io.javaoperatorsdk.operator.processing.event.source.SecondaryToPrimaryMapper;

import java.util.Optional;
import java.util.Set;

public class SecretDependentResource extends PerResourcePollingDependentResource<Secret, MySuperCR>
        implements ConfiguredDependentResource<ResourcePollerConfig>,
        Creator<Secret, MySuperCR>, SecondaryToPrimaryMapper<Secret>, Deleter<Secret> {

    @Override
    public void configureWith(ResourcePollerConfig config) {
        setPollingPeriod(config.pollPeriod());

    }

    @Override
    public Optional<ResourcePollerConfig> configuration() {
        return Optional.of(new ResourcePollerConfig(getPollingPeriod()));
    }

    @Override
    public Secret create(Secret secret, MySuperCR mySuperCR, Context<MySuperCR> context) {
        return null;
    }

    @Override
    public Set<ResourceID> toPrimaryResourceIDs(Secret secret) {
        return Set.of();
    }

    @Override
    public Set<Secret> fetchResources(MySuperCR mySuperCR) {
        return Set.of();
    }
}