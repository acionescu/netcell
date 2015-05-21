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

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.netcell.control.exceptions.InitializationException;
import ro.zg.netcell.receivers.control.ReceiversController;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

public class NodeStarter {
    private static Logger logger = MasterLogManager.getLogger(NodeStarter.class.getName());
    private static NetcellNode node;

    public static void main(String args[]) {
	String host = "localhost";
	if(args != null && args.length > 0) {
	    host = args[0];
	}
//	System.out.println("starting... for ip "+host);
//	System.setProperty("jgroups.bind_addr", host);
//	NetCell nc = new NetCellLoader().load("root");
	
	NetCell nc;
	try {
	    nc = NodeLoader.load("root");
	} catch (ContextAwareException e) {
	    logger.fatal("Failed to start netcell component", e);
	    return;
	}
	ReceiversController rc = new ReceiversController();
	try {
	    rc.start("xmls", nc);
	} catch (InitializationException e) {
	    logger.fatal("Failed to start receivers controller", e);
	    return;
	}
	/* use the receivers controller as a way to interact with the node from within a component */
	node = rc;
    }
    
    public static NetcellNode getNode() {
	return node;
    }
    
}
