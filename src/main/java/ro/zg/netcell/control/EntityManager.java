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
import ro.zg.netcell.vo.definitions.EntityDefinition;
import ro.zg.netcell.vo.definitions.EntityType;


public interface EntityManager<E extends EntityDefinition> {

    public void init() throws ContextAwareException;
    E saveEntity(E entity) throws ContextAwareException;
    E createEntity(E entity) throws ContextAwareException;
    E createEntityDirectoryStructure(String name, EntityType type) throws ContextAwareException;
    E updateEntity(E entity) throws ContextAwareException;
    boolean removeEntity(E entity) throws ContextAwareException;
    Map<String,E> getEntitiesAsMap() throws ContextAwareException;
    Map<String,List<E>> getEntities() throws ContextAwareException;
    List<String> getDefinitionTypes() throws ContextAwareException;
    List<String> getDefinitionTypes(String parentType) throws ContextAwareException;
    boolean containsEntityWithId(String entityId) throws ContextAwareException;
    /**
     * Returns a map with the entity id as key and a list with all the ids of the 
     * entities that depend upon that entity as value
     * @return
     * @throws ContextAwareException
     */
    Map<String,List<String>> getDependencies() throws ContextAwareException;
    
    List<String> getTemplatesIds() throws ContextAwareException;
}
