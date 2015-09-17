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

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.distributed.framework.SimpleTask;
import net.segoia.distributed.framework.SynchronousProcessingNodeClient;
import net.segoia.distributed.framework.Task;
import net.segoia.netcell.constants.DistributedServicesTypes;
import net.segoia.util.monitoring.LogEvent;
import net.segoia.util.monitoring.MonitoringManager;

public class MonitoringManagerProxy extends SynchronousProcessingNodeClient implements MonitoringManager{

    public void addLogEvent(LogEvent logEvent) throws ContextAwareException {
	Task t = createTask("addLogEvent", new Serializable[]{logEvent});
	processTask(t);
    }

    public long startMonitoring(String monitoringKey) throws ContextAwareException {
	Task t = createTask("startMonitoring", new Serializable[]{monitoringKey});
	return (Long)processTask(t).getResult();
    }

    public void stopMonitoring(String monitoringKey,long counterId) throws ContextAwareException {
	Task t = createTask("stopMonitoring", new Serializable[]{monitoringKey,counterId});
	processTask(t);
    }
    
    private Task createTask(String method, Serializable content){
	SimpleTask task = new SimpleTask(DistributedServicesTypes.MONITORING_MANAGER_DESC,content);
	task.setMethodName(method);
	return task;
    }

    public long startMonitoring(String monitoringKey, String masterMonitorKey) throws ContextAwareException {
	Task t = createTask("startMonitoring", new Serializable[]{monitoringKey,masterMonitorKey});
	return (Long)processTask(t).getResult();
    }

}
