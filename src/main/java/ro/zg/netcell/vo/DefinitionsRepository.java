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
package ro.zg.netcell.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import ro.zg.netcell.vo.definitions.EntityDefinition;
import ro.zg.util.data.repository.ObjectsRepository;

public class DefinitionsRepository extends ObjectsRepository<EntityDefinition>{
    /**
     * 
     */
    private static final long serialVersionUID = 2201130407989050029L;
    public static final String ID="id";
    public static final String TYPE = "type";
    
    public DefinitionsRepository(){
	addIndexProperty(ID);
	addIndexProperty(TYPE);
    }
    
    public boolean containsDefinitionWithId(String id){
	return containsObjectWithProperty(ID, id);
    }
    
    public boolean containsDefinitionsOfType(String type){
	return containsObjectWithProperty(TYPE, type);
    }
    
    public EntityDefinition getDefinitionById(String id){
	 return getSingleObjectForProperty(ID, id);
    }
    
    public Map<String,Collection<EntityDefinition>> getDefinitionsByType(){
	return (Map)getObjectsByProperty(TYPE);
    }
    
    public List<String> getAllIdsForType(String type){
	return new ArrayList(getObjectsMapForProperty(TYPE, type, ID).keySet());
    }
    
    public Map<String,EntityDefinition> getDefinitionsForTypeById(String type){
	return (Map)getObjectsMapForProperty(TYPE, type, ID);
    }
}
