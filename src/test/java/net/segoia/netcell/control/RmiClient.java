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

import net.segoia.netcell.client.NetcellRmiClient;
import net.segoia.netcell.control.Command;
import net.segoia.netcell.control.CommandResponse;

public class RmiClient {
    
    
    public static void main(String[] args) throws Exception{
	NetcellRmiClient client = new NetcellRmiClient("localhost",2000,"NetcellRmiReceiver");
	Command c = new Command();
	c.setName("get_definitions");
	
	CommandResponse cr = client.execute(c);
	
	System.out.println(cr);
	
//	 Registry registry = LocateRegistry.getRegistry("localhost", 9001);
//         NetCell stub = (NetCell) registry.lookup("Hello");
//         System.out.println(stub.execute(c));
    }
}
