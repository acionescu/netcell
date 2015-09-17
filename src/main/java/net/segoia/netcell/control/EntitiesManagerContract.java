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
import net.segoia.netcell.entities.GenericEntity;
import net.segoia.netcell.vo.DefinitionsRepository;
import net.segoia.netcell.vo.definitions.EntityDefinition;
import net.segoia.netcell.vo.definitions.EntityDefinitionSummary;
import net.segoia.netcell.vo.definitions.EntityType;
import net.segoia.util.data.GenericNameValueContext;

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
