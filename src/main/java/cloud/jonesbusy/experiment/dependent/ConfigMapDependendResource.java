package cloud.jonesbusy.experiment.dependent;

import cloud.jonesbusy.experiment.ObjectFetcherResource;
import cloud.jonesbusy.experiment.ObjectModel;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KubernetesDependent
public class ConfigMapDependendResource extends CRUDKubernetesDependentResource<ConfigMap, ObjectFetcherResource> {

    public static final Logger LOG = LoggerFactory.getLogger(ConfigMapDependendResource.class);

    public ConfigMapDependendResource() {
        super(ConfigMap.class);
    }

    @Override
    protected ConfigMap desired(ObjectFetcherResource primary, Context<ObjectFetcherResource> context) {
        ObjectModel model = context.getSecondaryResource(ObjectModel.class).orElseThrow();
        LOG.info(
                "Desired ConfigMap for ObjectFetcher: {}", primary.getMetadata().getName());
        return new ConfigMapBuilder()
                .withNewMetadata()
                .withName("my-configmap")
                .withNamespace(primary.getMetadata().getNamespace())
                .endMetadata()
                .addToData("id", primary.getSpec().id())
                .addToData("name", model.getName())
                .build();
    }
}
