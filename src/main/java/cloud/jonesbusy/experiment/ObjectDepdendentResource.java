package cloud.jonesbusy.experiment;

import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.external.PerResourcePollingDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import java.util.Set;
import java.util.logging.Logger;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@KubernetesDependent
public class ObjectDepdendentResource extends PerResourcePollingDependentResource<APIModel, ObjectFetcher> {

    private static final Logger LOG = Logger.getLogger(ObjectDepdendentResource.class.getName());

    @RestClient
    ObjectFetcherClient client;

    public ObjectDepdendentResource() {
        super(APIModel.class);
    }

    @Override
    public Set<APIModel> fetchResources(ObjectFetcher primary) {
        LOG.info("Fetching APIModel resources for ObjectFetcher: "
                + primary.getSpec().id());
        APIModel model = client.getObject(primary.getSpec().id());
        LOG.info("Fetched APIModel: " + model.getName());
        return Set.of(model);
    }

    @Override
    protected APIModel desired(ObjectFetcher primary, Context<ObjectFetcher> context) {
        LOG.info("Desired APIModel for ObjectFetcher: " + primary.getSpec().id());
        return new APIModel(primary.getSpec().id(), null, null);
    }
}
