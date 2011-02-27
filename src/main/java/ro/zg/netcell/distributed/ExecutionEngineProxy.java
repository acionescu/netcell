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
package ro.zg.netcell.distributed;

import java.io.Serializable;
import java.util.List;

import ro.zg.distributed.framework.SimpleTask;
import ro.zg.distributed.framework.Task;
import ro.zg.netcell.constants.DistributedServicesTypes;
import ro.zg.netcell.control.ExecutionEngineContract;
import ro.zg.netcell.vo.AdministrativeActionResponse;
import ro.zg.netcell.vo.WorkflowContext;
import ro.zg.util.data.GenericNameValueContext;

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
