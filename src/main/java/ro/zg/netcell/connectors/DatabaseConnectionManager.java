/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.netcell.connectors;

import java.sql.Connection;

import org.apache.commons.dbcp.BasicDataSource;

import ro.zg.scriptdao.constants.DataSourceConfigParameters;
import ro.zg.util.data.ConfigurationData;

public class DatabaseConnectionManager extends BaseConnectionManager<Connection> {
    private BasicDataSource dataSource;

    public void init() {
	buildDataSource();
    }

    private void buildDataSource() {
	ConfigurationData configData = dataSourceDefinition.getConfigData();
	dataSource = new BasicDataSource();
	dataSource.setDriverClassName((String) configData
		.getParameterValue(DataSourceConfigParameters.DRIVER_CLASS));
	dataSource.setUrl(composeUrl(configData));
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
