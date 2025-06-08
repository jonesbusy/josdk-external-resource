package cloud.jonesbusy.experiment.dependent;

import cloud.jonesbusy.experiment.ObjectFetcherClient;
import cloud.jonesbusy.experiment.ObjectFetcherResource;
import cloud.jonesbusy.experiment.ObjectModel;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.external.PerResourcePollingDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import java.util.Set;
import java.util.logging.Logger;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@KubernetesDependent
public class ExternalDependentResource extends PerResourcePollingDependentResource<ObjectModel, ObjectFetcherResource> {

    private static final Logger LOG = Logger.getLogger(ExternalDependentResource.class.getName());

    @RestClient
    ObjectFetcherClient client;

    public ExternalDependentResource() {
        super(ObjectModel.class);
    }

    @Override
    public Set<ObjectModel> fetchResources(ObjectFetcherResource primary) {
        LOG.info("Fetching APIModel resources for ObjectFetcher: "
                + primary.getSpec().id());
        ObjectModel model = client.getObject(primary.getSpec().id());
        LOG.info("Fetched APIModel: " + model.getName());
        return Set.of(model);
    }

    @Override
    protected ObjectModel desired(ObjectFetcherResource primary, Context<ObjectFetcherResource> context) {
        LOG.info("Desired ObjectModel for ObjectFetcher: " + primary.getSpec().id());
        return new ObjectModel(primary.getSpec().id(), null, null);
    }
}
