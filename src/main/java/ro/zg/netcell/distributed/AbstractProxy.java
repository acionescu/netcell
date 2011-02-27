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
import java.util.ArrayList;
import java.util.List;

import ro.zg.distributed.framework.SynchronousProcessingNodeClient;
import ro.zg.distributed.framework.Task;
import ro.zg.distributed.framework.TaskProcessingResponse;
import ro.zg.netcell.vo.AdministrativeActionResponse;

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
