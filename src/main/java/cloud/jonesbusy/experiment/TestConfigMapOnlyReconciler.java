package cloud.jonesbusy.experiment;

import cloud.jonesbusy.experiment.dependent.ConfigMapOnlyDependentResource;
import cloud.jonesbusy.experiment.dependent.SecretDependentResource;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.Secret;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.Workflow;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Workflow(
        dependents = {
            @Dependent(type = ConfigMapOnlyDependentResource.class),
            @Dependent(type = SecretDependentResource.class),
        })
@ControllerConfiguration
public class TestConfigMapOnlyReconciler implements Reconciler<ConfigMapOnlyResource> {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(TestConfigMapOnlyReconciler.class);

    @Override
    public UpdateControl<ConfigMapOnlyResource> reconcile(
            ConfigMapOnlyResource primary, Context<ConfigMapOnlyResource> context) throws Exception {
        LOG.info("Reconciling Test: {}", primary.getMetadata().getName());
        Optional<ConfigMap> configMapOptional = context.getSecondaryResource(ConfigMap.class);
        Optional<Secret> secretOptional = context.getSecondaryResource(Secret.class);
        if (configMapOptional.isEmpty() || secretOptional.isEmpty()) {
            LOG.info(
                    "No external resource found for ConfigMapOnlyResource: {}",
                    primary.getMetadata().getName());
            return UpdateControl.noUpdate();
        }
        LOG.info(
                "External resource found for ConfigMapOnlyResource: {}",
                primary.getMetadata().getName());
        return UpdateControl.patchResource(createForStatusUpdate(primary));
    }

    private ConfigMapOnlyResource createForStatusUpdate(ConfigMapOnlyResource primary) {
        ConfigMapOnlyResource res = new ConfigMapOnlyResource();
        res.setMetadata(new ObjectMetaBuilder()
                .withName(primary.getMetadata().getName())
                .withNamespace(primary.getMetadata().getNamespace())
                .build());
        return res;
    }
}
