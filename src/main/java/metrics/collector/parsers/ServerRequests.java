package metrics.collector.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.prometheus.client.Collector;
import metrics.collector.connector.JMXClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.ObjectInstance;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class ServerRequests extends Parser {
    private static final Logger LOG = LoggerFactory.getLogger(ServerRequests.class);
    private final ObjectMapper mapper;

    public ServerRequests(JMXClient jmxClient, ObjectMapper mapper) {
        super(jmxClient);
        this.mapper = mapper;
    }

    @Override
    public boolean canHandle(String name) {
        return "cloudfoundry.identity:name=ServerRequests".equals(name);
    }

    @Override
    public void parse(ObjectInstance objectInstance, List<Collector.MetricFamilySamples> collectedMetrics) {
        Object object = jmxClient.getAttribute(objectInstance.getObjectName(), "Globals");

        Map globals = null;
        try {
            globals = mapper.readValue(object.toString(), Map.class);
        } catch (IOException e) {
            LOG.error("Unable to parse", e);
            return;
        }

        Map detailed = (Map) globals.get("detailed");
        Map success = (Map) detailed.get("SUCCESS");

        if (success == null || detailed == null) {
            return;
        }

        long count = toLong(success.get("count"));
        Collector.MetricFamilySamples.Sample countMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "server_requests_count", emptyList(), emptyList(), count);

        double averageTime = (double) (success.get("averageTime"));
        Collector.MetricFamilySamples.Sample averageTimeMetrics = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "server_requests_average_time", emptyList(), emptyList(), averageTime);

        long databaseQueryCount = toLong(success.get("databaseQueryCount"));
        Collector.MetricFamilySamples.Sample databaseQueryCountMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "server_requests_database_query_count", emptyList(), emptyList(), databaseQueryCount);

        double averageDatabaseQueryTime = (double) success.get("averageDatabaseQueryTime");
        Collector.MetricFamilySamples.Sample averageDatabaseQueryTimeMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "server_requests_average_database_query_time", emptyList(), emptyList(), averageDatabaseQueryTime);

        List<Collector.MetricFamilySamples.Sample> samples = new ArrayList<>();
        samples.add(countMetric);
        samples.add(averageTimeMetrics);
        samples.add(databaseQueryCountMetric);
        samples.add(averageDatabaseQueryTimeMetric);

        collectedMetrics.add(new Collector.MetricFamilySamples("cloudfoundry.identity", Collector.Type.SUMMARY, "ServerRequests", samples));
    }

}
