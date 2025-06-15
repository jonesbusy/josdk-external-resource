package cloud.jonesbusy.experiment.dependent;

import cloud.jonesbusy.experiment.ConfigMapOnlyResource;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KubernetesDependent
public class ConfigMapOnlyDependentResource extends CRUDKubernetesDependentResource<ConfigMap, ConfigMapOnlyResource> {

    public static final Logger LOG = LoggerFactory.getLogger(ConfigMapOnlyDependentResource.class);

    public ConfigMapOnlyDependentResource() {
        super(ConfigMap.class);
    }

    @Override
    protected ConfigMap desired(ConfigMapOnlyResource primary, Context<ConfigMapOnlyResource> context) {
        LOG.info(
                "Desired ConfigMap for ConfigMapOnlyResource: {}",
                primary.getMetadata().getName());
        return new ConfigMapBuilder()
                .withNewMetadata()
                .withName(primary.getMetadata().getName())
                .withNamespace(primary.getMetadata().getNamespace())
                .endMetadata()
                .addToData("foo", "bar")
                .build();
    }
}
