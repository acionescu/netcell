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
package ro.zg.netcell.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.netcell.async.AsyncCallManager;
import ro.zg.netcell.async.AsyncResponseHandler;
import ro.zg.netcell.constants.ComponentTypes;
import ro.zg.netcell.control.NodeLoader;
import ro.zg.netcell.entities.GenericEntity;
import ro.zg.netcell.vo.configurations.ComponentExitPoint;
import ro.zg.netcell.vo.configurations.ComponentExitPointsMapping;
import ro.zg.netcell.vo.configurations.WorkFlowComponentRuntimeConfiguration;
import ro.zg.util.data.GenericNameValue;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.NameValue;
import ro.zg.util.data.ObjectsUtil;
import ro.zg.util.logging.MasterLogManager;

public class WorkflowContext implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2416302856443873165L;
    private String flowId;
    private String nextCompId;
    private transient String errorHandlerId;
    private transient Map<String, WorkFlowComponentRuntimeConfiguration> components;
    private GenericNameValueContext parametersContext;
    private boolean asyncContext = false;
    private boolean retry = false;
    private transient List<AsyncResponseHandler> childCalls = new ArrayList<AsyncResponseHandler>();

    private transient String currentComponentId;
    private transient GenericNameValueContext currentComponentContext;
    private transient WorkFlowComponentRuntimeConfiguration currentComponentConfig;
    private transient GenericEntity<GenericNameValueContext> currentComponent;
    private String exitPoint = "default-exit";

    public void executeNextComponent() throws Exception {
	currentComponentId = nextCompId;
	currentComponentConfig = components.get(currentComponentId);
	try {
	    executeComponent(currentComponentConfig.getComponent());
	} catch (Exception e) {
	    handleException(e);
	}

    }

    private void executeComponent(GenericEntity<GenericNameValueContext> currentComp) throws Exception {
//	System.out.println(currentComponentId + " <-- " + parametersContext);
	switch (currentComp.getEntityType()) {
	case ComponentTypes.ASYNC_CALL_COMPONENT:
	    executeAsyncCall(currentComp);
	    break;
	case ComponentTypes.ASYNC_JOIN_COMPONENT:
	    executeAsyncJoin(currentComp);
	    break;
	default:
	    executeNormalComponent(currentComp);
	    break;
	}
    }

    private void executeAsyncJoin(GenericEntity<GenericNameValueContext> currentComp) throws Exception {
	List<GenericNameValueContext> childResponses = new ArrayList<GenericNameValueContext>();
	/* wait for the child calls to finish */
	for (AsyncResponseHandler arh : childCalls) {
	    arh.waitToFinish();
	    if (!arh.isSucessful()) {
		if(arh.getError() instanceof ContextAwareException) {
		    ContextAwareException e = (ContextAwareException)arh.getError();
		    /* execute it synchronous */
		    if(e.getType().equals("NO_RESOURCES_AVAILABLE")) {
			WorkflowContext input = arh.getInput();
			input.setRetry(true);
			childResponses.add(NodeLoader.getExecutionEngineInstance().execute(input));
			continue;
		    }
		}
		throw new ContextAwareException("ASYNC_CALL_ERROR", arh.getError());
	    }
	    childResponses.add(arh.getResponse());
	}
	if (childResponses.size() > 0) {
	    mergeChildResponsesWithLocalContext(childResponses);
	}
	/*
	 * now we're done with the children contexts, further, decide if this is a context spawned after an async call
	 * in which case we return, or if this is a main context created as the result of an execute command in which
	 * case we continue the flow
	 */
	if (!isAsyncContext()) {
	    evaluateMapping(currentComponentConfig.getMapping());
	} else {/* terminate the execution, and return to the main context */
	    nextCompId = null;
	}
    }

    private void mergeChildResponsesWithLocalContext(List<GenericNameValueContext> childResponses) {
	/* the last response has the biggest priority, it will overwrite all the others */
	List<GenericNameValueContext> modified = new ArrayList<GenericNameValueContext>();
	/* compare each response with the original context and determine the modified params */
	for (GenericNameValueContext resp : childResponses) {
	    GenericNameValueContext m = getModifiedParams(parametersContext, resp);
	    if(m.size() > 0) {
		modified.add(m);
	    }
	}
	/* copy the modified params for each response onto the current context */
	for(GenericNameValueContext m : modified) {
	    m.copyTo(parametersContext);
	}
    }
    
    private GenericNameValueContext getModifiedParams(GenericNameValueContext orig, GenericNameValueContext response) {
	GenericNameValueContext modified = new GenericNameValueContext();
	for(NameValue p : response.getParameters().values()) {
	    if(!p.equals(orig.get(p.getName()))) {
		modified.put(p);
	    }
	}
	return modified;
    }

    private void executeAsyncCall(GenericEntity<GenericNameValueContext> currentComp) throws Exception {
	for (Map.Entry<Object, ComponentExitPoint> entry : currentComponentConfig.getMapping().getFixedMappings()
		.entrySet()) {
	    ComponentExitPoint cp = entry.getValue();
	    if (entry.getKey().equals("MAIN")) {
		nextCompId = cp.getNextComponentId();
	    } else {
		WorkflowContext newContext = (WorkflowContext) ObjectsUtil.copy(this);
		newContext.setNextCompId(cp.getNextComponentId());
		newContext.setAsyncContext(true);
		AsyncResponseHandler rsh = AsyncCallManager.getInstance().execute(newContext);
		childCalls.add(rsh);
	    }
	}
    }

    private void executeNormalComponent(GenericEntity<GenericNameValueContext> currentComp) throws Exception {
	currentComponentContext = currentComp.execute(parametersContext);
	mapComponentOutput(currentComponentContext, parametersContext, currentComponentConfig);
	evaluateMapping(currentComponentConfig.getMapping());
    }

    private void handleException(Exception e) throws ContextAwareException {
	ComponentExitPoint errorMapping = currentComponentConfig.getErrorMapping();

	ContextAwareException cae = null;
	if (e instanceof ContextAwareException) {
	    cae = (ContextAwareException) e;
	} else {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("componentId", currentComponentId));
	    // ec.put(new GenericNameValue("currentContext", currentContext));
	    cae = new ContextAwareException("WORKFLOW_EXECUTION_ERROR", e, ec);
	}
	String prevErrorComp = null;

	if (cae.getExceptionContext() == null) {
	    cae = new ContextAwareException(cae.getType(), cae.getCause(), new ExceptionContext());
	}

	prevErrorComp = (String) cae.getExceptionContext().getValue("componentId");
	if (prevErrorComp == null) {
	    prevErrorComp = currentComponentId;
	} else {
	    prevErrorComp = currentComponentId + "=>" + prevErrorComp;
	}
	cae.getExceptionContext().put("componentId", prevErrorComp);
//	cae.getExceptionContext().put("componentContext",currentComponentContext);
//	MasterLogManager.getLogger(flowId).debug("parametersContext:"+parametersContext);
	
	if (errorMapping != null) {
	    nextCompId = errorMapping.getNextComponentId();
	    exitPoint = errorMapping.getExitPointMapping();
	    if(nextCompId != null || exitPoint != null) {
		/* log this error */
//		MasterLogManager.getLogger(flowId).error("Error:", cae);
		
		return;
	    }
	}
	
	throw cae;
    }

    public GenericNameValueContext getResultContext() {
	/*
	 * the exit point is going to be set on the output context only if this is a main context
	 */
	if (!isAsyncContext()) {
	    parametersContext.put("exit", exitPoint);
	}
	return parametersContext;
    }

    /**
     * Evaluates the mapping of the last executed component and extracts the id of the next component to be executed or
     * the exit point mapping
     * 
     * @param mapping
     * @throws ContextAwareException
     */
    private void evaluateMapping(ComponentExitPointsMapping mapping) throws ContextAwareException {
	if (mapping == null) {
	    nextCompId = null;
	    return;
	}
	nextCompId = mapping.getNextComponentId();
	exitPoint = mapping.getExitLabel();
	if (nextCompId == null && exitPoint == null) {
	    ComponentExitPoint exitPoint = mapping.getExitPoint(currentComponentContext);
	    if (exitPoint == null) {
		ExceptionContext ec = new ExceptionContext();
		ec.put(new GenericNameValue("componentId", currentComponentId));
		throw new ContextAwareException("UNMAPPED_COMPONENT", ec);
	    }
	    if (exitPoint.isExitPoint()) {
		this.exitPoint = exitPoint.getExitPointMapping();
	    } else {
		nextCompId = exitPoint.getNextComponentId();
	    }
	}
    }

    public boolean isFinished() {
	return (nextCompId == null);
    }

    private void mapComponentOutput(GenericNameValueContext source, GenericNameValueContext dest,
	    WorkFlowComponentRuntimeConfiguration compConfig) {
	if (compConfig.getOutputParamName() != null) {
	    dest.put(compConfig.getOutputParamName(), source);
	} else {
	    mapOutputParameters(source, dest, compConfig.getOutputParamsMappings());
	}

    }

    private void mapOutputParameters(GenericNameValueContext source, GenericNameValueContext dest,
	    Map<String, String> mappings) {
	if (mappings == null) {
	    return;
	}
	for (Map.Entry<String, String> entry : mappings.entrySet()) {
	    String key = entry.getKey();
	    String value = entry.getValue();
	    dest.put(new GenericNameValue(value, source.getValue(key)));
	}
    }

    /**
     * @return the flowId
     */
    public String getFlowId() {
	return flowId;
    }

    /**
     * @return the nextCompId
     */
    public String getNextCompId() {
	return nextCompId;
    }

    /**
     * @return the errorHandlerId
     */
    public String getErrorHandlerId() {
	return errorHandlerId;
    }

    /**
     * @return the components
     */
    public Map<String, WorkFlowComponentRuntimeConfiguration> getComponents() {
	return components;
    }

    /**
     * @return the parametersContext
     */
    public GenericNameValueContext getParametersContext() {
	return parametersContext;
    }

    /**
     * @param flowId
     *            the flowId to set
     */
    public void setFlowId(String flowId) {
	this.flowId = flowId;
    }

    /**
     * @param nextCompId
     *            the nextCompId to set
     */
    public void setNextCompId(String nextCompId) {
	this.nextCompId = nextCompId;
    }

    /**
     * @param errorHandlerId
     *            the errorHandlerId to set
     */
    public void setErrorHandlerId(String errorHandlerId) {
	this.errorHandlerId = errorHandlerId;
    }

    /**
     * @param components
     *            the components to set
     */
    public void setComponents(Map<String, WorkFlowComponentRuntimeConfiguration> components) {
	this.components = components;
    }

    /**
     * @param parametersContext
     *            the parametersContext to set
     */
    public void setParametersContext(GenericNameValueContext parametersContext) {
	this.parametersContext = parametersContext;
    }

    /**
     * @return the asyncContext
     */
    public boolean isAsyncContext() {
	return asyncContext;
    }

    /**
     * @param asyncContext
     *            the asyncContext to set
     */
    public void setAsyncContext(boolean asyncContext) {
	this.asyncContext = asyncContext;
    }

    /**
     * @return the retry
     */
    public boolean isRetry() {
        return retry;
    }

    /**
     * @param retry the retry to set
     */
    public void setRetry(boolean retry) {
        this.retry = retry;
    }
    
}
