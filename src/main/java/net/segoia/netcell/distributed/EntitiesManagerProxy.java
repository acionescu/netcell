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
package net.segoia.netcell.distributed;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.distributed.framework.SimpleTask;
import net.segoia.distributed.framework.Task;
import net.segoia.netcell.constants.DistributedServicesTypes;
import net.segoia.netcell.control.EntitiesManagerContract;
import net.segoia.netcell.entities.GenericEntity;
import net.segoia.netcell.vo.AdministrativeActionResponse;
import net.segoia.netcell.vo.DefinitionsRepository;
import net.segoia.netcell.vo.definitions.EntityDefinition;
import net.segoia.netcell.vo.definitions.EntityDefinitionSummary;
import net.segoia.netcell.vo.definitions.EntityType;
import net.segoia.util.data.GenericNameValueContext;


public class EntitiesManagerProxy extends AbstractProxy implements EntitiesManagerContract{
    
    public DefinitionsRepository getEntitiesDefinitions() throws ContextAwareException{
	try {
	    return (DefinitionsRepository)getResultForTask(createTask("getEntitiesDefinitions",null));
	} catch (Exception e) {
	    throw new ContextAwareException("PROXY_ERROR",e);
	}
    }
    
    private Task createTask(String method, Serializable content){
	SimpleTask task = new SimpleTask(DistributedServicesTypes.ENTITIES_MANAGER_DESC,content);
	task.setMethodName(method);
	return task;
    }
    
    private Task createTask(String method, Serializable content, boolean isBroadcast){
	SimpleTask task = new SimpleTask(DistributedServicesTypes.ENTITIES_MANAGER_DESC,content,isBroadcast);
	task.setMethodName(method);
	return task;
    }

    public EntityDefinition createEntity(EntityDefinition entity) throws ContextAwareException {
	Task t = createTask("createEntity", new Serializable[]{entity});
	try {
	    return (EntityDefinition)getResultForTask(t);
	} catch (Exception e) {
	    throw new ContextAwareException("PROXY_ERROR",e);
	}
    }

    public EntityDefinition createEntityDirectoryStructure(String name, EntityType type) throws ContextAwareException {
	Task t = createTask("createEntity", new Serializable[]{name,type});
	try {
	    return (EntityDefinition)getResultForTask(t);
	} catch (Exception e) {
	    throw new ContextAwareException("PROXY_ERROR",e);
	}
    }

    public List<String> getDefinitionTypes() throws ContextAwareException {
	try {
	    return (List<String>)getResultForTask(createTask("getEntities",new Serializable[0]));
	} catch (Exception e) {
	    throw new ContextAwareException("PROXY_ERROR",e);
	}
    }

    public List<String> getDefinitionTypes(String parentType) throws ContextAwareException {
	Task t = createTask("getDefinitionTypes", new Serializable[]{parentType});
	try {
	    return (List<String>)getResultForTask(t);
	} catch (Exception e) {
	    throw new ContextAwareException("PROXY_ERROR",e);
	}
    }

    public EntityDefinition updateEntity(EntityDefinition entity) throws ContextAwareException {
	Task t = createTask("updateEntity", new Serializable[]{entity});
	try {
	    return (EntityDefinition)getResultForTask(t);
	} catch (Exception e) {
	    throw new ContextAwareException("PROXY_ERROR",e);
	}
    }

    public boolean containsEntityWithId(String entityId) throws ContextAwareException {
	Task t = createTask("containsEntityWithId", new Serializable[]{entityId});
	try {
	    return (Boolean)getResultForTask(t);
	} catch (Exception e) {
	    throw new ContextAwareException("PROXY_ERROR",e);
	}
    }

    public Map<String, GenericEntity<GenericNameValueContext>> getExecutableEntities() throws ContextAwareException {
	try {
	    return (Map)getResultForTask(createTask("getExecutableEntities",null));
	} catch (Exception e) {
	    throw new ContextAwareException("PROXY_ERROR",e);
	}
    }

    public boolean removeEntity(EntityDefinition entity) throws ContextAwareException {
	Task t = createTask("removeEntity", new Serializable[]{entity});
	try {
	    return (Boolean)getResultForTask(t);
	} catch (Exception e) {
	    throw new ContextAwareException("PROXY_ERROR",e);
	}
    }

    public List<String> getEntityDefinitionTemplatesIds(String parentType) throws ContextAwareException {
	Task t = createTask("getEntityDefinitionTemplatesIds", new Serializable[]{parentType});
	try {
	    return (List)getResultForTask(t);
	} catch (Exception e) {
	    throw new ContextAwareException("PROXY_ERROR",e);
	}
    }

    public Map<String,EntityDefinitionSummary> getEnitityDefinitionSummariesForType(String type) throws ContextAwareException {
	try {
	    return (Map<String,EntityDefinitionSummary>)getResultForTask(createTask("getEnitityDefinitionSummariesForType",new Serializable[]{type}));
	} catch (Exception e) {
	    throw new ContextAwareException("PROXY_ERROR",e);
	}
    }

    public boolean reload() throws Exception {
//	Task t = createTask("reload", null,true);
//	List<AdministrativeActionResponse> responses = getResultForBroadcastTask(t);
//	
//	return new AdministrativeActionResponse(null, (Serializable)responses);
	return false;
    }

}
