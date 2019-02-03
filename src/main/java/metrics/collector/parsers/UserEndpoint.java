package metrics.collector.parsers;

import io.prometheus.client.Collector;
import metrics.collector.connector.JMXClient;

import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class UserEndpoint extends Parser {

    public UserEndpoint(JMXClient jmxClient) {
        super(jmxClient);
    }

    @Override
    public boolean canHandle(String name) {
        return "cloudfoundry.identity:name=UserEndpoint".equals(name);
    }

    @Override
    public void parse(ObjectInstance objectInstance, List<Collector.MetricFamilySamples> collectedMetrics) {
        final ObjectName objectName = objectInstance.getObjectName();
        long totalUsers = toLong(jmxClient.getAttribute(objectName, "TotalUsers"));
        Collector.MetricFamilySamples.Sample totalUsersMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "user_endpoint_total_users", emptyList(), emptyList(), totalUsers);

        long userDeletes = toLong(jmxClient.getAttribute(objectName, "UserDeletes"));
        Collector.MetricFamilySamples.Sample userDeletesMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "user_endpoint_user_deletes", emptyList(), emptyList(), userDeletes);

        long userUpdates = toLong(jmxClient.getAttribute(objectName, "UserUpdates"));
        Collector.MetricFamilySamples.Sample userUpdatesMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "user_endpoint_user_updates", emptyList(), emptyList(), userUpdates);

        List<Collector.MetricFamilySamples.Sample> samples = new ArrayList<>();
        samples.add(totalUsersMetric);
        samples.add(userDeletesMetric);
        samples.add(userUpdatesMetric);
        collectedMetrics.add(new Collector.MetricFamilySamples("cloudfoundry.identity", Collector.Type.SUMMARY, "UserEndpoint", samples));
    }
}
