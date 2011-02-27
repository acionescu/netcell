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

import java.util.LinkedHashMap;
import java.util.Map;

import ro.zg.cfgengine.core.configuration.ConfigurationManager;
import ro.zg.cfgengine.core.exceptions.ConfigurationException;
import ro.zg.cfgengine.util.PackageCfgLoader;
import ro.zg.scriptdao.core.CommandManager;
import ro.zg.scriptdao.core.CommandTemplate;
import ro.zg.scriptdao.core.DefaultCommandManager;

public class DatasourcesCommandsManager {
    private String configFile;
    private String handlersFile;
    private ClassLoader resourcesLoader;
    
    public DatasourcesCommandsManager(String configFile, String handlersFile){
	this.configFile = configFile;
	this.handlersFile = handlersFile;
    }
    
    public DatasourcesCommandsManager(){
	
    }
    
    public Map<String,Map<String, CommandTemplate>> load() throws ConfigurationException{
	ConfigurationManager cfgManager = PackageCfgLoader.getInstance().load(handlersFile,configFile,resourcesLoader);
	Map<String,Object> entities = cfgManager.getObjectsByTagName("entity");
	Map<String,Map<String, CommandTemplate>> templates = new LinkedHashMap<String, Map<String, CommandTemplate>>();
	for(Object obj : entities.values()){
	    if(obj instanceof CommandManager){
		DefaultCommandManager cm = (DefaultCommandManager)obj;
		cm.setResourcesLoader(resourcesLoader);
		templates.put(cm.getName(), cm.getCommandTemplates());
	    }
	}
	return templates;
    }

    /**
     * @return the resourcesLoader
     */
    public ClassLoader getResourcesLoader() {
        return resourcesLoader;
    }

    /**
     * @param resourcesLoader the resourcesLoader to set
     */
    public void setResourcesLoader(ClassLoader resourcesLoader) {
        this.resourcesLoader = resourcesLoader;
    }
    
    
}
