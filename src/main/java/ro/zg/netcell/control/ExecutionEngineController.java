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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ro.zg.cfgengine.core.configuration.ConfigurationManager;
import ro.zg.cfgengine.util.PackageCfgLoader;
import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.netcell.entities.FlowCallerEntity;
import ro.zg.netcell.entities.GenericEntity;
import ro.zg.netcell.security.AccessRule;
import ro.zg.netcell.vo.WorkflowContext;
import ro.zg.util.data.GenericNameValue;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

public class ExecutionEngineController implements ExecutionEngineContract {
    private static Logger logger = MasterLogManager
	    .getLogger("ExecutionEngineController");
    private String engineHandlerFile;
    private String engineConfigFile;
    private ClassLoader resourcesLoader;
    private ResourcesManager resourcesManager;
    private Map<String, GenericEntity<GenericNameValueContext>> entities;
    private ConfigurationManager cfgManager;
    private int pendingTasks;
    private Map<String, AccessRule> accessRulesByPath;

    public ExecutionEngineController(ConfigurationManager cfgManager) {
	this.cfgManager = cfgManager;
	// entities = (Map) cfgManager.getObjectsByTagName("entity");
	// FlowCallerEntity fce = (FlowCallerEntity)
	// cfgManager.getObjectById("flowCaller");
	// if (fce != null) {
	// fce.setEngineController(this);
	// }
	// load();
    }

    public ExecutionEngineController() {

    }

    public void init() throws Exception {
	engineHandlerFile = resourcesManager
		.getResourceFullPath("ENGINE_HANDLER_FILE");
	engineConfigFile = resourcesManager
		.getResourceFullPath("ENGINE_CONFIG_FILE");
	cfgManager = PackageCfgLoader.getInstance().load(engineHandlerFile,
		engineConfigFile, resourcesLoader);
	load();
    }

    private void load() {
	entities = (Map) cfgManager.getObjectsByTagName("entity");
	loadAccessRules();
	initSpecialEntities();
    }

    public boolean reload() throws Exception {
	try {
	    init();
	    return true;
	} catch (Exception e) {
	    throw new ContextAwareException("RELOAD_FAILED", e);
	}
    }

    private void loadAccessRules() {
	Map<String, ?> rulesMap = cfgManager.getObjectsByTagName("access-rule");
	accessRulesByPath = new LinkedHashMap<String, AccessRule>();
	for (Map.Entry<String, ?> e : rulesMap.entrySet()) {
	    AccessRule rule = (AccessRule) e.getValue();
	    accessRulesByPath.put(rule.getTargetPathRegex(), rule);
	}
	logger.info("Loaded access rules: " + accessRulesByPath);
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

    /**
     * Check if the access rules that match the flowId are satisfied
     * 
     * @param flowId
     * @param input
     * @return
     * @throws ContextAwareException
     *             with message ACCESS_DENIED if at least one rule is not
     *             satisfied <br/>
     *             with message ACCESS_RULE_CHECK_FAILED if the execution of a
     *             validating flow fails
     */
    private void checkIfExecutionAllowed(String flowId,
	    GenericNameValueContext input) throws ContextAwareException {
	/* get the access rules that match the flowId */
	List<AccessRule> matchingRules = getMatchingAccessRules(flowId);

	if (matchingRules.size() > 0) {
	    /*
	     * create the input for the rule, should contain all the parameters
	     * of the original input plus the parameter "flowId" containing the
	     * name of the flow that is being checked
	     */
	    GenericNameValueContext ruleInput = new GenericNameValueContext();
	    input.copyTo(ruleInput);
	    ruleInput.put("flowId", flowId);
	    /* iterate over the rules using the same rule input */
	    for (AccessRule ar : matchingRules) {
		GenericNameValueContext response = null;
		try {
		    response = executeEntity(ar.getValidationFlowId(),
			    ruleInput);
		} catch (Exception e) {
		    ExceptionContext ec = new ExceptionContext();
		    ec.put(new GenericNameValue("validatingFlowId", ar
			    .getValidationFlowId()));
		    throw new ContextAwareException("ACCESS_RULE_CHECK_FAILED",
			    e, ec);
		}
		/* if the exit parameter is false, the access will be denied */
		if ("false".equals(response.getValue("exit"))) {
		    logger.warn("Execution denied to flow '" + flowId
			    + "' due to unsatisfied access rule " + ar);

		    ExceptionContext ec = new ExceptionContext();
		    ec.put(new GenericNameValue("requiredFlowId", flowId));
		    ec.put(new GenericNameValue("unsatisfiedAccessRuleId", ar
			    .getId()));
		    throw new ContextAwareException("ACCESS_DENIED", ec);
		}
	    }
	}
    }

    private List<AccessRule> getMatchingAccessRules(String flowId) {
	List<AccessRule> rulesList = new ArrayList<AccessRule>();
	/*
	 * iterate over the rules and get the ones for which targetPathRegex
	 * match the flowId
	 */
	for (AccessRule ar : accessRulesByPath.values()) {
	    if (flowId.matches(ar.getTargetPathRegex())) {
		rulesList.add(ar);
	    }
	}
	return rulesList;
    }

    private GenericNameValueContext executeEntity(String entityId,
	    GenericNameValueContext input) throws Exception {
	if (entityId == null) {
	    throw new ContextAwareException("FLOW_ID_EXPECTED");
	}
	GenericEntity<GenericNameValueContext> entity = entities.get(entityId);
	if (entity == null) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("flowId", entityId));
	    throw new ContextAwareException("UNKNOWN_FLOW_ID", ec);
	}
	pendingTasks++;
	try {
	    GenericNameValueContext result = entity.execute(input);
	    return result;
	} finally {
	    pendingTasks--;
	}
    }

    private GenericNameValueContext checkAccessAndExecuteEntity(
	    String entityId, GenericNameValueContext input) throws Exception {
	checkIfExecutionAllowed(entityId, input);
	return executeEntity(entityId, input);
    }

    public GenericNameValueContext execute(GenericNameValueContext input)
	    throws Exception {

	return executeEntity(input);

    }

    private GenericNameValueContext executeEntity(GenericNameValueContext input)
	    throws Exception {
	GenericNameValue flow = (GenericNameValue) input.remove("fid");
	if (flow == null) {
	    throw new ContextAwareException("FLOW_ID_EXPECTED");
	}
	String flowId = (String) flow.getValue();

	return checkAccessAndExecuteEntity(flowId, input);

    }

    public GenericNameValueContext execute(WorkflowContext wfContext)
	    throws Exception {
	// if (pendingTasks > 20 && wfContext.isAsyncContext() &&
	// !wfContext.isRetry()) {
	// throw new ContextAwareException("NO_RESOURCES_AVAILABLE");
	// }
	String flowId = wfContext.getFlowId();
	GenericNameValueContext input = new GenericNameValueContext();
	input.put("origContext", wfContext.getParametersContext());
	input.put("entryPointId", wfContext.getNextCompId());
	input.put("isAsyncContext", wfContext.isAsyncContext());

	return checkAccessAndExecuteEntity(flowId, input);
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
    public void setEntities(
	    Map<String, GenericEntity<GenericNameValueContext>> entities) {
	this.entities = entities;
	initSpecialEntities();
    }

}
