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

import java.sql.Connection;

import net.segoia.scriptdao.constants.DataSourceConfigParameters;
import net.segoia.util.data.ConfigurationData;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

public class DatabaseConnectionManager extends BaseConnectionManager<Connection> {
    private Logger logger = Logger.getLogger(DatabaseConnectionManager.class.getName());
    
    private BasicDataSource dataSource;

    public void init() {
	buildDataSource();
    }

    private void buildDataSource() {
	ConfigurationData configData = dataSourceDefinition.getConfigData();
	dataSource = new BasicDataSource();
//	dataSource.setDriverClassName((String) configData
//		.getParameterValue(DataSourceConfigParameters.DRIVER_CLASS));
	String dbUrl = composeUrl(configData);
	dataSource.setUrl(dbUrl);
	
	logger.info("Connecting to database at url "+dbUrl);
	dataSource.setUsername((String) configData.getParameterValue(DataSourceConfigParameters.USERNAME));
	dataSource.setPassword((String) configData.getParameterValue(DataSourceConfigParameters.PASSWORD));
    }

    private String composeUrl(ConfigurationData configData) {
	String separator = (String) configData.getParameterValue(DataSourceConfigParameters.URL_SEPARATOR);
	String url = "jdbc:";
	url += configData.getParameterValue(DataSourceConfigParameters.DATABASE_URL_NAME);
	url += configData.getParameterValue(DataSourceConfigParameters.HOST);
	url += ":";
	url += configData.getParameterValue(DataSourceConfigParameters.PORT);
	url += separator;
	url += configData.getParameterValue(DataSourceConfigParameters.SCHEMA_NAME);
	
	return url;
    }

    public Connection getConnection() throws Exception {
	return dataSource.getConnection();
    }

}
