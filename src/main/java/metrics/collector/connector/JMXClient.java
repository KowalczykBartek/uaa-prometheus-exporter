package metrics.collector.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.HashSet;
import java.util.Set;

public class JMXClient {
    private static final Logger LOG = LoggerFactory.getLogger(JMXClient.class);
    private static final Set<ObjectInstance> EMPTY_SET = new HashSet<>();

    private volatile JMXConnector jmxConnector;
    private volatile MBeanServerConnection mbeanConn;

    private final String host;
    private final int port;

    public JMXClient(String host, Integer port) {
        this.host = host;
        this.port = port;
        connect();
    }

    public void connect() {
        try {
            String url = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi";
            JMXServiceURL serviceUrl = new JMXServiceURL(url);
            this.jmxConnector = JMXConnectorFactory.connect(serviceUrl, null);
            this.mbeanConn = jmxConnector.getMBeanServerConnection();
        } catch (Exception ex) {
            LOG.error("Not able to connect", ex);
        }
    }

    public Object getAttribute(ObjectName objectName, String globals) {
        try {
            return mbeanConn.getAttribute(objectName, globals);
        } catch (Exception ex) {
            LOG.error("Exception during getAttribute", ex);
            throw new RuntimeException(ex);
        }
    }

    public Set<ObjectInstance> queryMBeans(ObjectName o, ObjectName o1) {
        try {
            return mbeanConn.queryMBeans(o, o1);
        } catch (Exception ex) {
            LOG.error("Exception during queryMBeans", ex);
            return EMPTY_SET;
        }
    }

    public MBeanInfo getMBeanInfo(ObjectName objectName) {
        try {
            return mbeanConn.getMBeanInfo(objectName);
        } catch (Exception ex) {
            LOG.error("Exception during getMBeanInfo", ex);
            return null;
        }
    }
}
