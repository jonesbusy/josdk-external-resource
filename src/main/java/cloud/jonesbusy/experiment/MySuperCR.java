package cloud.jonesbusy.experiment;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("cloud.jonesbusy.external-resource")
@Version("v1")
public class MySuperCR extends CustomResource<Void, Void> implements Namespaced {}
