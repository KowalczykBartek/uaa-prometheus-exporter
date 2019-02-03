package metrics.collector;

import com.fasterxml.jackson.databind.ObjectMapper;
import metrics.collector.connector.JMXClient;
import metrics.collector.parsers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.ObjectInstance;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Collector extends io.prometheus.client.Collector {
    private static final Logger LOG = LoggerFactory.getLogger(Collector.class);

    private final ObjectMapper mapper;
    private final JMXClient jmxClient;
    private final List<Parser> parsers;

    public Collector(JMXClient jmxClient) {
        mapper = new ObjectMapper();
        this.jmxClient = jmxClient;

        List<Parser> parsers = new ArrayList<>();
        parsers.add(new ClientEndpoint(jmxClient));
        parsers.add(new ServerRequests(jmxClient, mapper));
        parsers.add(new UaaAudit(jmxClient));
        parsers.add(new UserEndpoint(jmxClient));
        parsers.add(new SpringDataSource(jmxClient));

        List<String> names = new ArrayList<>();
        names.add("java.lang:type=Runtime");
        names.add("java.lang:type=OperatingSystem");
        names.add("java.lang:type=GarbageCollector,name=PS Scavenge");
        names.add("java.lang:type=GarbageCollector,name=PS MarkSweep");
        parsers.add(new GenericParser(jmxClient, names));

        //final means barrier isn't it ? even if not, lets pretend that
        this.parsers = parsers;
    }

    @Override
    public List<MetricFamilySamples> collect() {

        LOG.info("Going to collect all metrics over all mbeans, finger crossed");

        List<MetricFamilySamples> collectedMetrics = new ArrayList<>();

        try {
            Set<ObjectInstance> beanSet = jmxClient.queryMBeans(null, null);
            Iterator<ObjectInstance> iterator = beanSet.iterator();
            while (iterator.hasNext()) {
                ObjectInstance next = iterator.next();

                parsers.forEach(parser -> {
                    if (parser.canHandle(next.getObjectName().toString())) {
                        try {
                            parser.parse(next, collectedMetrics);
                        } catch (Exception ex) {
                            LOG.error("Exception occurred during parse processing", ex);
                        }
                    }
                });

            }
            return collectedMetrics;
        } catch (Exception ex) {
            LOG.error("Error occurred during collector run", ex);
            jmxClient.connect();//you can wonder wtf I am doing here !? answer is simple, I am doing good software sir !
            return collectedMetrics;
        }
    }
}
