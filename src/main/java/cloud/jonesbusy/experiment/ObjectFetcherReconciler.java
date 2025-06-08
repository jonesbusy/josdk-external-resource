package cloud.jonesbusy.experiment;

import cloud.jonesbusy.experiment.dependent.ConfigMapDependendResource;
import cloud.jonesbusy.experiment.dependent.ExternalDependentResource;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.Workflow;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reconciler for the {@link ObjectFetcherResource}.
 */
@Workflow(
        dependents = {
            @Dependent(type = ExternalDependentResource.class, name = "external-resource"),
            @Dependent(type = ConfigMapDependendResource.class, name = "configmap", dependsOn = "external-resource"),
        })
@ControllerConfiguration
@SuppressWarnings("unused")
public class ObjectFetcherReconciler implements Reconciler<ObjectFetcherResource> {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(ObjectFetcherReconciler.class);

    @Override
    public UpdateControl<ObjectFetcherResource> reconcile(
            ObjectFetcherResource primary, Context<ObjectFetcherResource> context) throws Exception {
        LOG.info("Reconciling ObjectFetch: {}", primary.getMetadata().getName());
        return UpdateControl.noUpdate();
    }
}
