package cloud.jonesbusy.experiment;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

/**
 * Custom resource definition for ObjectFetcher.
 * This class represents the primary resource that will be reconciled by the operator.
 * It is namespaced and has a specific group and version.
 */
@Group("cloud.jonesbusy.external-resource")
@Version("v1")
public class ObjectFetcherResource extends CustomResource<ObjectFetcherSpec, Void> implements Namespaced {}
