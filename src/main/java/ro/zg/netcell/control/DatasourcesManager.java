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
import java.util.List;
import java.util.Map;

import ro.zg.cfgengine.core.configuration.ConfigurationManager;
import ro.zg.cfgengine.core.exceptions.ConfigurationException;
import ro.zg.cfgengine.util.PackageCfgLoader;
import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.netcell.constants.ResourcesTypes;
import ro.zg.netcell.control.generators.DatasourceEntityDefinitionGenerator;
import ro.zg.netcell.vo.definitions.DataSourceDefinition;
import ro.zg.netcell.vo.definitions.DataSourceType;
import ro.zg.netcell.vo.definitions.EntityType;
import ro.zg.util.data.ConfigurationData;
import ro.zg.util.data.GenericNameValue;
import ro.zg.util.data.ObjectsUtil;

public class DatasourcesManager extends BaseEntityManager<DataSourceDefinition> {
    private Map<String, DataSourceDefinition> datasourcesTemplates;
    private Map<String, DataSourceDefinition> datasourcesDefinitions;
    
    private Map<String, DatasourceEntityDefinitionGenerator> generators = new HashMap<String, DatasourceEntityDefinitionGenerator>();
    private String handlersFile;
    private String templatesConfigFile;
    private String definitionsConfigFile;

    public DatasourcesManager() {

    }

    public void init() throws ContextAwareException {
	handlersFile = resourcesManager.getResourceFullPath(ResourcesTypes.DATASOURCES_HANDLER_FILE);
	templatesConfigFile = resourcesManager.getResourceFullPath(ResourcesTypes.DATASOURCES_TEMPLATES_CONFIG_FILE);
	definitionsConfigFile = resourcesManager.getResourceFullPath(ResourcesTypes.DATASOURCES_DEFINITIONS_CONFIG_FILE);

    }

    public DatasourcesManager(String handlersFile, String templatesConfigFile, String definitionsConfigFile) {
	this.handlersFile = handlersFile;
	this.templatesConfigFile = templatesConfigFile;
	this.definitionsConfigFile = definitionsConfigFile;
    }

    /**
     * @return the datasourcesTemplates
     */
    public Map<String, DataSourceDefinition> getDatasourcesTemplates() {
	try {
	    ConfigurationManager templatesCfgManager = PackageCfgLoader.getInstance().load(handlersFile,
		    templatesConfigFile,getResourcesLoader());
	    return (Map) templatesCfgManager.getAllObjects();
	} catch (ConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * @return the datasourcesDefinitions
     */
    public Map<String, DataSourceDefinition> getDatasourcesDefinitions() {
	try {
	    ConfigurationManager definitionsCfgManager = PackageCfgLoader.getInstance().load(handlersFile,
		    definitionsConfigFile,getResourcesLoader());
	    return (Map) definitionsCfgManager.getAllObjects();
	} catch (ConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

    public boolean containsId(String entityId) {
	return getDatasourcesDefinitions().containsKey(entityId);
    }

    public DataSourceDefinition updateEntity(DataSourceDefinition ed) throws ContextAwareException {
	ed.validate();
	if (!containsId(ed.getId())) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("id", ed.getId()));
	    throw new ContextAwareException("UNKNOWN_ENTITY", ec);
	}
	String fileName = ed.getId() + ".xml";
	makeBackup(ed);
	return saveEntity(ed);
    }

    public Map<String, DataSourceDefinition> getEntitiesAsMap() throws ContextAwareException {
	return getDatasourcesDefinitions();
    }

    public DataSourceDefinition saveEntity(DataSourceDefinition entity) throws ContextAwareException {
//	String dsTemplatesDir = resourcesManager.getResourceFullPath(ResourcesTypes.DATASOURCES_TEMPLATES_DIR);
//	String connectionManagerTemplate = getDefinitionsTemplates().get("connection-manager");
//	String commandMangerTemplate = getDefinitionsTemplates().get("command-manager");
//	
//	String connectionManagerTemplatePath = resourcesManager.getResourceRelativePath(dsTemplatesDir, connectionManagerTemplate);
//	String commandManagerTemplatePath = resourcesManager.getResourceRelativePath(dsTemplatesDir, commandMangerTemplate);
//	
//	String currentDsDir = getDatasourceDirForName(entity.getId());
//	String commandManagerFileName = resourcesManager.getResourceRelativePath(currentDsDir,"command-manager-config");
//	String connectionManagerFileName = resourcesManager.getResourceRelativePath(currentDsDir,"connection-manager-config");
//	
//	generateDefinition(connectionManagerFileName,entity,connectionManagerTemplatePath,"datasourceDefinition");
//	generateDefinition(commandManagerFileName,entity,commandManagerTemplatePath,"datasourceDefinition");
//	
//	return entity;
	String entityType = entity.getDatasourceType().toString();
	DatasourceEntityDefinitionGenerator generator = generators.get(entityType);
	if(generator == null){
	    ExceptionContext ec = new ExceptionContext();
	    ec.put("entityType",entityType);
	    throw new ContextAwareException("NO_GENERATOR_REGISTERED_FOR_ENTITY",ec);
	}
	return generator.saveEntity(entity);
    }

    public Map<String, List<DataSourceDefinition>> getEntities() throws ContextAwareException {
	throw new UnsupportedOperationException();
    }

    public List<String> getDefinitionTypes() throws ContextAwareException {
	Map<String,DataSourceDefinition> templates = getDatasourcesTemplates();
	List<String> types = new ArrayList<String>();
	for(DataSourceDefinition dsd : templates.values()){
	    types.add(dsd.getDatasourceType().toString());
	}
	return types;
    }

    public List<String> getDefinitionTypes(String parentType) throws ContextAwareException {
	throw new UnsupportedOperationException();
    }

    public DataSourceDefinition createEntityDirectoryStructure(String name, EntityType entityType) throws ContextAwareException {
	String datasourcesDir = resourcesManager.getFullSystemPath(ResourcesTypes.DATASOURCES_DIR);
	String currentDsDir = resourcesManager.getResourceRelativePath(datasourcesDir, name);
	File dsDir = new File(currentDsDir);
	/* check if a datasource with this name already exists */
	if(dsDir.exists()){
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("id", name));
	    throw new ContextAwareException("INVALID_ID", ec);
	}
	/* create directory structure */
	createDatasourceDirectoryStructure(dsDir);
	
	Map<String,DataSourceDefinition> dsTemplates = getDatasourcesTemplates();
	DataSourceDefinition template = dsTemplates.get(entityType.getType());
	DataSourceDefinition dsDef = new DataSourceDefinition();
	dsDef.setId(name);
	dsDef.setDatasourceType(DataSourceType.valueOf(entityType.getType()));
	dsDef.setConfigData((ConfigurationData)ObjectsUtil.copy(template.getConfigData()));
	
	return dsDef;
    }
    
    private URL getDatasourceDirForName(String name) throws ContextAwareException{
	String datasourcesDir = resourcesManager.getFullSystemPath(ResourcesTypes.DATASOURCES_DIR);
	URL dsUrl = resourcesManager.getUrl(datasourcesDir, name, false);
//	String currentDsDir = resourcesManager.getResourceRelativePath(datasourcesDir, name);
	return dsUrl;
    }
    
    private void createDatasourceDirectoryStructure(File dsDir){
	File commandsDir = new File(dsDir,"commands");
	commandsDir.mkdirs();
    }

    public boolean containsEntityWithId(String entityId) throws ContextAwareException {
	return getDatasourcesDefinitions().containsKey(entityId);
    }

    public Map<String,List<String>> getDependencies() throws ContextAwareException {
	/* does not have any dependencies */
	return new HashMap<String, List<String>>();
    }

    public boolean removeEntity(DataSourceDefinition entity) throws ContextAwareException {
	return removeResource(getDatasourceDirForName(entity.getId()));
    }

    /**
     * @return the generators
     */
    public Map<String, DatasourceEntityDefinitionGenerator> getGenerators() {
        return generators;
    }

    /**
     * @param generators the generators to set
     */
    public void setGenerators(Map<String, DatasourceEntityDefinitionGenerator> generators) {
        this.generators = generators;
    }

    public List<String> getTemplatesIds() throws ContextAwareException {
	return new ArrayList<String>(getDefinitionsTemplates().keySet());
    }

}
