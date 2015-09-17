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
package net.segoia.netcell.control;

import java.util.List;
import java.util.Map;

import net.segoia.netcell.control.Command;
import net.segoia.netcell.control.NetCell;
import net.segoia.netcell.control.NetCellLoader;
import net.segoia.netcell.vo.definitions.DataAccessComponentDefinition;
import net.segoia.netcell.vo.definitions.ExecutableEntityDefinition;
import junit.framework.TestCase;

public class NetCellControlTestCase extends TestCase {
    private NetCell nc;
//    protected ConfigurationManager cfgManager;
//
    public void setUp() throws Exception {
	nc = new NetCellLoader().load("root");
    }
    
//    public void testLoadNetCell() throws Exception{
//	
//	Command c = new Command();
//	c.setName("get_definitions");
//	CommandResponse cr = nc.execute(c);
//	System.out.println(cr);
//	System.out.println(ReflectionUtility.objectToString( cr.getParameters()));
////	c.setName("execute");
////	c.put("fid", "generatedFlow2");
////	c.put("enteredValue","-40");
////	
////	cr = nc.execute(c);
////	System.out.println(cr);
//	
//    }
    
    public void testDataSources() throws Exception{
	Command c = new Command();
	c.setName("get_datasources_templates");
	System.out.println(nc.execute(c));
	c.setName("get_datasources_definitions");
	System.out.println(nc.execute(c));
	
	c.setName("get_definitions");
	Map<String,List<ExecutableEntityDefinition>> def = (Map)nc.execute(c).getValue("definitions");
	DataAccessComponentDefinition dac = (DataAccessComponentDefinition)def.get("data-access-component-definition").get(0);
	System.out.println(dac.getConfigData().getUserInputParams());
    }

}
