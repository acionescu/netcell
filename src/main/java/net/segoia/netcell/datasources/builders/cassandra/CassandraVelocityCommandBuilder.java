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
package net.segoia.netcell.datasources.builders.cassandra;

import java.util.HashMap;
import java.util.Map;

import net.segoia.scriptdao.core.VelocityCommandBuilder;

import com.datastax.driver.core.utils.UUIDs;

public class CassandraVelocityCommandBuilder extends VelocityCommandBuilder{

    /* (non-Javadoc)
     * @see net.segoia.scriptdao.core.VelocityCommandBuilder#buildCommand(java.lang.String, java.util.Map)
     */
    @Override
    public String buildCommand(String script, Map arguments) {
	if(arguments == null) {
	    arguments =new HashMap();
	}
	arguments.put("uuids", UUIDs.class);
	
	return super.buildCommand(script, arguments);
    }
    
    

}
