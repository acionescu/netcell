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
package ro.zg.netcell.control.commands;

import ro.zg.netcell.vo.definitions.EntityDefinition;
import ro.zg.netcell.vo.definitions.EntityType;
import ro.zg.util.data.GenericNameValueContext;

public class CreateEntity extends BaseCommandExecutor{

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	GenericNameValueContext response = new GenericNameValueContext();
	EntityDefinition def = (EntityDefinition)input.getValue("definition");
	if(def != null){
	    response.put("definition",getNetCellController().createEntity(def));
	}
	else{
	    String name = (String)input.getValue("name");
	    EntityType entityType = (EntityType)input.getValue("type");
	    response.put("definition",getNetCellController().createEntity(name,entityType));
	}
	return response;
    }

}
