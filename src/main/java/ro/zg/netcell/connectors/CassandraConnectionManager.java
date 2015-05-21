package ro.zg.netcell.connectors;

import ro.zg.scriptdao.constants.DataSourceConfigParameters;
import ro.zg.util.data.ConfigurationData;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;

public class CassandraConnectionManager extends BaseConnectionManager<Session> {
    private static Logger logger = MasterLogManager.getLogger(CassandraConnectionManager.class.getName());
    // CASSANDRA

    public static final String HOSTS = "HOSTS";
    public static final String KEYSPACE = "KEYSPACE";

    private String keyspace;
    private Cluster cluster;
    private Session session;

    public void init() {
	buildCluster();
    }

    private void buildCluster() {
	ConfigurationData configData = dataSourceDefinition.getConfigData();

	String hostsStr = (String) configData.getParameterValue(HOSTS);
	String[] hosts = hostsStr.split(",");

	Integer port = (Integer) configData.getParameterValue(DataSourceConfigParameters.PORT);

	Builder builder = Cluster.builder();

	for (String host : hosts) {
	    builder.addContactPoint(host.trim());
	}

	if (port != null) {
	    builder.withPort(port);
	}

	cluster = builder.build();

	keyspace = (String) configData.getParameterValue(KEYSPACE);
    }

    private void ensureCluster() {
	if (cluster == null || cluster.isClosed()) {
	    buildCluster();
	}
    }

    @Override
    public synchronized Session getConnection() throws Exception {
	if (session == null || session.isClosed()) {
	    ensureCluster();

	    if (keyspace != null) {
		try {
		    session = cluster.connect(keyspace);
		} catch (Exception e) {
		    logger.warn("Failed to connect to cassandra cluster with config data "
			    + dataSourceDefinition.getConfigData());
		}
	    }

	    if (session == null) {
		session = cluster.connect();
	    }

	}
	return session;
    }

}
