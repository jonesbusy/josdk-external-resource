package cloud.jonesbusy.experiment;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KubernetesDependent
public class ConfigMapDependendResource extends CRUDKubernetesDependentResource<ConfigMap, ObjectFetcher> {

    public static final Logger LOG = LoggerFactory.getLogger(ConfigMapDependendResource.class);

    public ConfigMapDependendResource() {
        super(ConfigMap.class);
    }

    @Override
    protected ConfigMap desired(ObjectFetcher primary, Context<ObjectFetcher> context) {
        APIModel model = context.getSecondaryResource(APIModel.class).orElseThrow();
        LOG.info(
                "Desired ConfigMap for ObjectFetcher: {}", primary.getMetadata().getName());
        return new ConfigMapBuilder()
                .withNewMetadata()
                .withName("my-configmap")
                .withNamespace(primary.getMetadata().getNamespace())
                .endMetadata()
                .addToData("id", model.getId())
                .addToData("name", model.getName())
                .build();
    }
}
