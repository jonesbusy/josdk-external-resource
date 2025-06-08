package cloud.jonesbusy.experiment;

import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.Secret;
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
            @Dependent(type = SecretDependentResource.class, name = SecretDependentResource.NAME),
        })
@ControllerConfiguration
public class MySuperCRReconcillier implements Reconciler<MySuperCR> {

    public static final Logger LOG = LoggerFactory.getLogger(MySuperCRReconcillier.class);

    @Override
    public UpdateControl<MySuperCR> reconcile(MySuperCR mySuperCR, Context<MySuperCR> context) throws Exception {
        LOG.info("Reconciling MySuperCR: {}", mySuperCR.getMetadata().getName());
        Secret secret = context.getSecondaryResource(Secret.class).orElseThrow();
        LOG.info("Found secret: {}", secret.getMetadata().getName());
        LOG.info(
                "Secret data: username={}, password={}",
                secret.getData().get("username"),
                secret.getData().get("password"));

        return UpdateControl.patchResource(createForStatusUpdate(mySuperCR));
    }

    private MySuperCR createForStatusUpdate(MySuperCR primary) {
        MySuperCR res = new MySuperCR();
        res.setMetadata(new ObjectMetaBuilder()
                .withName(primary.getMetadata().getName())
                .withNamespace(primary.getMetadata().getNamespace())
                .build());
        return res;
    }
}
