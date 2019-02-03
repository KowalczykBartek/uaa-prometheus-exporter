package metrics.collector.parsers;

import io.prometheus.client.Collector;
import metrics.collector.connector.JMXClient;

import javax.management.ObjectInstance;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class SpringDataSource extends Parser {

    public SpringDataSource(JMXClient jmxClient) {
        super(jmxClient);
    }

    @Override
    public boolean canHandle(String name) {
        return "spring.application:type=DataSource,name=dataSource".equals(name);
    }

    @Override
    public void parse(ObjectInstance objectInstance, List<Collector.MetricFamilySamples> collectedMetrics) {
        long maxActive = toLong(jmxClient.getAttribute(objectInstance.getObjectName(), "MaxActive"));
        Collector.MetricFamilySamples.Sample maxActiveMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "spring_datasource_max_active", emptyList(), emptyList(), maxActive);

        long maxIdle = toLong(jmxClient.getAttribute(objectInstance.getObjectName(), "MaxIdle"));
        Collector.MetricFamilySamples.Sample maxIdleMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "spring_datasource_max_idle", emptyList(), emptyList(), maxIdle);

        long numActive = toLong(jmxClient.getAttribute(objectInstance.getObjectName(), "NumActive"));
        Collector.MetricFamilySamples.Sample numActiveMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "spring_datasource_num_active", emptyList(), emptyList(), numActive);

        long numIdle = toLong(jmxClient.getAttribute(objectInstance.getObjectName(), "NumIdle"));
        Collector.MetricFamilySamples.Sample numIdleMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "spring_datasource_num_idle", emptyList(), emptyList(), numIdle);

        List<Collector.MetricFamilySamples.Sample> samples = new ArrayList<>();
        samples.add(maxActiveMetric);
        samples.add(maxIdleMetric);
        samples.add(numActiveMetric);
        samples.add(numIdleMetric);
        collectedMetrics.add(new Collector.MetricFamilySamples("spring.application", Collector.Type.SUMMARY, "DataSource", samples));
    }
}
