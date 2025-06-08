package cloud.jonesbusy.experiment;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KubernetesDependent
public class SecretDependentResource2 extends CRUDKubernetesDependentResource<Secret, MySuperCR2> {

    public static final Logger LOG = LoggerFactory.getLogger(SecretDependentResource2.class);

    public SecretDependentResource2() {
        super(Secret.class);
    }

    @Override
    protected Secret desired(MySuperCR2 primary, Context<MySuperCR2> context) {
        LOG.info("Desired secret for MySuperCR2: {}", primary.getMetadata().getName());
        UsernamePassword usernamePassword =
                context.getSecondaryResource(UsernamePassword.class).orElseThrow();
        String remoteUsername = usernamePassword.getUsername();
        String remotePassword = usernamePassword.getPassword();
        LOG.info("Using remote username: {}, password: {}", remoteUsername, remotePassword);
        return new SecretBuilder()
                .withNewMetadata()
                .withName("my-secret")
                .withNamespace(primary.getMetadata().getNamespace())
                .endMetadata()
                .addToData(
                        "username", Base64.getEncoder().encodeToString(remoteUsername.getBytes(StandardCharsets.UTF_8)))
                .addToData(
                        "password", Base64.getEncoder().encodeToString(remotePassword.getBytes(StandardCharsets.UTF_8)))
                .build();
    }

    @Override
    public Result<Secret> match(Secret actual, MySuperCR2 primary, Context<MySuperCR2> context) {
        UsernamePassword usernamePassword =
                context.getSecondaryResource(UsernamePassword.class).orElseThrow();
        String remoteUsername = usernamePassword.getUsername();
        String remotePassword = usernamePassword.getPassword();
        String actualUsername =
                new String(Base64.getDecoder().decode(actual.getData().get("username")), StandardCharsets.UTF_8);
        String actualPassword =
                new String(Base64.getDecoder().decode(actual.getData().get("password")), StandardCharsets.UTF_8);
        if (!remoteUsername.equals(actualUsername) || !remotePassword.equals(actualPassword)) {
            LOG.info(
                    "Secret data does not match: expected username={}, password={}, actual username={}, password={}",
                    remoteUsername,
                    remotePassword,
                    actualUsername,
                    actualPassword);
            return Result.nonComputed(false);
        }
        LOG.info("Secret data matches for MySuperCR2: {}", primary.getMetadata().getName());
        return Result.nonComputed(true);
    }
}
