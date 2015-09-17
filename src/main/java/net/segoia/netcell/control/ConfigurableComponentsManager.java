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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.segoia.cfgengine.core.configuration.ConfigurationManager;
import net.segoia.cfgengine.core.exceptions.ConfigurationException;
import net.segoia.cfgengine.util.PackageCfgLoader;
import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.netcell.constants.ResourcesTypes;
import net.segoia.netcell.vo.definitions.ConfigurableComponentDefinition;
import net.segoia.netcell.vo.definitions.EntityType;
import net.segoia.util.data.ObjectsUtil;

public class ConfigurableComponentsManager extends BaseEntityManager<ConfigurableComponentDefinition>{

    private Map<String, ConfigurableComponentDefinition> getConfigurableComponents() throws ContextAwareException{
	String configurableComponentsHandlersFile = resourcesManager.getResourceFullPath(ResourcesTypes.DEFINITIONS_HANDLER_FILE);
	String configurableComponetsTemplatesFile = resourcesManager.getResourceFullPath(ResourcesTypes.CONFIGURABLE_COMPONENTS_CONFIG_FILE);
	ConfigurationManager cfgManager;
	try {
	    cfgManager = PackageCfgLoader.getInstance().load(configurableComponentsHandlersFile, configurableComponetsTemplatesFile, getResourcesLoader());
	} catch (ConfigurationException e) {
	    throw new ContextAwareException("ERROR_LOADING_CONFIGURABLE_TEMPLATES",e);
	}
	
	Map<String,ConfigurableComponentDefinition> configurableComponents = new LinkedHashMap<String, ConfigurableComponentDefinition>();
	for(Map.Entry<String, Object> entry :  cfgManager.getAllObjects().entrySet()) {
	    if(entry.getValue() instanceof ConfigurableComponentDefinition) {
		configurableComponents.put(entry.getKey(), (ConfigurableComponentDefinition)entry.getValue());
	    }
	}
	
	return configurableComponents;
    }

    
    public boolean containsEntityWithId(String entityId) throws ContextAwareException {
	return getConfigurableComponents().containsKey(entityId);
    }

    public ConfigurableComponentDefinition createEntityDirectoryStructure(String name, EntityType type) throws ContextAwareException {
	ConfigurableComponentDefinition ccd = getConfigurableComponents().get(type.getType());
	ConfigurableComponentDefinition newCcd = (ConfigurableComponentDefinition)ObjectsUtil.copy(ccd);
	newCcd.setId(name);
	newCcd.setTemplateId(type.getType());
	newCcd.getConfigData().setTemplateId(type.getType());
	return newCcd;
    }

    public List<String> getDefinitionTypes() throws ContextAwareException {
	// TODO Auto-generated method stub
	return null;
    }

    public List<String> getDefinitionTypes(String parentType) throws ContextAwareException {
	// TODO Auto-generated method stub
	return null;
    }

    public Map<String, List<String>> getDependencies() throws ContextAwareException {
	// TODO Auto-generated method stub
	return null;
    }

    public Map<String, List<ConfigurableComponentDefinition>> getEntities() throws ContextAwareException {
	// TODO Auto-generated method stub
	return null;
    }

    public Map<String, ConfigurableComponentDefinition> getEntitiesAsMap() throws ContextAwareException {
	return new HashMap<String, ConfigurableComponentDefinition>();
    }

    public List<String> getTemplatesIds() throws ContextAwareException {
	return new ArrayList<String>(getConfigurableComponents().keySet());
    }

    public void init() throws ContextAwareException {
	// TODO Auto-generated method stub
	
    }

    public boolean removeEntity(ConfigurableComponentDefinition entity) throws ContextAwareException {
	String entitiesDir = resourcesManager.getResourceFullPath(ResourcesTypes.CONFIGURABLE_COMPONENTS_DIR);
	URL entityUrl = resourcesManager.getUrl(entitiesDir, getEntityFileForId(entity.getId()), false);
	if (entityUrl != null) {
	    /* remove component definition */
	    boolean removed = removeResource(entityUrl);
	    return removed;
	}
	return false;
    }

    public ConfigurableComponentDefinition saveEntity(ConfigurableComponentDefinition entity)
	    throws ContextAwareException {
	String templateId = entity.getTemplateId();
	if(templateId == null) {
	    templateId = entity.getConfigData().getTemplateId();
	}
	String stubsDir = resourcesManager.getResourceFullPath(ResourcesTypes.CONFIGURABLE_COMPONENTS_STUBS_DIR);
	String stubFile = resourcesManager.getResourceRelativePath(stubsDir, templateId.toLowerCase()+".xml");
	String configurableComponentsDir = resourcesManager.getResourceFullPath(ResourcesTypes.CONFIGURABLE_COMPONENTS_DIR);
	URL entityUrl = resourcesManager.getUrl(configurableComponentsDir, getEntityFileForId(entity.getId()), true);
	generateDefinition(entityUrl, entity, stubFile, "entityDefinition");
	return entity;
    }

}
