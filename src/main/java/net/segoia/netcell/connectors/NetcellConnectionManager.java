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

import net.segoia.netcell.control.NetCell;
import net.segoia.netcell.control.NetcellNode;
import net.segoia.netcell.control.NodeLoader;
import net.segoia.netcell.control.NodeStarter;
import net.segoia.util.data.ConfigurationData;

import org.apache.commons.lang.NotImplementedException;

public class NetcellConnectionManager extends BaseConnectionManager<NetcellNode>{
    public static final String IS_LOCAL="IS_LOCAL";
    
    private boolean isLocal;
    
    public void init() {
	ConfigurationData cfgData = dataSourceDefinition.getConfigData();
	isLocal = (Boolean)cfgData.getParameterValue(IS_LOCAL);
    }
    

    @Override
    public NetcellNode getConnection() throws Exception {
	if(isLocal) {
	    return NodeStarter.getNode();
	}
	throw new NotImplementedException("The NetCell instance is not local and we don't know how to use a remote one.");
    }

}
