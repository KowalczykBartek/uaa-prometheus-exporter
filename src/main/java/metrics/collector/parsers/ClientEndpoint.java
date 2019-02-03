package metrics.collector.parsers;

import io.prometheus.client.Collector;
import metrics.collector.connector.JMXClient;

import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class ClientEndpoint extends Parser {

    public ClientEndpoint(JMXClient jmxClient) {
        super(jmxClient);
    }

    @Override
    public boolean canHandle(String name) {
        return "cloudfoundry.identity:name=ClientEndpoint".equals(name);
    }

    @Override
    public void parse(ObjectInstance objectInstance, List<Collector.MetricFamilySamples> collectedMetrics) {
        final ObjectName objectName = objectInstance.getObjectName();

        Long clientDeletes = toLong(jmxClient.getAttribute(objectName, "ClientDeletes"));
        Collector.MetricFamilySamples.Sample clientDeletesMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "client_endpoint_client_deletes", emptyList(), emptyList(), clientDeletes);

        long clientSecretChanges = toLong(jmxClient.getAttribute(objectName, "ClientSecretChanges"));
        Collector.MetricFamilySamples.Sample clientSecretChangesMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "client_endpoint_client_secret_changes", emptyList(), emptyList(), clientSecretChanges);

        long clientUpdates = toLong(jmxClient.getAttribute(objectName, "ClientUpdates"));
        Collector.MetricFamilySamples.Sample clientUpdatesMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "client_endpoint_client_updates_metric", emptyList(), emptyList(), clientUpdates);

        long totalClients = toLong(jmxClient.getAttribute(objectName, "TotalClients"));
        Collector.MetricFamilySamples.Sample totalClientsMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "client_endpoint_total_clients", emptyList(), emptyList(), totalClients);

        List<Collector.MetricFamilySamples.Sample> samples = new ArrayList<>();
        samples.add(clientDeletesMetric);
        samples.add(clientSecretChangesMetric);
        samples.add(clientUpdatesMetric);
        samples.add(totalClientsMetric);
        collectedMetrics.add(new Collector.MetricFamilySamples("cloudfoundry.identity", Collector.Type.SUMMARY, "ClientEndpoint", samples));
    }
}
