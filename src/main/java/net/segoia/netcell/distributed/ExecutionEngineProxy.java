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

import net.segoia.distributed.framework.SimpleTask;
import net.segoia.distributed.framework.Task;
import net.segoia.netcell.constants.DistributedServicesTypes;
import net.segoia.netcell.control.ExecutionEngineContract;
import net.segoia.netcell.vo.AdministrativeActionResponse;
import net.segoia.netcell.vo.WorkflowContext;
import net.segoia.util.data.GenericNameValueContext;

public class ExecutionEngineProxy extends AbstractProxy implements ExecutionEngineContract{

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	Task t = createTask("execute", new Serializable[]{input},false);
	return (GenericNameValueContext)getResultForTask(t);
    }
    
    public GenericNameValueContext execute(WorkflowContext wfContext) throws Exception {
	Task t = createTask("execute", new Serializable[]{wfContext},false);
	return (GenericNameValueContext)getResultForTask(t);
    }
    
    private Task createTask(String method, Serializable content, boolean isBroadcast){
	SimpleTask task = new SimpleTask(DistributedServicesTypes.EXECUTION_ENGINE_DESC,content);
	task.setMethodName(method);
	return task;
    }

    public boolean reload() throws Exception {
//	Task t = createTask("reload", null,true);
//	List<AdministrativeActionResponse> responses = getResultForBroadcastTask(t);
//	
//	return new AdministrativeActionResponse(null, (Serializable)responses);
	
	return false;
    }

}
