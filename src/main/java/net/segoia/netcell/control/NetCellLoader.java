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

import java.io.File;

import net.segoia.cfgengine.core.configuration.ConfigurationManager;
import net.segoia.cfgengine.core.exceptions.ConfigurationException;
import net.segoia.cfgengine.util.PackageCfgLoader;
import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;
import net.segoia.netcell.control.NetCell;
import net.segoia.util.logging.Logger;
import net.segoia.util.logging.MasterLogManager;

public class NetCellLoader {
    private String defaulCfgEntryPoint = "config"+File.separator+"config.xml";
    private static NetCellControllerContract netcellControllerInstance;
    private static Logger logger = MasterLogManager.getLogger(NetCellLoader.class.getName());
    
    public static NetCellControllerContract getNetcellControllerInstance() {
	return netcellControllerInstance;
    }
    
    public NetCell load(String rootDir) throws ContextAwareException{
	return load(rootDir,defaulCfgEntryPoint);
    }
    
    public NetCell load(String rootDir, String entryPoint) throws ContextAwareException{
	ConfigurationManager cfgManager = null;
	try {
	    String configFileName = getFullEntryPointPath(rootDir,entryPoint);
	    ClassLoader classLoader = this.getClass().getClassLoader();
	    logger.info("Loading node from "+classLoader.getResource(configFileName));
	    cfgManager = PackageCfgLoader.getInstance().load(configFileName,classLoader);
	} catch (ConfigurationException e) {
	   throw new ContextAwareException("INITIALIZATION_EXCEPTION",e);
	}
	NetCellController netCellController = (NetCellController)cfgManager.getObjectById("NetCellController");
	netcellControllerInstance = netCellController;
	CommandControllerWrapper commandController = (CommandControllerWrapper)cfgManager.getObjectById("CommandControllerWrapper");
	if(netCellController != null && commandController != null){
	    netCellController.start(rootDir);
//	    commandController.setMonitoringManager(netCellController.getMonitoringManager());
	    return new NetCellFacade(commandController);
	}
	ExceptionContext ec = new ExceptionContext();
	ec.put("rootDir",rootDir);
	ec.put("entryPoint",entryPoint);
	throw new ContextAwareException("INITIALIZATION_EXCEPTION",ec);
	
    }
    
    private String getFullEntryPointPath(String rootDir, String entryPoint){
	return rootDir+File.separator+entryPoint;
    }
    
}
