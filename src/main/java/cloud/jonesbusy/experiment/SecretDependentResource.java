package cloud.jonesbusy.experiment;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.Creator;
import io.javaoperatorsdk.operator.processing.dependent.Matcher;
import io.javaoperatorsdk.operator.processing.dependent.external.PerResourcePollingDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import io.javaoperatorsdk.operator.processing.event.ResourceID;
import io.javaoperatorsdk.operator.processing.event.source.SecondaryToPrimaryMapper;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KubernetesDependent
public class SecretDependentResource extends PerResourcePollingDependentResource<Secret, MySuperCR>
        implements SecondaryToPrimaryMapper<Secret>, Matcher<Secret, MySuperCR>, Creator<Secret, MySuperCR> {

    public static final String NAME = "secret";

    public static final Logger LOG = LoggerFactory.getLogger(SecretDependentResource.class);

    public SecretDependentResource() {
        super(Secret.class);
    }

    @Override
    public Result<Secret> match(Secret resource, MySuperCR primary, Context<MySuperCR> context) {
        LOG.info(
                "Matching secret {} for primary {}",
                resource.getMetadata().getName(),
                primary.getMetadata().getName());
        return Result.nonComputed(resource.getMetadata().getName().equals("secret"));
    }

    @Override
    public Secret create(Secret secret, MySuperCR primary, Context<MySuperCR> context) {
        LOG.info("Creating secret {}", secret.getMetadata().getName());
        return new SecretBuilder()
                .withNewMetadata()
                .withName("my-secret")
                .withNamespace(primary.getMetadata().getNamespace())
                .endMetadata()
                .addToData("username", "new")
                .addToData("password", "new")
                .build();
    }

    @Override
    public Secret desired(MySuperCR primary, Context<MySuperCR> context) {
        LOG.info("Desired secret {}", primary.getMetadata().getName());
        return new SecretBuilder()
                .withNewMetadata()
                .withName("my-secret")
                .withNamespace(primary.getMetadata().getNamespace())
                .endMetadata()
                .addToData("username", "empty")
                .addToData("password", "empty")
                .build();
    }

    @Override
    public Set<ResourceID> toPrimaryResourceIDs(Secret resource) {
        LOG.info(
                "Mapping secret {} to primary resource ID",
                resource.getMetadata().getName());
        return Set.of(new ResourceID("my-cr", resource.getMetadata().getNamespace()));
    }

    @Override
    public Set<Secret> fetchResources(MySuperCR primary) {
        LOG.info("Fetching secrets for {}", primary.getMetadata().getName());
        return Set.of(new SecretBuilder()
                .withNewMetadata()
                .withName("my-secret")
                .withNamespace(primary.getMetadata().getNamespace())
                .endMetadata()
                .addToData("username", "The username")
                .addToData("password", "The password")
                .build());
    }
}
