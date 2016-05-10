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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.segoia.cfgengine.core.configuration.ConfigurationManager;
import net.segoia.cfgengine.core.exceptions.ConfigurationException;
import net.segoia.cfgengine.util.PackageCfgLoader;
import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;
import net.segoia.netcell.entities.GenericEntity;
import net.segoia.netcell.vo.DefinitionsRepository;
import net.segoia.netcell.vo.definitions.EntityDefinition;
import net.segoia.netcell.vo.definitions.EntityDefinitionSummary;
import net.segoia.netcell.vo.definitions.EntityType;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.data.ListMap;
import net.segoia.util.logging.Logger;
import net.segoia.util.logging.MasterLogManager;

public class EntitiesManager implements EntitiesManagerContract {
    private static Logger logger = MasterLogManager.getLogger(EntitiesManager.class.getName());
    private ClassLoader resourcesLoader;
    private ResourcesManager resourcesManager;
    private Map<String, BaseEntityManager<EntityDefinition>> managers;
    private ConfigurationManager executableEntitiesCfgManager;

    private DefinitionsRepository definitionsRepository;
    
    public void start() throws ContextAwareException {
	init();
    }
    
    public void init() throws ContextAwareException {
	logger.info("Entities Manger - start initialization");
	initEntitiesDefinitions();
	initExecutableEntities();
	refreshDefinitions();
    }

    private void initEntitiesDefinitions() throws ContextAwareException {
	if (managers == null) {
	    return;
	}
	for (BaseEntityManager<EntityDefinition> entityManager : managers.values()) {
	    entityManager.setResourcesLoader(resourcesLoader);
	    entityManager.init();
	}
    }

    private void initExecutableEntities() throws ContextAwareException {
	String engineHandlerFile = resourcesManager.getResourceFullPath("ENGINE_HANDLER_FILE");
	String engineConfigFile = resourcesManager.getResourceFullPath("ENGINE_CONFIG_FILE");
	try {
	    executableEntitiesCfgManager = PackageCfgLoader.getInstance().load(engineHandlerFile, engineConfigFile,
		    resourcesLoader);
	} catch (ConfigurationException e) {
	    throw new ContextAwareException("ERROR_LOADING_EXECUTABLE_ENTITIES", e);
	}

    }

    private void refreshDefinitions() throws ContextAwareException {
	Map<String, List<EntityDefinition>> allEntities = clasifyDefinitions(getEntitiesAsMap()).getAll();
	DefinitionsRepository newDefinitionsRepository = new DefinitionsRepository();

	for (List<EntityDefinition> defList : allEntities.values()) {
	    newDefinitionsRepository.addObjects(defList);
	}
	definitionsRepository = newDefinitionsRepository;
    }

    private EntityManager<EntityDefinition> getManagerForEntity(EntityDefinition entity) throws ContextAwareException {
	return getManagerForEntity(entity.getEntityType().getRoot().getType());
    }

    private EntityManager<EntityDefinition> getManagerForEntity(String entityType) throws ContextAwareException {
	if (managers == null) {
	    throw new ContextAwareException("NO_ENTITY_MANAGER_DEFINED");
	}
	EntityManager<EntityDefinition> em = managers.get(entityType);
	if (em == null) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put("entity", entityType);
	    throw new ContextAwareException("NO_ENTITY_MANAGER_DEFINED", ec);
	}
	return em;
    }

    public Map<String, GenericEntity<GenericNameValueContext>> getExecutableEntities() {
	return (Map) executableEntitiesCfgManager.getObjectsByTagName("entity");
    }

    private ListMap<String, EntityDefinition> clasifyDefinitions(Map<String, EntityDefinition> definitions)
	    throws ContextAwareException {
	ListMap<String, EntityDefinition> definitionsByTypes = new ListMap<String, EntityDefinition>();

	for (Object o : definitions.values()) {
	    if (o instanceof EntityDefinition) {
		EntityDefinition def = (EntityDefinition) o;
		definitionsByTypes.add(def.getType(), def);
	    }
	}
	return definitionsByTypes;
    }

    public EntityDefinition createEntity(EntityDefinition entity) throws ContextAwareException {
	EntityManager<EntityDefinition> manager = getManagerForEntity(entity);
	EntityDefinition newEntity = manager.createEntity(entity);
	getEntitiesDefinitions();
	return newEntity;
    }

    public Map<String, EntityDefinition> getEntitiesAsMap() throws ContextAwareException {
	Map<String, EntityDefinition> map = new LinkedHashMap<String, EntityDefinition>();
	for (EntityManager<EntityDefinition> m : managers.values()) {
	    map.putAll(m.getEntitiesAsMap());
	}
	return map;
    }

    public DefinitionsRepository getEntitiesDefinitions() throws ContextAwareException {
	return definitionsRepository;
    }

    public Map<String, EntityDefinitionSummary> getEnitityDefinitionSummariesForType(String type) {
	Map<String, EntityDefinition> definitions = definitionsRepository.getDefinitionsForTypeById(type);
	Map<String, EntityDefinitionSummary> edsMap = new HashMap<String, EntityDefinitionSummary>();
	for (EntityDefinition ed : definitions.values()) {
	    try {
		edsMap.put(ed.getId(), EntityDefinitionSummary.createFromEntityDefinition(ed));
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	return edsMap;
    }

    public EntityDefinition saveEntity(EntityDefinition entity) throws ContextAwareException {
	EntityManager<EntityDefinition> manager = getManagerForEntity(entity);
	return manager.saveEntity(entity);
    }

    public EntityDefinition updateEntity(EntityDefinition entity) throws ContextAwareException {
	EntityManager<EntityDefinition> manager = getManagerForEntity(entity);
	return manager.updateEntity(entity);
    }

    public boolean containsEntityWithId(String entityId) throws ContextAwareException {
	for (EntityManager<EntityDefinition> em : managers.values()) {
	    if (em.containsEntityWithId(entityId)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * @return the managers
     */
    public Map<String, BaseEntityManager<EntityDefinition>> getManagers() {
	return managers;
    }

    /**
     * @return the resourcesLoader
     */
    public ClassLoader getResourcesLoader() {
	return resourcesLoader;
    }

    /**
     * @param resourcesLoader
     *            the resourcesLoader to set
     */
    public void setResourcesLoader(ClassLoader resourcesLoader) {
	this.resourcesLoader = resourcesLoader;
    }

    /**
     * @param managers
     *            the managers to set
     */
    public void setManagers(Map<String, BaseEntityManager<EntityDefinition>> managers) {
	this.managers = managers;
    }

    public List<String> getDefinitionTypes() throws ContextAwareException {
	return new ArrayList(getEntitiesDefinitions().getDefinitionsByType().keySet());
    }

    public List<String> getDefinitionTypes(String parentType) throws ContextAwareException {
	return getManagerForEntity(parentType).getDefinitionTypes();
    }

    public EntityDefinition createEntityDirectoryStructure(String name, EntityType type) throws ContextAwareException {
//	List<String> hierarchyList = type.getHierarchyAsList();
//	String rootType = hierarchyList.remove(0);
	EntityType rootType = type.getRoot();
//	return getManagerForEntity(rootType).createEntity(name,
//		EntityType.getEntityType(hierarchyList.toArray(new String[0])));
	return getManagerForEntity(rootType.getType()).createEntityDirectoryStructure(name, rootType.getChildType());
	
    }

    /**
     * @return the resourcesManager
     */
    public ResourcesManager getResourcesManager() {
	return resourcesManager;
    }

    /**
     * @param resourcesManager
     *            the resourcesManager to set
     */
    public void setResourcesManager(ResourcesManager resourcesManager) {
	this.resourcesManager = resourcesManager;
    }

    public Map<String, List<String>> getDependencies() throws ContextAwareException {
	ListMap<String, String> dependencies = new ListMap<String, String>();
	for (EntityManager<EntityDefinition> em : managers.values()) {
	    dependencies.add(em.getDependencies());
	}
	return dependencies.getAll();
    }

    public boolean removeEntity(EntityDefinition entity) throws ContextAwareException {
	List<String> dependencies = getDependencies().get(entity.getId());
	if (dependencies != null && dependencies.size() > 0) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put("dependencies", dependencies);
	    throw new ContextAwareException("CANNOT_REMOVE_ENTITY", ec);
	}
	EntityManager<EntityDefinition> manager = getManagerForEntity(entity);
	boolean status = manager.removeEntity(entity);
	refreshDefinitions();
	return status;
    }

    public List<String> getEntityDefinitionTemplatesIds(String parentType) throws ContextAwareException {
	return getManagerForEntity(parentType).getTemplatesIds();
    }

    public boolean reload() throws ContextAwareException {
	try {
	    init();
	    return true;
	} catch (Exception e) {
	    throw new ContextAwareException("RELOAD_FAILED",e);
	}
    }

}
