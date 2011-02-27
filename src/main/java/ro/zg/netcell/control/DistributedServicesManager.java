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

import ro.zg.cfgengine.core.configuration.ConfigurationManager;
import ro.zg.cfgengine.util.UrlCfgLoader;
import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.distributed.framework.ProcessingNode;
import ro.zg.distributed.framework.ProcessingResponseReceiver;
import ro.zg.distributed.framework.SimpleTask;
import ro.zg.distributed.framework.SubmitTaskResponse;
import ro.zg.distributed.framework.SynchronousProcessingNodeClient;
import ro.zg.distributed.framework.TaskProcessingResponse;
import ro.zg.distributed.framework.cfg.DefaultProcessingNodeFactory;
import ro.zg.distributed.framework.cfg.ProcessingNodeConfiguration;
import ro.zg.netcell.distributed.EntitiesManagerProxy;
import ro.zg.netcell.distributed.ExecutionEngineProxy;
import ro.zg.netcell.distributed.MonitoringManagerProxy;

public class DistributedServicesManager {
    private ProcessingNode processingNode;
    private SynchronousProcessingNodeClient synchronousProcessingNodeClient;
    private EntitiesManagerProxy entitiesManager;
    private ExecutionEngineProxy executionEngine;
    private MonitoringManagerProxy monitoringManager;
    
    private String repositoryDir;
    private String entryPointFile;
    
    public void init() throws ContextAwareException{
//	String configFile = resourcesManager.getFullSystemPath(ResourcesTypes.DISTRIBUTED_SERVICES_CONFIG_FILE);
	try {
//	    ConfigurationManager cfgManager = PackageCfgLoader.getInstance().load(configFile);
	    ConfigurationManager cfgManager = UrlCfgLoader.getInstance().load(repositoryDir, entryPointFile);
	    ProcessingNodeConfiguration nodeCfg = (ProcessingNodeConfiguration)cfgManager.getObjectById("mainNodeConfiguration");
	    nodeCfg.setResourcesLoader(cfgManager.getResourcesLoader());
	    ResourcesManager resourcesManager = (ResourcesManager)cfgManager.getObjectById("resourcesManager");
	    resourcesManager.setResourcesLoader(cfgManager.getResourcesLoader());
	    processingNode = new DefaultProcessingNodeFactory().createObject(nodeCfg);
	} catch (Exception e) {
	    throw new ContextAwareException("ERROR_LOADING_DISTRIBUTED_MANAGER",e);
	}
	
	synchronousProcessingNodeClient=new SynchronousProcessingNodeClient();
	synchronousProcessingNodeClient.setProcessingNode(processingNode);
	
	entitiesManager.setProcessingNode(processingNode);
	executionEngine.setProcessingNode(processingNode);
	monitoringManager.setProcessingNode(processingNode);
	
	try {
	    processingNode.start();
	} catch (Exception e) {
	    throw new ContextAwareException("FAILED_TO_START_DISTRIBUTED_NODE",e);
	}
    }
    
    public SubmitTaskResponse processAsynchronousTask(SimpleTask task, ProcessingResponseReceiver receiver) {
	return processingNode.submitTask(task, receiver);
    }
    
    public TaskProcessingResponse processSynchronousTask(SimpleTask task) throws ContextAwareException {
	return synchronousProcessingNodeClient.processTask(task);
    }
    

    /**
     * @return the entitiesManager
     */
    public EntitiesManagerProxy getEntitiesManager() {
        return entitiesManager;
    }

    /**
     * @param entitiesManager the entitiesManager to set
     */
    public void setEntitiesManager(EntitiesManagerProxy entitiesManager) {
        this.entitiesManager = entitiesManager;
    }

    /**
     * @return the executionEngine
     */
    public ExecutionEngineProxy getExecutionEngine() {
        return executionEngine;
    }

    /**
     * @param executionEngine the executionEngine to set
     */
    public void setExecutionEngine(ExecutionEngineProxy executionEngine) {
        this.executionEngine = executionEngine;
    }

    /**
     * @return the repositoryDir
     */
    public String getRepositoryDir() {
        return repositoryDir;
    }

    /**
     * @return the entryPointFile
     */
    public String getEntryPointFile() {
        return entryPointFile;
    }

    /**
     * @param repositoryDir the repositoryDir to set
     */
    public void setRepositoryDir(String repositoryDir) {
        this.repositoryDir = repositoryDir;
    }

    /**
     * @param entryPointFile the entryPointFile to set
     */
    public void setEntryPointFile(String entryPointFile) {
        this.entryPointFile = entryPointFile;
    }

    /**
     * @return the monitoringManager
     */
    public MonitoringManagerProxy getMonitoringManager() {
        return monitoringManager;
    }

    /**
     * @param monitoringManager the monitoringManager to set
     */
    public void setMonitoringManager(MonitoringManagerProxy monitoringManager) {
        this.monitoringManager = monitoringManager;
    }
    
    public String getProcessingNodeId() {
	return processingNode.getLocalNodeAddress().toString();
    }
}
