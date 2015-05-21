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

import java.io.File;

import ro.zg.cfgengine.core.configuration.ConfigurationManager;
import ro.zg.cfgengine.core.exceptions.ConfigurationException;
import ro.zg.cfgengine.util.PackageCfgLoader;
import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.distributed.framework.ReflectionBasedDistributedService;
import ro.zg.netcell.constants.DistributedServicesTypes;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

public class NodeLoader {
    private static String defaulCfgEntryPoint = "config" + File.separator
	    + "config.xml";
    private static Logger logger = MasterLogManager.getLogger(NodeLoader.class.getName());

    private static NetCell netcellInstance;
    private static ExecutionEngineContract executionEngineInstance;

    public static NetCell getNetCellInstance() {
	return netcellInstance;
    }

    public static ExecutionEngineContract getExecutionEngineInstance() {
	return executionEngineInstance;
    }

    public static NetCell load(String rootDir) throws ContextAwareException {
	return load(rootDir, defaulCfgEntryPoint);
    }

    public static NetCell load(String rootDir, String entryPoint)
	    throws ContextAwareException {
	ConfigurationManager cfgManager = null;
	try {
	    String configFileName = getFullEntryPointPath(rootDir, entryPoint);
	    // ClassLoader classLoader = this.getClass().getClassLoader();
	    ClassLoader classLoader = NodeLoader.class.getClassLoader();
	    logger.info("Loading node from "
		    + classLoader.getResource(configFileName));
	    cfgManager = PackageCfgLoader.getInstance().load(configFileName,
		    classLoader);
	} catch (ConfigurationException e) {
	    throw new ContextAwareException("INITIALIZATION_EXCEPTION", e);
	}
	NetCell commandController = (NetCell) cfgManager
		.getObjectById("CommandControllerWrapper");
	netcellInstance = commandController;
	
	/* get local instance of the execution engine */
	DistributedServicesManager distributedServicesManager =(DistributedServicesManager) cfgManager
		.getObjectById("distributedServicesManager");
	ReflectionBasedDistributedService rfds = (ReflectionBasedDistributedService)distributedServicesManager.getDistributedService(DistributedServicesTypes.EXECUTION_ENGINE_DESC);
	executionEngineInstance = (ExecutionEngineContract)rfds.getTargetObject();
	return commandController;
    }

    private static String getFullEntryPointPath(String rootDir,
	    String entryPoint) {
	return rootDir + File.separator + entryPoint;
    }
}
