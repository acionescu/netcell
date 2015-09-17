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
package net.segoia.netcell.config;

import net.segoia.netcell.entities.GenericEntity;
import net.segoia.util.data.GenericNameValueContext;

public class EntitiesTestCase extends BaseEngineConfigTestCase{

   
//    public void testPublicEntity() throws Exception{
//	GenericEntity<Object> entity = (GenericEntity)cfgManager.getObjectById("comparator");
//	assertNotNull(entity);
//	GenericNameValueContext c = new GenericNameValueContext();
//	c.put("a","5");
//	c.put("b","2");
//	Object res = entity.execute(c);
//	System.out.println(res);
//    }
//    
//    public void testRawFlow() throws Exception{
//	GenericEntity<GenericNameValueContext> entity = (GenericEntity)cfgManager.getObjectById("firstFlow");
//	assertNotNull(entity);
//	GenericNameValueContext c = new GenericNameValueContext();
//	c.put("enteredValue","54");
//	GenericNameValueContext res = entity.execute(c);
//	System.out.println(res.getParameters());
//    }
//    
//    public void testWrappedFlow() throws Exception{
//	GenericEntity<GenericNameValueContext> entity = (GenericEntity)cfgManager.getObjectById("wrappedFirstFlow");
//	assertNotNull(entity);
//	GenericNameValueContext c = new GenericNameValueContext();
//	c.put("enteredValue","10");
//	GenericNameValueContext res = entity.execute(c);
//	System.out.println(res.getParameters());
//    }
//    
//    public void testGeneratedFlow1() throws Exception{
//	GenericEntity<GenericNameValueContext> entity = (GenericEntity)cfgManager.getObjectById("generatedFlow1");
//	assertNotNull(entity);
//	GenericNameValueContext c = new GenericNameValueContext();
//	c.put("enteredValue","54");
//	GenericNameValueContext res = entity.execute(c);
//	System.out.println(res.getParameters());
//    }
//    
//    public void testGeneratedFlow2() throws Exception{
//	GenericEntity<GenericNameValueContext> entity = (GenericEntity)cfgManager.getObjectById("mortiimati");
//	assertNotNull(entity);
//	GenericNameValueContext c = new GenericNameValueContext();
//	c.put("list","[4,5,2]");
//	c.put("index","1");
//	GenericNameValueContext res = entity.execute(c);
//	System.out.println(res.getParameters());
//    }
    
//    public void testWrapperFlow() throws Exception{
////	GenericEntity<GenericNameValueContext> entity = (GenericEntity)cfgManager.getObjectById("testEmbededFlow");
////	assertNotNull(entity);
//	GenericNameValueContext c = new GenericNameValueContext();
//	c.put("fid","testEmbededFlow");
//	c.put("enteredValue","10");
//	GenericNameValueContext res = engineController.execute(c);
//	System.out.println("response= "+res.getParameters());
//    }
    
    public void testDatabaseCall() throws Exception{
	GenericEntity<GenericNameValueContext> entity = (GenericEntity)cfgManager.getObjectById("get-people");
	assertNotNull(entity);
	GenericNameValueContext c = new GenericNameValueContext();
//	c.put("list","[4,5,2]");
//	c.put("index","1");
	GenericNameValueContext res = entity.execute(c);
	System.out.println(res.getParameters());
	
	 res = entity.execute(c);
	System.out.println(res.getParameters());
	
	 res = entity.execute(c);
	System.out.println(res.getParameters());
    }
}
