/**
 * netcell - Java ESB with an embedded business process modeling engine
 * Copyright (C) 2009  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.netcell.connectors;

import net.segoia.scriptdao.constants.DataSourceConfigParameters;
import net.segoia.util.data.ConfigurationData;
import net.segoia.util.logging.Logger;
import net.segoia.util.logging.MasterLogManager;

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
