package cloud.jonesbusy.experiment;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KubernetesService {

    public Secret buildSecret(String name, String namespace, String key, String value) {
        return new SecretBuilder()
                .withNewMetadata()
                .withName(name)
                .addToAnnotations("created-by", "KubernetesService")
                .addToLabels("app", "my-app")
                .withNamespace(namespace)
                .endMetadata()
                .addToStringData(key, value)
                .build();
    }
}
