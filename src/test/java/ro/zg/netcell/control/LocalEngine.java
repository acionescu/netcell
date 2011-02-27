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

import ro.zg.netcell.receivers.control.ReceiversController;

public class LocalEngine {
    
    public static void main(String args[]) throws Exception{
//	System.setProperty("jgroups.bind_addr", "localhost");
	NetCell nc = new NetCellLoader().load("root");
	ReceiversController rc = new ReceiversController();
	rc.start("xmls", nc);
	
	
//	Command c = new Command();
//	c.setName("execute");
//	c.put("fid","test.http.save-inner-urls");
//	c.put("url", "http://www.voidspace.org.uk/psychology/jung_index.shtml");
//	
//	CommandResponse cr = nc.execute(c);
//	System.out.println(cr);
    }

}
