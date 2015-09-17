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

import java.util.List;
import java.util.Map;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.netcell.vo.DefinitionsRepository;
import net.segoia.netcell.vo.WorkflowContext;
import net.segoia.netcell.vo.definitions.EntityDefinition;
import net.segoia.netcell.vo.definitions.EntityDefinitionSummary;
import net.segoia.netcell.vo.definitions.EntityType;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.execution.ExecutionEntity;
import net.segoia.util.monitoring.MonitoringManager;

public interface NetCellControllerContract extends ExecutionEntity<GenericNameValueContext, GenericNameValueContext>{
    
    DefinitionsRepository getDefinitions() throws ContextAwareException;
    
    List<String> getDefinitionTypes() throws ContextAwareException;
    
    List<String> getDefinitionTypes(String parentType) throws ContextAwareException;
    
    public EntityDefinition createEntity(EntityDefinition ed) throws ContextAwareException;
    
    public EntityDefinition createEntity(String name, EntityType entityType) throws ContextAwareException;
    
    public EntityDefinition updateEntity(EntityDefinition ed) throws ContextAwareException;
    
    public boolean removeEntity(EntityDefinition ed) throws ContextAwareException;
    
    public MonitoringManager getMonitoringManager() throws ContextAwareException;

    boolean removeEntity(String entityId) throws ContextAwareException;
    
    boolean refresh() throws ContextAwareException;
    
    boolean simulateRefresh() throws ContextAwareException;
    
    List<String> getTemplatesIdsForEntity(String entityType) throws ContextAwareException;
    
    public GenericNameValueContext execute(WorkflowContext wfContext) throws Exception;
    
    public String getNodeId();
    
    Map<String,EntityDefinitionSummary> getWorkflowDefinitionSummaries() throws ContextAwareException;
    
    
}
