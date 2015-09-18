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

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

import org.junit.Ignore;

import junit.framework.TestCase;
import net.segoia.cfgengine.core.configuration.ConfigurationManager;
import net.segoia.cfgengine.util.PackageCfgLoader;
import net.segoia.netcell.control.ExecutionEngineController;
import net.segoia.netcell.control.ExecutionEngineLoader;
import net.segoia.netcell.control.ResourcesManager;

@Ignore
public class BaseEngineConfigTestCase extends TestCase{
    protected ConfigurationManager cfgManager;
    protected ExecutionEngineController engineController;
    
    public void setUp() throws Exception{
//	String dir = "root/config/engine/";
	String rootDir = "/media/netcell-node/repository";
	String dir = "/media/netcell-node/repository/root/config/engine/";
	String handlers = dir+"engine-handlers.xml";
	String config = dir+"engine-config.xml";
//	cfgManager = PackageCfgLoader.getInstance().load(handlers, config);
	engineController = new ExecutionEngineLoader().load(handlers, config);
	
	
	ConfigurationManager resCfg = PackageCfgLoader.getInstance().load("/media/netcell-node/repository/root/config/controller/resources-manager.xml");
	
	ResourcesManager resourcesManager = new ResourcesManager();//(ResourcesManager)resCfg.getObjectById("resourcesManager");
	resourcesManager.setRootDir(rootDir);
	
	HashMap<String, String> resRelativePaths = new HashMap<>();
	resRelativePaths.put("ENGINE_HANDLER_FILE", "root/config/engine/engine-handlers.xml");
	resRelativePaths.put("ENGINE_CONFIG_FILE", "root/config/engine/engine-config.xml");
	resourcesManager.setResourcesRelativePaths(resRelativePaths);
	
	resourcesManager.init();
	engineController.setResourcesManager(resourcesManager);
	engineController.setResourcesLoader(new URLClassLoader(new URL[] {new URL("file://"+rootDir+"/")},Thread.currentThread().getContextClassLoader()));
	engineController.init();
    }
}
