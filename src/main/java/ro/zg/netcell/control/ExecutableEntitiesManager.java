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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ro.zg.cfgengine.core.configuration.ConfigurationManager;
import ro.zg.cfgengine.core.exceptions.ConfigurationException;
import ro.zg.cfgengine.util.PackageCfgLoader;
import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.netcell.constants.ResourcesTypes;
import ro.zg.netcell.vo.definitions.ConfigurableComponentDefinition;
import ro.zg.netcell.vo.definitions.DataAccessComponentDefinition;
import ro.zg.netcell.vo.definitions.EntityType;
import ro.zg.netcell.vo.definitions.ExecutableEntityDefinition;
import ro.zg.netcell.vo.definitions.WorkFlowDefinition;
import ro.zg.scriptdao.core.CommandTemplate;
import ro.zg.util.data.GenericNameValue;
import ro.zg.util.data.ListMap;
import ro.zg.util.data.ObjectsUtil;
import ro.zg.util.data.UserInputParameter;

public class ExecutableEntitiesManager extends BaseEntityManager<ExecutableEntityDefinition> {
    private String handlersFile;
    private String configFile;

    // private DatasourcesCommandsManager datasourceCommandsController;

    public ExecutableEntitiesManager(String handlers, String config, DatasourcesCommandsManager dcc) {
	this.handlersFile = handlers;
	this.configFile = config;
	// this.datasourceCommandsController = dcc;
    }

    public ExecutableEntitiesManager() {
    }

    public void init() throws ContextAwareException {
	// String dataSourcesCommandsHandler = controller
	// .getResourceFullPath(ResourcesTypes.DATASOURCES_COMMANDS_HANDLER_FILE);
	// String dataSourcesCommandsConfig = controller
	// .getResourceFullPath(ResourcesTypes.DATASOURCES_COMMANDS_CONFIG_FILE);
	// datasourceCommandsController = new DatasourcesCommandsManager(dataSourcesCommandsConfig,
	// dataSourcesCommandsHandler);

	handlersFile = resourcesManager.getResourceFullPath(ResourcesTypes.DEFINITIONS_HANDLER_FILE);
	configFile = resourcesManager.getResourceFullPath(ResourcesTypes.DEFINITIONS_CONFIG_FILE);

    }

    private Map<String, ExecutableEntityDefinition> load() throws ContextAwareException {
	ConfigurationManager cfgManager;
	try {
	    cfgManager = PackageCfgLoader.getInstance().load(handlersFile, configFile, getResourcesLoader());
	    Map<String, ExecutableEntityDefinition> entities = (Map) cfgManager.getAllObjects();

	    // Map<String, Map<String, CommandTemplate>> commands;
	    // try {
	    // commands = datasourceCommandsController.load();
	    // } catch (ConfigurationException e) {
	    // throw new ContextAwareException("ERROR_LOADING_DATASOURCES_COMMANDS", e);
	    // }

	    // for (Object obj : entities.values()) {
	    // if (obj instanceof EntityDefinition) {
	    // EntityDefinition def = (EntityDefinition) obj;
	    // if (def instanceof DataAccessComponentDefinition) {
	    // populateDataAccessComponents((DataAccessComponentDefinition) def, commands);
	    // }
	    // }
	    // }
	    return entities;

	} catch (ConfigurationException e) {
	    throw new ContextAwareException("ERROR_LOADING_EXECUTABLE_ENTITIES", e);
	}
    }
    
    private ListMap<String, ExecutableEntityDefinition> clasifyDefinitions(
	    Map<String, ExecutableEntityDefinition> definitions) throws ContextAwareException {
	ListMap<String, ExecutableEntityDefinition> definitionsByTypes = new ListMap<String, ExecutableEntityDefinition>();
	// Map<String, Map<String, CommandTemplate>> commands;
	// try {
	// commands = datasourceCommandsController.load();
	// } catch (ConfigurationException e) {
	// throw new ContextAwareException("ERROR_LOADING_DATASOURCES_COMMANDS", e);
	// }

	for (Object o : definitions.values()) {
	    if (o instanceof ExecutableEntityDefinition) {
		ExecutableEntityDefinition def = (ExecutableEntityDefinition) o;
		definitionsByTypes.add(def.getType(), def);
		// if (def instanceof DataAccessComponentDefinition) {
		// populateDataAccessComponents((DataAccessComponentDefinition) def, commands);
		// }
	    }
	}
	return definitionsByTypes;
    }
    
    private Map<String, ExecutableEntityDefinition> getExecutableEntitiesAsMap() throws ContextAwareException {
	Map<String, ExecutableEntityDefinition> map = new HashMap<String, ExecutableEntityDefinition>();
	for (Object o : load().values()) {
	    if (o instanceof ExecutableEntityDefinition) {
		ExecutableEntityDefinition eed = (ExecutableEntityDefinition) o;
		map.put(eed.getId(), eed);
	    }
	}
	return map;
    }

   
    private void populateDataAccessComponents(DataAccessComponentDefinition dacd,
	    Map<String, Map<String, CommandTemplate>> commands) {
	Map<String, CommandTemplate> commandsForDatasource = commands.get(dacd.getDataSourceName());
	CommandTemplate ct = commandsForDatasource.get(dacd.getCommandName());
	UserInputParameter uip = new UserInputParameter();
	uip.setName(DataAccessComponentDefinition.COMMAND_CONTENT);
	uip.setValue(ct.getScript());
	uip.setInputType(UserInputParameter.UNKNOWN_LARGE_VALUE);
	dacd.getConfigData().addUserIntpuParam(uip);
    }

    public Map<String, List<ExecutableEntityDefinition>> getDefinitions() throws ContextAwareException {
	return clasifyDefinitions(load()).getAll();
    }

    public List<String> getDefinitionTypes() throws ContextAwareException {
	return new ArrayList<String>(clasifyDefinitions(load()).keySet());
    }

    public List<ExecutableEntityDefinition> getDefinitionsByType(String type) throws ContextAwareException {
	return clasifyDefinitions(load()).get(type);
    }

    public boolean containsId(String id) throws ContextAwareException {
	return load().containsKey(id);
    }

    public Map<String, ExecutableEntityDefinition> getEntitiesAsMap() throws ContextAwareException {
	return load();
    }

    public ExecutableEntityDefinition saveEntity(ExecutableEntityDefinition entity) throws ContextAwareException {
	String templatesPath = resourcesManager.getResourceFullPath(ResourcesTypes.DEFINITIONS_TEMPLATES_DIR);
	String templateName = getDefinitionsTemplates().get(entity.getType());
	if (templateName == null) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("defType", entity.getType()));
	    throw new ContextAwareException("UNKNOWN_DEFINITION", ec);
	}
	String templatePath = templatesPath + File.separator + templateName;
	String entitiesDir = resourcesManager.getResourceFullPath(ResourcesTypes.EXECUTABLE_ENTITIES_DIR);
//	String entityFilePath = resourcesManager.getFullSystemPath(ResourcesTypes.EXECUTABLE_ENTITIES_DIR, entity
//		.getId());
	URL entityUrl = resourcesManager.getUrl(entitiesDir, getEntityFileForId(entity.getId()), true);
	// try {
	// /* create file if not existent */
	// File ff = new File(entityFilePath);
	// if (!ff.exists()) {
	// ff.createNewFile();
	// }
	// FileWriter fw = new FileWriter(entityFilePath);
	// getDefinitionsGenerator().generateXmlDefinition(entity, fw, templatePath);
	// fw.close();
	// } catch (IOException e) {
	// ExceptionContext ec = new ExceptionContext();
	// ec.put(new GenericNameValue("file", entityFilePath));
	// throw new ContextAwareException("ERROR_CREATING_DEFINITION_FILE", e, ec);
	// }
	generateDefinition(entityUrl, entity, templatePath, "entityDefinition");
	return entity;
    }

    public Map<String, List<ExecutableEntityDefinition>> getEntities() throws ContextAwareException {
	throw new UnsupportedOperationException();
    }

    public List<String> getDefinitionTypes(String parentType) throws ContextAwareException {
	throw new UnsupportedOperationException();
    }

    public ExecutableEntityDefinition createEntityDirectoryStructure(String name, EntityType type) throws ContextAwareException {
	throw new UnsupportedOperationException();
    }

    public boolean containsEntityWithId(String entityId) throws ContextAwareException {
	return load().containsKey(entityId);
    }

    public Map<String, List<String>> getDependencies() throws ContextAwareException {
	Map<String, ExecutableEntityDefinition> definitions = getExecutableEntitiesAsMap();
	ListMap<String, String> dependencies = new ListMap<String, String>();
	for (ExecutableEntityDefinition e : definitions.values()) {
	    if (e instanceof WorkFlowDefinition) {
		WorkFlowDefinition wf = (WorkFlowDefinition) e;
		for (String depId : wf.getDependencies()) {
		    dependencies.add(depId, wf.getId());
		}
	    }
	}
	return dependencies.getAll();
    }

    public boolean removeEntity(ExecutableEntityDefinition entity) throws ContextAwareException {
//	String entityFilePath = resourcesManager.getFullSystemPath(ResourcesTypes.EXECUTABLE_ENTITIES_DIR, entity
//		.getId()
//		+ ".xml");
	String entitiesDir = resourcesManager.getResourceFullPath(ResourcesTypes.EXECUTABLE_ENTITIES_DIR);
	URL entityUrl = resourcesManager.getUrl(entitiesDir, getEntityFileForId(entity.getId()), false);
	return removeResource(entityUrl);
    }

    public List<String> getTemplatesIds() throws ContextAwareException {
	return new ArrayList<String>();
    }

}
