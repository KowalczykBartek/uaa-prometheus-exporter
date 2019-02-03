package metrics.collector.parsers;

import io.prometheus.client.Collector;
import metrics.collector.connector.JMXClient;

import javax.management.ObjectInstance;
import java.util.List;

public abstract class Parser {
    public static String APPLICATION_PREFFIX = "cloudfoundy_uaa_";
    protected final JMXClient jmxClient;

    public Parser(JMXClient jmxClient) {
        this.jmxClient = jmxClient;
    }

    public abstract boolean canHandle(String name);

    public abstract void parse(ObjectInstance objectInstance, List<Collector.MetricFamilySamples> collectedMetrics);

    public static long toLong(Object value) {
        if (value == null) {
            return 0l;
        } else if (value instanceof Integer) {
            return Long.valueOf((Integer) value);
        }
        return Long.valueOf((String) value);
    }
}
