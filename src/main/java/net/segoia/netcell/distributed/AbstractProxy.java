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
import java.util.ArrayList;
import java.util.List;

import net.segoia.distributed.framework.SynchronousProcessingNodeClient;
import net.segoia.distributed.framework.Task;
import net.segoia.distributed.framework.TaskProcessingResponse;
import net.segoia.netcell.vo.AdministrativeActionResponse;

public class AbstractProxy extends SynchronousProcessingNodeClient{

    protected Serializable getResultForTask(Task t) throws Exception{
	TaskProcessingResponse resp = super.processTask(t);
	if(resp.getException() != null){
	    throw resp.getException();
	}
//	if(resp.getResult() == null) {
//	    System.out.println(this.getClass().getName()+":NULL_RESPONSE for "+resp.getTaskId()+" "+resp.getException());
//	}
	return resp.getResult();
    }
    
    protected List<AdministrativeActionResponse> getResultForBroadcastTask(Task t) throws Exception {
	TaskProcessingResponse resp = super.processTask(t);
	if(resp.getException() != null){
	    throw resp.getException();
	}
	List<AdministrativeActionResponse> responseList = new ArrayList<AdministrativeActionResponse>();
	for(TaskProcessingResponse tpr : (List<TaskProcessingResponse>)resp.getResult()) {
	    
	    responseList.add(new AdministrativeActionResponse(tpr.getProcessingNodeAddress().toString(), tpr.getResult(),tpr.getException()));
	}
	return responseList;
    }
    
}
