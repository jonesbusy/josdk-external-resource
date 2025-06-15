package cloud.jonesbusy.experiment;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

@ApplicationScoped
public class KubernetesService {

    public Secret buildSecret(String name, String namespace, String key, String value, Map<String, String> labels) {
        return new SecretBuilder()
                .withNewMetadata()
                .withName(name)
                .addToAnnotations("created-by", "KubernetesService")
                .addToLabels(labels)
                .withNamespace(namespace)
                .endMetadata()
                .addToStringData(key, value)
                .build();
    }

    public ConfigMap buildConfigMap(String name, String namespace, Map<String, String> labels) {
        return new ConfigMapBuilder()
                .withNewMetadata()
                .withName(name)
                .addToAnnotations("created-by", "KubernetesService")
                .addToLabels("app", "my-app")
                .withNamespace(namespace)
                .endMetadata()
                .addToData("foo", "bar")
                .build();
    }

}
