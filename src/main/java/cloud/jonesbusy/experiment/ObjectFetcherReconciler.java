package cloud.jonesbusy.experiment;

import cloud.jonesbusy.experiment.dependent.ConfigMapDependendResource;
import cloud.jonesbusy.experiment.dependent.ExternalDependentResource;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.Workflow;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reconciler for the {@link ObjectFetcherResource}.
 */
@Workflow(
        dependents = {
            @Dependent(type = ExternalDependentResource.class, name = "external-resource", dependsOn = "configmap"),
            @Dependent(type = ConfigMapDependendResource.class, name = "configmap"),
        })
@ControllerConfiguration(finalizerName = "foo")
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
        Optional<ObjectModel> externalResource = context.getSecondaryResource(ObjectModel.class, "external-resource");
        if (externalResource.isEmpty()) {
            LOG.info(
                    "No external resource found for ObjectFetcher: {}",
                    primary.getMetadata().getName());
            return UpdateControl.noUpdate();
        }
        LOG.info(
                "External resource found for ObjectFetcher: {}",
                primary.getMetadata().getName());
        if (!Objects.equals(
                primary.getStatus().observedGeneration(), primary.getMetadata().getGeneration())) {
            LOG.info("Observed generation does not match for ObjectFetcher");
            return UpdateControl.patchStatus(createForStatusUpdate(primary, externalResource.get()));
        }
        LOG.info("ObjectFetcher {} is up to date", primary.getMetadata().getName());
        return UpdateControl.noUpdate();
    }

    private ObjectFetcherResource createForStatusUpdate(ObjectFetcherResource primary, ObjectModel externalResource) {
        ObjectFetcherResource res = new ObjectFetcherResource();
        res.setMetadata(new ObjectMetaBuilder()
                .withName(primary.getMetadata().getName())
                .withNamespace(primary.getMetadata().getNamespace())
                .build());
        ObjectFetcherStatus status =
                new ObjectFetcherStatus("CREATED", primary.getMetadata().getGeneration());
        res.setStatus(status);
        return res;
    }
}
