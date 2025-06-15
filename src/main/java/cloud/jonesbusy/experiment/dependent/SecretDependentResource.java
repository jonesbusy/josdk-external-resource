package cloud.jonesbusy.experiment.dependent;

import cloud.jonesbusy.experiment.ConfigMapOnlyResource;
import cloud.jonesbusy.experiment.KubernetesService;
import io.fabric8.kubernetes.api.model.Secret;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KubernetesDependent
public class SecretDependentResource extends CRUDKubernetesDependentResource<Secret, ConfigMapOnlyResource> {

    public static final Logger LOG = LoggerFactory.getLogger(SecretDependentResource.class);

    @Inject
    KubernetesService kubernetesService;

    public SecretDependentResource() {
        super(Secret.class);
    }

    @Override
    protected Secret desired(ConfigMapOnlyResource primary, Context<ConfigMapOnlyResource> context) {
        LOG.info(
                "Desired Secret for ConfigMapOnlyResource: {}",
                primary.getMetadata().getName());
        return kubernetesService.buildSecret(
                primary.getMetadata().getName(), primary.getMetadata().getNamespace(), "foo", "bar");
    }
}
