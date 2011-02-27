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

import java.util.List;
import java.util.Map;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.netcell.entities.GenericEntity;
import ro.zg.netcell.vo.DefinitionsRepository;
import ro.zg.netcell.vo.definitions.EntityDefinition;
import ro.zg.netcell.vo.definitions.EntityDefinitionSummary;
import ro.zg.netcell.vo.definitions.EntityType;
import ro.zg.util.data.GenericNameValueContext;

public interface EntitiesManagerContract {
    DefinitionsRepository getEntitiesDefinitions() throws ContextAwareException;
    EntityDefinition createEntity(EntityDefinition entity) throws ContextAwareException;
    EntityDefinition createEntityDirectoryStructure(String name, EntityType type) throws ContextAwareException;
    EntityDefinition updateEntity(EntityDefinition entity) throws ContextAwareException;
    boolean removeEntity(EntityDefinition entity) throws ContextAwareException;
    List<String> getDefinitionTypes() throws ContextAwareException;
    List<String> getDefinitionTypes(String parentType) throws ContextAwareException;
    boolean containsEntityWithId(String entityId) throws ContextAwareException;
    Map<String,GenericEntity<GenericNameValueContext>> getExecutableEntities() throws ContextAwareException;
    List<String> getEntityDefinitionTemplatesIds(String parentType) throws ContextAwareException;
    Map<String,EntityDefinitionSummary> getEnitityDefinitionSummariesForType(String type) throws ContextAwareException;
    boolean reload() throws Exception;

}
