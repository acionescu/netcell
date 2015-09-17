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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;
import net.segoia.netcell.entities.GenericEntity;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.data.GenericNameValueContextUtil;
import net.segoia.util.data.reflection.ReflectionUtility;

/**
 * Wrapps a call to a java method on an object, through reflection It returns a {@link GenericNameValueContext} with the
 * specified parameters
 * 
 * @author adi
 * 
 */
public class MethodCaller extends GenericEntity<Object> {
    /**
     * Expects: argsContext {@link GenericNameValueContext} : context where the arguments can be found resource Object :
     * the java object on which the method will be called paramAsResource : the name of the parameter to be used as a
     * resource method : the name of the method to be called argsOrder : the order of the method arguments argsTypes :
     * the specific types for the arguments(this can be used if the types of the parameters does not match the exact
     * types of the method arguments) simpleResponse : Map<String,String> : in case the method call returns a simple
     * java type, the returned value can be mapped on another value
     */
    public Object execute(GenericNameValueContext input) throws Exception {
	GenericNameValueContext argsContext = (GenericNameValueContext) input.getValue("argsContext");
	Object resource = input.getValue("resource");
	String resourceClass = null;
	if (resource == null) {
	    String paramAsResource = (String) input.getValue("paramAsResource");
	    resource = argsContext.getValue(paramAsResource);
	}
	if (resource == null) {
	    resourceClass = (String) input.getValue("resourceClass");
	}

	String methodName = (String) input.getValue("method");

	List<String> argsOrder = (List<String>) input.getValue("argsOrder");
	Object[] args = null;
	if (argsContext != null && argsOrder != null && argsOrder.size() > 0) {
	    List<Object> argsList = argsContext.getValues(argsOrder);
	    args = argsList.toArray(new Object[0]);

	}
	Object result = null;

	List<String> argsTypes = (List<String>) input.getValue("argsTypes");
	try {
	    if (resource != null) {
		if (argsTypes != null) {
		    String[] typesArray = argsTypes.toArray(new String[0]);
		    convertArgsToTypes(args, typesArray);
		    result = ReflectionUtility.callMethod(resource, methodName, args, typesArray);
		} else {
		    result = ReflectionUtility.callMethod(resource, methodName, args);
		}
	    }
	    else if(resourceClass != null) {
		if (argsTypes != null) {
		    String[] typesArray = argsTypes.toArray(new String[0]);
		    convertArgsToTypes(args, typesArray);
		    result = ReflectionUtility.callStaticMetod(resourceClass, methodName, args, typesArray);
		} else {
		    result = ReflectionUtility.callStaticMethod(resourceClass, methodName, args);
		}
	    }
	    else {
		throw new ContextAwareException("METHOD_CALL_ERROR","No resource or resourceClass specified");
	    }
	} catch (Exception e) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put("methodName", methodName);
	    if (resource != null) {
		ec.put("resource", resource.getClass().getName());
	    }
	    ec.put("args", Arrays.asList(args));
	    if (argsTypes != null) {
		ec.put("argsTypes", argsTypes);
	    }
	    throw new ContextAwareException("METHOD_CALL_ERROR", e, ec);
	}
	String responseMappingName = (String) input.getValue("responseMappingName");
	if (responseMappingName != null) {
	    String convertToKnownType = (String) input.getValue("convertToKnownType");
	    GenericNameValueContext resp = new GenericNameValueContext();
	    if (convertToKnownType != null && "true".equals(convertToKnownType)) {
		resp.put(responseMappingName, GenericNameValueContextUtil.convertToKnownType(result));
	    } else {
		resp.put(responseMappingName, result);
	    }
	    return resp;
	}

	Map<Object, Object> simpleResponseMappings = (Map<Object, Object>) input.getValue("simpleResponseWithMappings");
	if (simpleResponseMappings != null) {
	    return processSimpleResponseWithMappings(result, simpleResponseMappings);
	}
	/* check complex mappings */
	List<Map<Object, Object>> complexResponseMappings = (List) input.getValue("complexResponseMappings");
	if (complexResponseMappings != null) {
	    return processComplexResponse(result, complexResponseMappings);
	}
	return processResponse(result);
    }

    public Object processComplexResponse(Object rawResult, List<Map<Object, Object>> respMappings) throws Exception {
	GenericNameValueContext response = new GenericNameValueContext();
	for (Map map : respMappings) {
	    String methodName = (String) map.get("methodName");
	    String mappingName = (String) map.get("mappingName");
	    Object value = rawResult;
	    if (methodName != null) {
		value = ReflectionUtility.callMethod(rawResult, methodName, new Object[0]);
	    }
	    response.put(mappingName, GenericNameValueContextUtil.convertToKnownType(value));
	}
	return response;
    }

    public Object processSimpleResponseWithMappings(Object rawResult, Map<Object, Object> respMappings)
	    throws ContextAwareException {
	Object res = respMappings.get(rawResult.toString());
	if (res == null) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put("result", rawResult);
	    throw new ContextAwareException("UNKNOWN_RESULT", ec);
	}
	GenericNameValueContext resContext = new GenericNameValueContext();
	resContext.put("exit", res);
	return resContext;
    }

    private Object processResponse(Object rawResult) {
	GenericNameValueContext resContext = new GenericNameValueContext();
	resContext.put("exit", rawResult);
	return resContext;
    }

    private void convertArgsToTypes(Object[] args, String[] types) {
	for (int i = 0; i < args.length; i++) {
	    args[i] = GenericNameValueContextUtil.convertToType(args[i], types[i]);
	}
    }
}
