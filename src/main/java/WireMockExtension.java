import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WireMockExtension implements BeforeAllCallback, AfterAllCallback, AfterEachCallback {

    private WireMockServer server;

    @Override
    public void beforeAll(ExtensionContext context) {
        var port = getPort(context.getTestClass().orElse(null));
        server = new WireMockServer(wireMockConfig().port(port));
        server.start();
        WireMock.configureFor("localhost", port);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        server.stop();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        WireMock.reset();
    }

    protected int getPort(Class<?> testClass) {
        WireMockTest annotation = getAnnotation(testClass);
        return (annotation != null) ? annotation.port() : 8080;
    }

    protected WireMockTest getAnnotation(Class<?> testClass) {
        return testClass.getAnnotation(WireMockTest.class);
    }
}