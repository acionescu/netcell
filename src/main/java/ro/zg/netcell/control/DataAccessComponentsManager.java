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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ro.zg.cfgengine.core.configuration.ConfigurationManager;
import ro.zg.cfgengine.core.exceptions.ConfigurationException;
import ro.zg.cfgengine.util.PackageCfgLoader;
import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.netcell.constants.ResourcesTypes;
import ro.zg.netcell.vo.InputParameter;
import ro.zg.netcell.vo.definitions.DataAccessComponentDefinition;
import ro.zg.netcell.vo.definitions.EntitiesTypes;
import ro.zg.netcell.vo.definitions.EntityType;
import ro.zg.scriptdao.core.CommandTemplate;
import ro.zg.util.data.ListMap;
import ro.zg.util.data.UserInputParameter;

public class DataAccessComponentsManager extends BaseEntityManager<DataAccessComponentDefinition> {
    private String handlersFile;
    private String configFile;
    private DatasourcesCommandsManager datasourceCommandsController;

    public void init() throws ContextAwareException {
	String dataSourcesCommandsHandler = resourcesManager
		.getResourceFullPath(ResourcesTypes.DATASOURCES_COMMANDS_HANDLER_FILE);
	String dataSourcesCommandsConfig = resourcesManager
		.getResourceFullPath(ResourcesTypes.DATASOURCES_COMMANDS_CONFIG_FILE);
	datasourceCommandsController = new DatasourcesCommandsManager(dataSourcesCommandsConfig,
		dataSourcesCommandsHandler);
	datasourceCommandsController.setResourcesLoader(resourcesLoader);

	handlersFile = resourcesManager.getResourceFullPath(ResourcesTypes.DEFINITIONS_HANDLER_FILE);
	configFile = resourcesManager.getResourceFullPath(ResourcesTypes.DATA_ACCESS_COMPONENTS_CONFIG_FILE);

    }

    private Map<String, DataAccessComponentDefinition> load() throws ContextAwareException {
	ConfigurationManager cfgManager;
	try {
	    cfgManager = PackageCfgLoader.getInstance().load(handlersFile, configFile, getResourcesLoader());
	    Map<String, DataAccessComponentDefinition> entities = (Map) cfgManager.getAllObjects();

	    Map<String, Map<String, CommandTemplate>> commands;
	    try {
		commands = datasourceCommandsController.load();
	    } catch (ConfigurationException e) {
		throw new ContextAwareException("ERROR_LOADING_DATASOURCES_COMMANDS", e);
	    }

	    for (Object obj : entities.values()) {
		if (obj instanceof DataAccessComponentDefinition) {
		    DataAccessComponentDefinition def = (DataAccessComponentDefinition) obj;
		    populateDataAccessComponents(def, commands);
		}
	    }
	    return entities;

	} catch (ConfigurationException e) {
	    throw new ContextAwareException("ERROR_LOADING_EXECUTABLE_ENTITIES", e);
	}
    }

    private void populateDataAccessComponents(DataAccessComponentDefinition dacd,
	    Map<String, Map<String, CommandTemplate>> commands) {
	Map<String, CommandTemplate> commandsForDatasource = commands.get(dacd.getDataSourceName());
	CommandTemplate ct = commandsForDatasource.get(dacd.getCommandName());
//	if(ct == null) {
//	    return;
//	}
	UserInputParameter uip = new UserInputParameter();
	uip.setName(DataAccessComponentDefinition.COMMAND_CONTENT);
	uip.setValue(ct.getScript());
	uip.setInputType(UserInputParameter.UNKNOWN_LARGE_VALUE);
	UserInputParameter commandType = new UserInputParameter();
	commandType.setName(DataAccessComponentDefinition.COMMAND_TYPE);
	commandType.setValue(ct.getType());
	commandType.setInputType(UserInputParameter.VALUE_FROM_LIST);

	dacd.getConfigData().addUserIntpuParam(commandType);
	dacd.getConfigData().addUserIntpuParam(uip);
    }

    public DataAccessComponentDefinition createEntityDirectoryStructure(String name, EntityType type) throws ContextAwareException {
	throw new UnsupportedOperationException();
    }

    public List<String> getDefinitionTypes() throws ContextAwareException {
	return new ArrayList<String>();
    }

    public List<String> getDefinitionTypes(String parentType) throws ContextAwareException {
	List<String> types = new ArrayList<String>();
	types.add(EntitiesTypes.DATA_ACCESS_COMPONENT);
	return types;
    }

    public Map<String, List<DataAccessComponentDefinition>> getEntities() throws ContextAwareException {
	// return new HashMap<String, List<DataAccessComponentDefinition>>();
	throw new UnsupportedOperationException();
    }

    public Map<String, DataAccessComponentDefinition> getEntitiesAsMap() throws ContextAwareException {
	Map<String, DataAccessComponentDefinition> map = new LinkedHashMap<String, DataAccessComponentDefinition>();
	for (Object o : load().values()) {
	    if (o instanceof DataAccessComponentDefinition) {
		DataAccessComponentDefinition dacd = (DataAccessComponentDefinition) o;
		map.put(dacd.getId(), dacd);
	    }
	}
	return map;
    }

    public DataAccessComponentDefinition saveEntity(DataAccessComponentDefinition entity) throws ContextAwareException {
	String templatesPath = resourcesManager.getResourceFullPath(ResourcesTypes.DATASOURCES_TEMPLATES_DIR);
	String dataSourceType = entity.getDataSourceType().toString();
	templatesPath = resourcesManager.getResourceRelativePath(templatesPath, dataSourceType.toLowerCase());
	templatesPath = resourcesManager.getResourceRelativePath(templatesPath, "dac");
	String templateName = "datasource-access-component-template.xml";
	String templatePath = templatesPath + File.separator + templateName;
	String dacDir = resourcesManager.getResourceFullPath(ResourcesTypes.DATA_ACCESS_COMPONENTS_DIR);
	// String entityFilePath = resourcesManager.getFullSystemPath(ResourcesTypes.DATA_ACCESS_COMPONENTS_DIR, entity
	// .getId());
	URL entityUrl = resourcesManager.getUrl(dacDir, getEntityFileForId(entity.getId()), true);
	generateDefinition(entityUrl, entity, templatePath, "entityDefinition");

	/* save the command */
	CommandTemplate dataSourceCommand = new CommandTemplate();
	dataSourceCommand.setName(entity.getCommandName());
	dataSourceCommand.setScript((String) entity.getConfigData().getParameterValue(
		DataAccessComponentDefinition.COMMAND_CONTENT));
	dataSourceCommand.setType((String) entity.getConfigData().getParameterValue(
		DataAccessComponentDefinition.COMMAND_TYPE));

	List<String> parameterNames = new ArrayList<String>();
	for (InputParameter ip : entity.getInputParameters()) {
	    parameterNames.add(ip.getName());
	}
	dataSourceCommand.setParameterNames(parameterNames.toArray(new String[0]));
	URL commandUrl = getCommandPath(entity.getDataSourceName(), getFlatFileForEntityId(entity.getCommandName()));
	dataSourceCommand.setSource(commandUrl.getPath());

	String commandTemplatesPath = resourcesManager.getResourceRelativePath(templatesPath, "commands");
	String commandTemplate = resourcesManager.getResourceRelativePath(commandTemplatesPath, "command-template.xml");

	generateDefinition(commandUrl, dataSourceCommand, commandTemplate, "command");

	return entity;
    }

    private URL getCommandPath(String dataSourceName, String commandName) throws ContextAwareException {
	String datasourcesPath = resourcesManager.getResourceFullPath(ResourcesTypes.DATASOURCES_DIR);

	// String currentDsPath = resourcesManager.getResourceRelativePath(datasourcesPath, dataSourceName);
	String commandsPath = resourcesManager.getResourceRelativePath(dataSourceName, "commands");

	// return resourcesManager.getResourceRelativePath(commandsPath, commandName);
	return resourcesManager.getUrl(datasourcesPath, commandsPath+File.separator+commandName, true);
    }

    public boolean containsEntityWithId(String entityId) throws ContextAwareException {
	return load().containsKey(entityId);
    }

    public Map<String, List<String>> getDependencies() throws ContextAwareException {
	Map<String, DataAccessComponentDefinition> definitions = getEntitiesAsMap();
	ListMap<String, String> dependencies = new ListMap<String, String>();
	for (DataAccessComponentDefinition def : definitions.values()) {
	    dependencies.add(def.getDataSourceName(), def.getId());
	}
	return dependencies.getAll();
    }

    public boolean removeEntity(DataAccessComponentDefinition entity) throws ContextAwareException {
	String dacDir = resourcesManager.getFullSystemPath(ResourcesTypes.DATA_ACCESS_COMPONENTS_DIR);
	URL entityUrl = resourcesManager.getUrl(dacDir, getEntityFileForId(entity.getId()), false);
	if (entityUrl != null) {
	    /* remove component definition */
	    boolean removed = removeResource(entityUrl);
	    /* remove command */
	    removed &= removeResource(getCommandPath(entity.getDataSourceName(), entity.getCommandName()));
	    return removed;
	}
	return false;
    }

    public List<String> getTemplatesIds() throws ContextAwareException {
	return new ArrayList<String>();
    }

}
