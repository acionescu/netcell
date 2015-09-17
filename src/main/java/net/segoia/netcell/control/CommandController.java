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

import java.util.Map;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;
import net.segoia.netcell.control.Command;
import net.segoia.netcell.control.CommandResponse;
import net.segoia.netcell.control.NetCell;
import net.segoia.netcell.entities.GenericEntity;
import net.segoia.util.data.GenericNameValue;
import net.segoia.util.data.GenericNameValueContext;

public class CommandController implements NetCell{
    private Map<String, GenericEntity<GenericNameValueContext>> commandExecutors;
    
    public CommandResponse execute(Command input) throws Exception {
	String commandName = input.getName();
	GenericEntity<GenericNameValueContext> executor = commandExecutors.get(commandName);
	if(executor == null){
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("command_name",commandName));
	    throw new ContextAwareException("UNKNOWN_COMMAND",ec); 
	}
	GenericNameValueContext response = executor.execute(input);
	CommandResponse commandResponse = new CommandResponse();
	response.copyTo(commandResponse);
	commandResponse.setResponseCode("rc0");
	commandResponse.setRequestId(input.getRequestId());
	commandResponse.setName(input.getName());
	return commandResponse;
    }

    public Map<String, GenericEntity<GenericNameValueContext>> getCommandExecutors() {
        return commandExecutors;
    }

    public void setCommandExecutors(Map<String, GenericEntity<GenericNameValueContext>> commandExecutors) {
        this.commandExecutors = commandExecutors;
    }
}
