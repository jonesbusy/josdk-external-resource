package cloud.jonesbusy.experiment.dependent;

import cloud.jonesbusy.experiment.ConfigMapOnlyResource;
import cloud.jonesbusy.experiment.KubernetesService;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.dependent.GarbageCollected;
import io.javaoperatorsdk.operator.processing.dependent.Creator;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.BooleanWithUndefined;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependentResource;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@KubernetesDependent(createResourceOnlyIfNotExistingWithSSA = false, useSSA = BooleanWithUndefined.TRUE)
public class SecretDependentResource extends KubernetesDependentResource<Secret, ConfigMapOnlyResource> implements Creator<Secret, ConfigMapOnlyResource>, GarbageCollected<ConfigMapOnlyResource> {

    public static final Logger LOG = LoggerFactory.getLogger(SecretDependentResource.class);

    @Inject
    KubernetesService kubernetesService;

    public SecretDependentResource() {
        super(Secret.class);
    }

    @Override
    protected Secret handleCreate(Secret desired, ConfigMapOnlyResource primary, Context<ConfigMapOnlyResource> context) {
        handleUpdate(desired, desired, primary, context);
        return super.handleCreate(desired, primary, context);
    }

    @Override
    protected Secret desired(ConfigMapOnlyResource primary, Context<ConfigMapOnlyResource> context) {
        LOG.info(
                "Desired Secret for ConfigMapOnlyResource: {}",
                primary.getMetadata().getName());
        return kubernetesService.buildSecret(
                primary.getMetadata().getName(), primary.getMetadata().getNamespace(), "foo", "bar", labels(primary));
    }

    private Map<String, String> labels(ConfigMapOnlyResource resource) {
        return Map.of(
                "app.kubernetes.io/managed-by", "on-prem-operator",
                "app.kubernetes.io/instance", resource.getMetadata().getName(),
                "app.kubernetes.io/component", "ldap-service-account");
    }

}
