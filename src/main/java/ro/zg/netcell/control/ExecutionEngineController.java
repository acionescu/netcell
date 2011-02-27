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

import java.util.Map;

import ro.zg.cfgengine.core.configuration.ConfigurationManager;
import ro.zg.cfgengine.util.PackageCfgLoader;
import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.netcell.entities.FlowCallerEntity;
import ro.zg.netcell.entities.GenericEntity;
import ro.zg.netcell.vo.WorkflowContext;
import ro.zg.util.data.GenericNameValue;
import ro.zg.util.data.GenericNameValueContext;

public class ExecutionEngineController implements ExecutionEngineContract {
    private String engineHandlerFile;
    private String engineConfigFile;
    private ClassLoader resourcesLoader;
    private ResourcesManager resourcesManager;
    private Map<String, GenericEntity<GenericNameValueContext>> entities;
    private ConfigurationManager cfgManager;
    private int pendingTasks;

    public ExecutionEngineController(ConfigurationManager cfgManager) {
	this.cfgManager = cfgManager;
	entities = (Map) cfgManager.getObjectsByTagName("entity");
	FlowCallerEntity fce = (FlowCallerEntity) cfgManager.getObjectById("flowCaller");
	if (fce != null) {
	    fce.setEngineController(this);
	}
    }

    public ExecutionEngineController() {

    }

    public void init() throws Exception {
	engineHandlerFile = resourcesManager.getResourceFullPath("ENGINE_HANDLER_FILE");
	engineConfigFile = resourcesManager.getResourceFullPath("ENGINE_CONFIG_FILE");
	cfgManager = PackageCfgLoader.getInstance().load(engineHandlerFile, engineConfigFile, resourcesLoader);
	load();
    }

    private void load() {
	entities = (Map) cfgManager.getObjectsByTagName("entity");
	FlowCallerEntity fce = (FlowCallerEntity) cfgManager.getObjectById("flowCaller");
	if (fce != null) {
	    fce.setEngineController(this);
	}
    }

    public boolean reload() throws Exception {
	try {
	    init();
	    return true;
	} catch (Exception e) {
	    throw new ContextAwareException("RELOAD_FAILED", e);
	}
    }

    private void initSpecialEntities() {
	FlowCallerEntity fce = (FlowCallerEntity) entities.get("flowCaller");
	if (fce != null) {
	    fce.setEngineController(this);
	}
    }

    public boolean containsEntityWithId(String id) {
	return cfgManager.containsObjectWithId(id);
    }

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	pendingTasks++;
	// String log = input+" => ";
	GenericNameValueContext response = executeEntity(input);
	pendingTasks--;
	// log+=response;
	// System.out.println(log);
	return response;
    }

    private GenericNameValueContext executeEntity(GenericNameValueContext input) throws Exception {
	GenericNameValue flow = (GenericNameValue) input.remove("fid");
	if (flow == null) {
	    throw new ContextAwareException("FLOW_ID_EXPECTED");
	}
	String flowId = (String) flow.getValue();
	GenericEntity<GenericNameValueContext> entity = entities.get(flowId);
	if (entity == null) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("flowId", flowId));
	    throw new ContextAwareException("UNKNOWN_FLOW_ID", ec);
	}
	return entity.execute(input);
    }

    public GenericNameValueContext execute(WorkflowContext wfContext) throws Exception {
	// if (pendingTasks > 20 && wfContext.isAsyncContext() && !wfContext.isRetry()) {
	// throw new ContextAwareException("NO_RESOURCES_AVAILABLE");
	// }
	String flowId = wfContext.getFlowId();
	if (flowId == null) {
	    throw new ContextAwareException("FLOW_ID_EXPECTED");
	}
	GenericEntity<GenericNameValueContext> entity = entities.get(flowId);
	if (entity == null) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("flowId", flowId));
	    throw new ContextAwareException("UNKNOWN_FLOW_ID", ec);
	}
	GenericNameValueContext input = new GenericNameValueContext();
	input.put("origContext", wfContext.getParametersContext());
	input.put("entryPointId", wfContext.getNextCompId());
	input.put("isAsyncContext", wfContext.isAsyncContext());
	pendingTasks++;
	try {
	    GenericNameValueContext result = entity.execute(input);
	    return result;
	} finally {
	    pendingTasks--;
	}
    }

    public void destroy() throws Exception {
	cfgManager.destroy();
    }

    public int getPendingTasks() {
	return pendingTasks;
    }

    /**
     * @return the resourcesManager
     */
    public ResourcesManager getResourcesManager() {
	return resourcesManager;
    }

    /**
     * @param resourcesManager
     *            the resourcesManager to set
     */
    public void setResourcesManager(ResourcesManager resourcesManager) {
	this.resourcesManager = resourcesManager;
    }

    /**
     * @return the resourcesLoader
     */
    public ClassLoader getResourcesLoader() {
	return resourcesLoader;
    }

    /**
     * @param resourcesLoader
     *            the resourcesLoader to set
     */
    public void setResourcesLoader(ClassLoader resourcesLoader) {
	this.resourcesLoader = resourcesLoader;
    }

    /**
     * @return the entities
     */
    public Map<String, GenericEntity<GenericNameValueContext>> getEntities() {
	return entities;
    }

    /**
     * @param entities
     *            the entities to set
     */
    public void setEntities(Map<String, GenericEntity<GenericNameValueContext>> entities) {
	this.entities = entities;
	initSpecialEntities();
    }

}
