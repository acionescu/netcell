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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.distributed.framework.ProcessingResponseReceiver;
import ro.zg.distributed.framework.SimpleTask;
import ro.zg.distributed.framework.SubmitTaskResponse;
import ro.zg.distributed.framework.TaskProcessingResponse;
import ro.zg.netcell.control.commands.mapping.CommandResponseBuilder;
import ro.zg.netcell.control.commands.mapping.CommandToTaskMapping;
import ro.zg.netcell.control.commands.mapping.TaskMappingInfo;
import ro.zg.util.data.GenericNameValue;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

public class CommandRouter implements NetCell {
    private Map<String, CommandToTaskMapping> commandMappings;
    private DistributedServicesManager distributedServicesManager;
    private Logger logger = MasterLogManager.getLogger("CommandRouter");

    public CommandResponse execute(Command input) throws Exception {
//	logger.debug("Executing: "+input);
	String commandName = input.getName();
	CommandToTaskMapping commandMapping = commandMappings.get(commandName);

	if (commandMapping == null) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("command_name", commandName));
	    throw new ContextAwareException("UNKNOWN_COMMAND", ec);
	}
	CommandResponse response = executeTasksForCommand(commandMapping, input);
//	logger.debug("Responding: "+response);
	return response;
    }

    private CommandResponse executeTasksForCommand(CommandToTaskMapping commandMapping, Command input)
	    throws ContextAwareException {
	List<TaskMappingInfo> tasksList = commandMapping.getTasksList();
	if (tasksList != null) {
	    List<TaskProcessingResponse> responsesList = new ArrayList<TaskProcessingResponse>();
	    for (TaskMappingInfo tmi : tasksList) {
		ProcessingResponseReceiver receiver = tmi.getResponseReceiver();
		SimpleTask task = getTaskFromMapping(tmi, input);
		TaskProcessingResponse resp = processTask(task, receiver);
		responsesList.add(resp);
		/* 
		 * stop execution if the task failed and stopOnFailed is on
		 */
		if(tmi.isStopOnFailedOn() && ! resp.isSucessful()) {
		    break;
		}
	    }
	    return buildCommandResponse(commandMapping, input, responsesList);
	}
	ExceptionContext ec = new ExceptionContext();
	ec.put("commandName", input.getName());
	throw new ContextAwareException("NO_TASKS_DEFINED_FOR_COMMAND", ec);
    }

    private CommandResponse buildCommandResponse(CommandToTaskMapping commandMapping, Command input,
	    List<TaskProcessingResponse> responses) throws ContextAwareException {
	CommandResponse commandResponse = null;
	CommandResponseBuilder builder = commandMapping.getCommandResponseBuilder();
	if (builder != null) {
	    commandResponse = builder.buildResponse(responses);
	} else {
	    List<TaskMappingInfo> taskList = commandMapping.getTasksList();
//	    if (taskList != null && taskList.size() == responses.size()) {
		commandResponse = new CommandResponse();
		for (int i = 0; i < responses.size(); i++) {
		    TaskMappingInfo taskInfo = taskList.get(i);
		    Serializable result = getResultFromTaskResponse(responses.get(i));
		    if (taskInfo.isUseResponseAsOutputContextOn()) {
			((GenericNameValueContext) result).copyTo(commandResponse);
		    } else {
			commandResponse.put(taskInfo.getOutputParamName(), result);
		    }
		}
//	    }
	}
	if (commandResponse != null) {
	    commandResponse.setName(input.getName());
	    commandResponse.setRequestId(input.getRequestId());
	    commandResponse.setResponseCode("rc0");
	} else {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put("input", input);
	    throw new ContextAwareException("FAILED_BUILDING_COMMAND_RESPONSE", ec);
	}
	return commandResponse;
    }

    private Serializable getResultFromTaskResponse(TaskProcessingResponse response) throws ContextAwareException {
	if (response.isSucessful()) {
	    return response.getResult();
	}
	throw new ContextAwareException("TASK_PROCESSING_ERROR", response.getException());
    }

    private SimpleTask getTaskFromMapping(TaskMappingInfo mapping, Command input) {
	List<String> inputParamNames = mapping.getInputParamNames();
	Serializable[] inputParams = null;
	if (mapping.isSendCommandContextAsParamOn()) {
	    inputParams = new Serializable[] { input };
	} else if (inputParamNames != null) {
	    inputParams = input.getValues(inputParamNames).toArray(new Serializable[0]);
	}

	SimpleTask task = new SimpleTask(mapping.getServiceDescription(), inputParams, mapping.isBroadcast());
	task.setMethodName(mapping.getMethod());
	return task;
    }

    private TaskProcessingResponse processTask(SimpleTask task, ProcessingResponseReceiver asyncResponseReceiver)
	    throws ContextAwareException {
	if (asyncResponseReceiver != null) {
	    SubmitTaskResponse submitResponse = distributedServicesManager.processAsynchronousTask(task,
		    asyncResponseReceiver);
	    if (!submitResponse.isSuccessfull()) {
		throw new ContextAwareException("TASK_SUBMIT_ERROR", submitResponse.getError());
	    }
	    return null;
	} else {
	    return distributedServicesManager.processSynchronousTask(task);
	}
    }

    /**
     * @return the commandMappings
     */
    public Map<String, CommandToTaskMapping> getCommandMappings() {
	return commandMappings;
    }

    /**
     * @return the distributedServicesManager
     */
    public DistributedServicesManager getDistributedServicesManager() {
	return distributedServicesManager;
    }

    /**
     * @param commandMappings
     *            the commandMappings to set
     */
    public void setCommandMappings(Map<String, CommandToTaskMapping> commandMappings) {
	this.commandMappings = commandMappings;
    }

    /**
     * @param distributedServicesManager
     *            the distributedServicesManager to set
     */
    public void setDistributedServicesManager(DistributedServicesManager distributedServicesManager) {
	this.distributedServicesManager = distributedServicesManager;
    }

}
