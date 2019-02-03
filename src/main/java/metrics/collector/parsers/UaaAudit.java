package metrics.collector.parsers;

import io.prometheus.client.Collector;
import metrics.collector.connector.JMXClient;

import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class UaaAudit extends Parser {

    public UaaAudit(JMXClient jmxClient) {
        super(jmxClient);
    }

    @Override
    public boolean canHandle(String name) {
        return "cloudfoundry.identity:name=UaaAudit".equals(name);
    }

    @Override
    public void parse(ObjectInstance objectInstance, List<Collector.MetricFamilySamples> collectedMetrics) {
        final ObjectName objectName = objectInstance.getObjectName();
        long clientAuthenticationCount = toLong(jmxClient.getAttribute(objectName, "ClientAuthenticationCount"));
        Collector.MetricFamilySamples.Sample clientAuthenticationCountMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "uaa_audit_client_authentication_count", emptyList(), emptyList(), clientAuthenticationCount);

        long clientAuthenticationFailureCount = toLong(jmxClient.getAttribute(objectName, "ClientAuthenticationFailureCount"));
        Collector.MetricFamilySamples.Sample clientAuthenticationFailureCountMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "uaa_audit_client_authentication_failure_count", emptyList(), emptyList(), clientAuthenticationFailureCount);

        long principalAuthenticationFailureCount = toLong(jmxClient.getAttribute(objectName, "PrincipalAuthenticationFailureCount"));
        Collector.MetricFamilySamples.Sample principalAuthenticationFailureCountMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "uaa_audit_principal_authentication_failure_count", emptyList(), emptyList(), principalAuthenticationFailureCount);

        long principalNotFoundCount = toLong(jmxClient.getAttribute(objectName, "PrincipalNotFoundCount"));
        Collector.MetricFamilySamples.Sample principalNotFoundCountMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "uaa_audit_principal_not_found_count", emptyList(), emptyList(), principalNotFoundCount);

        long userAuthenticationCount = toLong(jmxClient.getAttribute(objectName, "UserAuthenticationCount"));
        Collector.MetricFamilySamples.Sample userAuthenticationCountMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "uaa_audit_user_authentication_count", emptyList(), emptyList(), userAuthenticationCount);

        long userAuthenticationFailureCount = toLong(jmxClient.getAttribute(objectName, "UserAuthenticationFailureCount"));
        Collector.MetricFamilySamples.Sample userAuthenticationFailureCountMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "uaa_audit_user_authentication_failure_count", emptyList(), emptyList(), userAuthenticationFailureCount);

        long userNotFoundCount = toLong(jmxClient.getAttribute(objectName, "UserNotFoundCount"));
        Collector.MetricFamilySamples.Sample userNotFoundCountMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "uaa_audit_user_not_found_count", emptyList(), emptyList(), userNotFoundCount);

        long userPasswordChanges = toLong(jmxClient.getAttribute(objectName, "UserPasswordChanges"));
        Collector.MetricFamilySamples.Sample userPasswordChangesMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "uaa_audit_user_password_changes", emptyList(), emptyList(), userPasswordChanges);

        long userPasswordFailures = toLong(jmxClient.getAttribute(objectName, "UserPasswordFailures"));
        Collector.MetricFamilySamples.Sample userPasswordFailuresMetric = new Collector.MetricFamilySamples.Sample(APPLICATION_PREFFIX + "uaa_audit_user_password_failures", emptyList(), emptyList(), userPasswordFailures);

        List<Collector.MetricFamilySamples.Sample> samples = new ArrayList<>();
        samples.add(clientAuthenticationCountMetric);
        samples.add(clientAuthenticationFailureCountMetric);
        samples.add(principalAuthenticationFailureCountMetric);
        samples.add(principalNotFoundCountMetric);
        samples.add(userAuthenticationFailureCountMetric);
        samples.add(userAuthenticationCountMetric);
        samples.add(userNotFoundCountMetric);
        samples.add(userPasswordChangesMetric);
        samples.add(userPasswordFailuresMetric);
        collectedMetrics.add(new Collector.MetricFamilySamples("cloudfoundry.identity", Collector.Type.SUMMARY, "UaaAudit", samples));
    }
}
