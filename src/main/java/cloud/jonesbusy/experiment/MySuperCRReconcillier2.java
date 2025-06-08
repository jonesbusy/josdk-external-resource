package cloud.jonesbusy.experiment;

import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
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
            @Dependent(type = UsernamePasswordDependendResource.class, name = "username"),
            @Dependent(type = SecretDependentResource2.class, name = "secret", dependsOn = "username"),
        })
@ControllerConfiguration
public class MySuperCRReconcillier2 implements Reconciler<MySuperCR2> {

    public static final Logger LOG = LoggerFactory.getLogger(MySuperCRReconcillier2.class);

    @Override
    public UpdateControl<MySuperCR2> reconcile(MySuperCR2 primary, Context<MySuperCR2> context) throws Exception {
        LOG.info("Reconciling MySuperCR2: {}", primary.getMetadata().getName());

        boolean ready =
                context.managedWorkflowAndDependentResourceContext() // accessing workflow reconciliation results
                        .getWorkflowReconcileResult()
                        .orElseThrow()
                        .allDependentResourcesReady();
        LOG.info("All dependent resources ready: {}", ready);
        if (!ready) {
            LOG.info("Dependent resources are not ready, skipping status update.");
            return UpdateControl.patchStatus(createForStatusUpdate(primary, "Not Ready"));
        } else {
            LOG.info("Dependent resources are ready, proceeding with status update.");
            return UpdateControl.patchStatus(createForStatusUpdate(primary, "Ready"));
        }
    }

    private MySuperCR2 createForStatusUpdate(MySuperCR2 primary, String status) {
        MySuperCR2 res = new MySuperCR2();
        res.setStatus(new SimpleStatus(status));
        res.setMetadata(new ObjectMetaBuilder()
                .withName(primary.getMetadata().getName())
                .withNamespace(primary.getMetadata().getNamespace())
                .build());
        return res;
    }
}
