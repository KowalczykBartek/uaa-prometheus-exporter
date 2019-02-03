package metrics.collector.parsers;

import com.google.common.base.CaseFormat;
import io.prometheus.client.Collector;
import metrics.collector.connector.JMXClient;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.ObjectInstance;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.OpenType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;

/**
 * Should at least try to parse stuff from java.lang
 */
public class GenericParser extends Parser {
    final List<String> names;

    public GenericParser(JMXClient jmxClient, List<String> names) {
        super(jmxClient);
        this.names = names;
    }

    @Override
    public boolean canHandle(String name) {
        return names.contains(name);
    }

    @Override
    public void parse(ObjectInstance objectInstance, List<Collector.MetricFamilySamples> collectedMetrics) {
        List<Collector.MetricFamilySamples.Sample> samples = new ArrayList<>();

        MBeanInfo mBeanInfo = jmxClient.getMBeanInfo(objectInstance.getObjectName());
        for (MBeanAttributeInfo info : mBeanInfo.getAttributes()) {

            Collector.MetricFamilySamples.Sample userUpdatesMetric = null;
            String generateName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, info.getName());
            String type = info.getType();

            if ("long".equals(type)) {
                long value = (long) jmxClient.getAttribute(objectInstance.getObjectName(), info.getName());
                userUpdatesMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "generic_" + generateName, emptyList(), emptyList(), value);
            } else if ("int".equals(type)) {
                int value = (int) jmxClient.getAttribute(objectInstance.getObjectName(), info.getName());
                userUpdatesMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "generic_" + generateName, emptyList(), emptyList(), value);
            } else if ("double".equals(type)) {
                double value = (double) jmxClient.getAttribute(objectInstance.getObjectName(), info.getName());
                userUpdatesMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "generic_" + generateName, emptyList(), emptyList(), value);
            } else {
                List<Collector.MetricFamilySamples.Sample> tempSamples = new ArrayList<>();
                parse(APPLICATION_PREFFIX + "generic_" + generateName, objectInstance, info, tempSamples);
                samples.addAll(tempSamples);
            }

            if (userUpdatesMetric != null) {
                samples.add(userUpdatesMetric);
            }
        }
        collectedMetrics.add(new Collector.MetricFamilySamples("generic", Collector.Type.SUMMARY, "GenericParser", samples));
    }

    public List<Collector.MetricFamilySamples.Sample> parse(String currentName, ObjectInstance objectInstance, MBeanAttributeInfo info, List<Collector.MetricFamilySamples.Sample> tempSamples) {
        Object data = jmxClient.getAttribute(objectInstance.getObjectName(), info.getName());
        if (data instanceof CompositeDataSupport) {
            CompositeDataSupport compositeDataSupport = (CompositeDataSupport) data;
            final Set<String> keys = compositeDataSupport.getCompositeType().keySet();
            final Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                final String key = iterator.next();
                final Object value = compositeDataSupport.get(key);
                final OpenType<?> type = compositeDataSupport.getCompositeType().getType(key);
                //seems that if you are instance of CompositeDataSupport - you will have that property.
                final String objCanonicalName = objectInstance.getObjectName().getKeyPropertyList().get("name").replaceAll("\\s+", "");
                parseFurther(currentName + objCanonicalName, key, value, type, tempSamples);
            }
        }
        return tempSamples;
    }

    private void parseFurther(String currentName, String key, Object value, OpenType<?> type, List<Collector.MetricFamilySamples.Sample> tempSamples) {
        if (type.getTypeName().equals("java.lang.Integer")) {
            String generateName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, currentName + key);
            final Collector.MetricFamilySamples.Sample sample = new Collector.MetricFamilySamples.Sample(generateName, emptyList(), emptyList(), (int) value);
            tempSamples.add(sample);
        } else if (type.getTypeName().equals("java.lang.Long")) {
            String generateName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, currentName + key);
            final Collector.MetricFamilySamples.Sample sample = new Collector.MetricFamilySamples.Sample(generateName, emptyList(), emptyList(), (long) value);
            tempSamples.add(sample);
        } else if (type.getTypeName().equals("java.lang.Double")) {
            String generateName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, currentName + key);
            final Collector.MetricFamilySamples.Sample sample = new Collector.MetricFamilySamples.Sample(generateName, emptyList(), emptyList(), (double) value);
            tempSamples.add(sample);
        }
    }
}
