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

import ro.zg.util.execution.ExecutionEntity;
import ro.zg.util.execution.ExecutionEntityWrapper;
import ro.zg.util.monitoring.MonitoringManager;

public class CommandControllerWrapper extends ExecutionEntityWrapper<ExecutionEntity<Command,CommandResponse>, Command, CommandResponse> implements NetCell{
    private MonitoringManager monitoringManager;
    private RequestsManager requestsManager;
    private long nextId;
    
    public CommandResponse execute(Command input) throws Exception{
	
//	String requestId = getNextId();
//	input.setRequestId(requestId);
//	requestsManager.saveRequest(input);
	long counterId = monitoringManager.startMonitoring(input.getName(),"All-Commands");
	monitoringManager.stopMonitoring(input.getName(),counterId);
	CommandResponse resp = super.execute(input);
//	requestsManager.saveResponse(resp);
	return resp;
    }

    private String getNextId() {
	String nodeId = NetCellLoader.getNetcellControllerInstance().getNodeId();
	String reqId = nodeId+"/"+System.currentTimeMillis()+"/"+(++nextId);
	return reqId;
    }
    
    /**
     * @return the monitoringManager
     */
    public MonitoringManager getMonitoringManager() {
        return monitoringManager;
    }

    /**
     * @param monitoringManager the monitoringManager to set
     */
    public void setMonitoringManager(MonitoringManager monitoringManager) {
        this.monitoringManager = monitoringManager;
    }

    /**
     * @return the requestsManager
     */
    public RequestsManager getRequestsManager() {
        return requestsManager;
    }

    /**
     * @param requestsManager the requestsManager to set
     */
    public void setRequestsManager(RequestsManager requestsManager) {
        this.requestsManager = requestsManager;
    }
    
}
