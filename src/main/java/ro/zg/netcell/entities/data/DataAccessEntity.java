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
package ro.zg.netcell.entities.data;

import java.util.HashMap;
import java.util.Map;

import ro.zg.netcell.entities.GenericEntity;
import ro.zg.scriptdao.core.CommandManager;
import ro.zg.util.data.GenericNameValueContext;

public class DataAccessEntity extends GenericEntity<GenericNameValueContext>{

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	CommandManager<GenericNameValueContext> commandManager = (CommandManager<GenericNameValueContext>)input.getValue("commandManager");
	String commandName = (String)input.getValue("commandName");
	GenericNameValueContext argsContext = (GenericNameValueContext)input.getValue("argsContext");
	
	String[] argumentsNames = commandManager.getCommandArguments(commandName);
	Map<String,Object> argsMap = new HashMap<String, Object>();
	/* we expect that the input params have the same name as the command expected params */
	for(int i=0;i<argumentsNames.length;i++){
	    String argName = argumentsNames[i];
	    argsMap.put(argName,argsContext.getValue(argName));
	}
	return (GenericNameValueContext)commandManager.executeCommand(commandName, argsMap);
    }

}
