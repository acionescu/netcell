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
package net.segoia.netcell.entities;

import java.util.Map;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;
import net.segoia.netcell.entities.GenericEntity;
import net.segoia.netcell.vo.WorkflowContext;
import net.segoia.netcell.vo.configurations.ComponentExitPoint;
import net.segoia.netcell.vo.configurations.ComponentExitPointsMapping;
import net.segoia.netcell.vo.configurations.WorkFlowComponentRuntimeConfiguration;
import net.segoia.util.data.GenericNameValue;
import net.segoia.util.data.GenericNameValueContext;

public class WorkFlow extends GenericEntity<GenericNameValueContext> {

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	String entryPointId = (String) input.getValue("entryPointId");
	if(entryPointId == null) {
	    entryPointId = (String)input.getValue("defaultEntryPointId");
	}
	String errorHandlerId = (String) input.getValue("errorHandlerId");
	Map<String, WorkFlowComponentRuntimeConfiguration> components = (Map<String, WorkFlowComponentRuntimeConfiguration>) input
		.getValue("components");
	GenericNameValueContext inputContext = (GenericNameValueContext) input.getValue("inputContext");
	if(inputContext == null) {
	    inputContext = (GenericNameValueContext) input.getValue("fullInputContext");
	}
	WorkflowContext wfContext = new WorkflowContext();
	if(input.getValue("isAsyncContext") != null) {
	    wfContext.setAsyncContext((Boolean)input.getValue("isAsyncContext"));
	}
//	System.out.println("stack: "+input.getExecutionStack());
	wfContext.setFlowId(input.getExecutionStack().peekLast());
	wfContext.setNextCompId(entryPointId);
	wfContext.setErrorHandlerId(errorHandlerId);
	wfContext.setComponents(components);
	wfContext.setParametersContext(inputContext);
	GenericNameValueContext output = process(wfContext);

	// GenericNameValueContext output = process(inputContext, entryPointId, components, errorHandlerId);
	GenericNameValueContext response = getFlowOutput(output, input);
	return response;
    }

    private GenericNameValueContext process(GenericNameValueContext input, String entryPointId,
	    Map<String, WorkFlowComponentRuntimeConfiguration> components, String errorHandlerId) throws Exception {
	String currentCompId = entryPointId;
	GenericNameValueContext currentContext = input;
	String exitPointMapping = null;
	do {
	    WorkFlowComponentRuntimeConfiguration compConfig = components.get(currentCompId);
	    GenericEntity<GenericNameValueContext> currentComp = compConfig.getComponent();
	    ComponentExitPointsMapping mapping = compConfig.getMapping();
	    GenericNameValueContext currentCompContext = null;
	    String nextCompId = null;
	    try {
		// System.out.println(currentContext+"->"+currentCompId);
		currentCompContext = currentComp.execute(currentContext);
		mapComponentOutput(currentCompContext, currentContext, compConfig);
		/* if no mapping defined for this component exit */
		if (mapping == null) {
		    break;
		}
		nextCompId = mapping.getNextComponentId();
		exitPointMapping = mapping.getExitLabel();
		if (nextCompId == null && exitPointMapping == null) {
		    ComponentExitPoint exitPoint = mapping.getExitPoint(currentCompContext);
		    if (exitPoint == null) {
			ExceptionContext ec = new ExceptionContext();
			ec.put(new GenericNameValue("componentId", currentCompId));
			throw new ContextAwareException("UNMAPPED_COMPONENT", ec);
		    }
		    if (exitPoint.isExitPoint()) {
			exitPointMapping = exitPoint.getExitPointMapping();
		    } else {
			nextCompId = exitPoint.getNextComponentId();
		    }
		}
	    } catch (Exception e) {
		ComponentExitPoint errorMapping = compConfig.getErrorMapping();
		if (errorMapping != null) {
		    nextCompId = errorMapping.getNextComponentId();
		    exitPointMapping = errorMapping.getExitPointMapping();
		} else {
		    ContextAwareException cae = null;
		    if (e instanceof ContextAwareException) {
			cae = (ContextAwareException) e;
		    } else {
			ExceptionContext ec = new ExceptionContext();
			ec.put(new GenericNameValue("componentId", currentCompId));
			// ec.put(new GenericNameValue("currentContext", currentContext));
			cae = new ContextAwareException("WORKFLOW_EXECUTION_ERROR", e, ec);
		    }
		    String prevErrorComp = null;

		    if (cae.getExceptionContext() == null) {
			cae = new ContextAwareException(cae.getType(), cae.getCause(), new ExceptionContext());
		    }

		    prevErrorComp = (String) cae.getExceptionContext().getValue("componentId");
		    if (prevErrorComp == null) {
			prevErrorComp = currentCompId;
		    } else {
			prevErrorComp = currentCompId + "=>" + prevErrorComp;
		    }
		    cae.getExceptionContext().put("componentId", prevErrorComp);
		    throw cae;
		}
	    }
	    currentCompId = nextCompId;
	} while (currentCompId != null);
	if (exitPointMapping != null) {
	    currentContext.put(new GenericNameValue("exit", exitPointMapping));
	}
	return currentContext;
    }

    private GenericNameValueContext process(WorkflowContext wfContext) throws Exception {
	while (!wfContext.isFinished()) {
	    wfContext.executeNextComponent();
	}
	return wfContext.getResultContext();
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

    private GenericNameValueContext getFlowOutput(GenericNameValueContext source, GenericNameValueContext input) {
	/* in case it returns the value of a single parameter */
	String outputPrmName = (String) input.getValue("outputParameterName");
	if (outputPrmName != null) {
	    return (GenericNameValueContext) source.getValue(outputPrmName);
	}
	return source;
    }

}
