package metrics.collector;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;
import metrics.collector.connector.JMXClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) throws IOException {
        final String host = getOrDefault(System.getenv("JMX_HOST"), "localhost");
        final Integer port = Integer.valueOf(getOrDefault(System.getenv("JMX_PORT"), "9876"));
        final Integer httpPort = Integer.valueOf(getOrDefault(System.getenv("HTTP_PORT"), "9090"));

        JMXClient jmxClient = new JMXClient(host, port);
        CollectorRegistry.defaultRegistry.register(new Collector(jmxClient));
        HTTPServer server = new HTTPServer(httpPort);
        LOG.info("Listening ... port 9090 service on endpoint /metrics");
    }

    static String getOrDefault(String value, String defaultValue) {
        return value == null ? defaultValue : value;
    }
}
