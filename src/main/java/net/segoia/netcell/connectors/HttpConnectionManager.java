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

import java.util.Map;

import net.segoia.scriptdao.constants.DataSourceConfigParameters;
import net.segoia.util.data.ConfigurationData;
import net.segoia.util.data.UserInputParameter;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpConnectionManager extends BaseConnectionManager<HttpClient> {
    private HttpClient httpClient;

    public void init() {
	initHttpClient();
    }

    private void initHttpClient() {
	ConfigurationData cfgData = dataSourceDefinition.getConfigData();
	
	HttpParams params = new BasicHttpParams();
	ConnManagerParams.setMaxTotalConnections(params, Integer.parseInt(cfgData.getParameterValue(DataSourceConfigParameters.MAX_TOTAL_CONNECTIONS).toString()));
	ConnPerRouteBean connPerRoute = new ConnPerRouteBean(10);
	HttpHost localhost = new HttpHost("locahost", 80);
	connPerRoute.setMaxForRoute(new HttpRoute(localhost), 50);
	ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
	ConnManagerParams.setTimeout(params, Long.parseLong(cfgData.getParameterValue(DataSourceConfigParameters.CONNECTION_TIMEOUT).toString()));

	
	SchemeRegistry schemeRegistry = new SchemeRegistry();
	schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

	ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
	
	
	
	/* set config params */
	ConfigurationData configData = dataSourceDefinition.getConfigData();
	Map<String, UserInputParameter> userInputParams = configData.getUserInputParams();
	for (UserInputParameter uip : userInputParams.values()) {
	    params.setParameter(uip.getInnerName(), uip.getValue());
	}

	HttpConnectionParams.setSoTimeout(params, 25000);
	httpClient = new DefaultHttpClient(cm, params);

    }

    public HttpClient getConnection() throws Exception {
	return httpClient;
    }

}
