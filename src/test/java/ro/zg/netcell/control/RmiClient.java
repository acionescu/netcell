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
package ro.zg.netcell.control;

import ro.zg.netcell.client.NetcellRmiClient;

public class RmiClient {
    
    
    public static void main(String[] args) throws Exception{
	NetcellRmiClient client = new NetcellRmiClient("localhost",2000,"NetcellRmiReceiver");
	Command c = new Command();
	c.setName("get_definitions");
	
	CommandResponse cr = client.execute(c);
	
	System.out.println(cr);
    }
}
