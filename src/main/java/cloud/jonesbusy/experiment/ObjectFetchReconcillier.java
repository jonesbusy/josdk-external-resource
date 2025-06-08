package cloud.jonesbusy.experiment;

import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.Workflow;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Workflow(
        dependents = {
            @Dependent(type = ObjectDepdendentResource.class, name = "dependent-resource"),
            @Dependent(type = ConfigMapDependendResource.class, name = "configmap", dependsOn = "dependent-resource"),
        })
@ControllerConfiguration
public class ObjectFetchReconcillier implements Reconciler<ObjectFetcher> {

    public static final Logger LOG = LoggerFactory.getLogger(ObjectFetchReconcillier.class);

    @Override
    public UpdateControl<ObjectFetcher> reconcile(ObjectFetcher primary, Context<ObjectFetcher> context)
            throws Exception {
        LOG.info("Reconciling ObjectFetch: {}", primary.getMetadata().getName());
        return UpdateControl.noUpdate();
    }
}
