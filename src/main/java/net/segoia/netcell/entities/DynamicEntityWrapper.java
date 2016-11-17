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

import java.io.Serializable;
import java.util.Map;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;
import net.segoia.netcell.entities.GenericEntity;
import net.segoia.netcell.vo.configurations.ExceptionMapping;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.data.GenericNameValueContextUtil;

public class DynamicEntityWrapper<O> extends GenericEntity<O> implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -564798093340781210L;
    private DynamicEntityWrapperConfiguration<O> config;
    public static final String GLOBAL_CONTEXT = "_globalContext";

    public O execute(GenericNameValueContext input) throws Exception {
	GenericNameValueContext globalContext = input;
	GenericNameValueContext staticContext = config.getStaticContext();
	Map<String, String> dynamicInput = config.getDynamicParameters();
	GenericEntity<O> executor = config.getExecutor();
	GenericNameValueContext localContext = getLocalContext(globalContext, staticContext, dynamicInput);
	if (config.getUserConfig() != null) {
	    input.putMap(config.getUserConfig().getValuesMap());
	}

	String originalInputName = config.getOriginalInputName();
	if (originalInputName != null) {
	    localContext.put(originalInputName, input);
	}
	/* in case no key for original context was passed, still add it under _globalContext key */
	else {
	    GenericNameValueContext gContext = null;
	    if (executor instanceof DynamicEntityWrapper) {
		input.remove(GLOBAL_CONTEXT);
	    } else {
		gContext = (GenericNameValueContext) input.getValue(GLOBAL_CONTEXT);
	    }
	    if (gContext == null) {
		gContext = input;
	    }
	    localContext.put(GLOBAL_CONTEXT, gContext);
	}
	/* set on the context the id of the execution entity */
	String currentEntityId = getId();
	try {

	    // currentEntityId = (currentEntityId == null) ? "?" : currentEntityId;
	    if (currentEntityId == null) {
		currentEntityId = executor.getId();
	    }

	    if (input.getExecutionStack() != null) {
		localContext.setExecutionStack(input.getExecutionStack());
	    }
	    localContext.pushToExecutionStack(currentEntityId);
	    /* actual execution */
	    O result = executor.execute(localContext);

	    /* check if the last execution entity has the same id */
	    checkExecutionStackConsistency(currentEntityId, localContext.popExecutionStack());

	    return result;
	} catch (Exception e) {
	    /* check if the last execution entity has the same id */
	    checkExecutionStackConsistency(currentEntityId, localContext.popExecutionStack());
	    /* map the error */
	    Map<String, ExceptionMapping> exceptionMappgins = config.getExceptionMappings();
	    if (exceptionMappgins != null) {
		ExceptionMapping ep = exceptionMappgins.get(e.getClass().getName());
		if (ep != null) {
		    throw new ContextAwareException(ep.getMappingName(), e);
		}
	    }
	    throw new ContextAwareException("GENERIC_EXECUTION_ERROR", e);
	}
    }

    /* check if the last execution entity has the same id */
    private void checkExecutionStackConsistency(String currentEntityId, String lastExecutionEntity)
	    throws ContextAwareException {
	if (!currentEntityId.equals(lastExecutionEntity)) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put("expected", currentEntityId);
	    ec.put("actual", lastExecutionEntity);
	    throw new ContextAwareException("EXECUTION_STACK_ERROR", ec);
	}
    }

    /**
     * Creates the local component context Adds the static parameters Extracts the dynamic parameters from the global
     * context and sets them on the local context
     * 
     * @param globalContext
     * @param staticContext
     * @param dynamicParameters
     * @return
     */
    private GenericNameValueContext getLocalContext(GenericNameValueContext globalContext,
	    GenericNameValueContext staticContext, Map<String, String> dynamicParameters) {
	GenericNameValueContext localContext = new GenericNameValueContext();
	if (staticContext != null) {
	    localContext.putAll(staticContext.getParameters());
	}
	if (dynamicParameters != null) {
	    for (Map.Entry<String, String> entry : dynamicParameters.entrySet()) {
		String key = entry.getKey();
		String mappedParam = entry.getValue();
		// GenericNameValue contextParam = (GenericNameValue) globalContext.get(value);
		// if (contextParam != null) {
		// localContext.put(key, contextParam.getValue());
		// }
		Object value = GenericNameValueContextUtil.getValueFromContext(globalContext, mappedParam);
		localContext.put(key, value);

	    }
	}
	return localContext;
    }

    public DynamicEntityWrapperConfiguration<O> getConfig() {
	return config;
    }

    public void setConfig(DynamicEntityWrapperConfiguration<O> config) {
	this.config = config;
    }

    public int getEntityType() {
	int type = config.getEntityType();
	if (type != 0) {
	    return type;
	}
	return config.getExecutor().getEntityType();
    }
}
