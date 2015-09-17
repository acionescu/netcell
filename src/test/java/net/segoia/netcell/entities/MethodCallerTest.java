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
package net.segoia.netcell.entities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.segoia.netcell.entities.MethodCaller;
import net.segoia.util.data.GenericNameValueContext;
import junit.framework.TestCase;

public class MethodCallerTest extends TestCase{
    
    public void test() throws Exception{
	GenericNameValueContext c = new GenericNameValueContext();
	c.put("resource",new Date());
	c.put("method","getTime");
	MethodCaller mc = new MethodCaller();
	Object res = mc.execute(c);
	assertNotNull(res);
	System.out.println(res);
    }
    
    public void testWitParams() throws Exception{
	GenericNameValueContext c = new GenericNameValueContext();
	Map map = new HashMap();
	map.put("key", "value");
	c.put("resource",map);
	c.put("method","get");
	GenericNameValueContext argsContext = new GenericNameValueContext();
	argsContext.put("param","key");
	List<String> params = new ArrayList<String>();
	params.add("param");
	c.put("argsContext",argsContext);
	c.put("argsOrder",params);
	List<String> types = new ArrayList<String>();
	types.add("java.lang.Object");
	c.put("argsTypes",types);
	MethodCaller mc = new MethodCaller();
	
	Object res = mc.execute(c);
	assertNotNull(res);
	assertEquals("value", "value");
	System.out.println(res);
    }
}
