package ro.zg.netcell.connectors;

import java.util.List;

import ro.zg.scriptdao.constants.DataSourceConfigParameters;
import ro.zg.util.data.ConfigurationData;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;

public class CassandraConnectionManager extends BaseConnectionManager<Session> {
    // CASSANDRA

    public static final String HOSTS = "HOSTS";
    public static final String KEYSPACE = "KEYSPACE";

    private Cluster cluster;
    private String keyspace;

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
    public Session getConnection() throws Exception {
	ensureCluster();

	if (keyspace != null) {
	    return cluster.connect(keyspace);
	}

	return cluster.connect();
    }

}
