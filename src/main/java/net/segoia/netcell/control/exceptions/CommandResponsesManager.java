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
package net.segoia.netcell.control.exceptions;

import java.util.Map;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.netcell.control.CommandResponse;

public class CommandResponsesManager {
    /**
     * Holds the mappings between Exceptions and responses descriptions </br>
     * key - full name of the exception </br> value - the ResponseDescription -
     * code, and parameter names
     */
    private Map<String, ResponseBuilder> exceptionsResponseCodesMap;

    private String internalErrorResponseCode;

    /**
     * If this is not a {@link ContextAwareException} assume it's an internal
     * error
     */
    public CommandResponse getResponseForException(Throwable e) {
	if (e == null) {
	    return getInternalErrorResponse();
	}
	/* try to get a response */
	if (e instanceof ContextAwareException) {
	    return getSpecificResponseForException((ContextAwareException) e);
	}
	/* if not ContextAwareException try to get some hint from the cause */
	return getResponseForException(e.getCause());

    }

    public CommandResponse getSpecificResponseForException(
	    ContextAwareException e) {
	ResponseBuilder rd = exceptionsResponseCodesMap.get(e.getType());
	if (rd != null) {
	    return rd.buildResponse(e);
	} else {
	    /* if no recognized message found, try to dig deeper */
	    return getResponseForException(e.getCause());
	}
    }

    private CommandResponse getInternalErrorResponse() {
	CommandResponse res = new CommandResponse();
	res.setResponseCode(internalErrorResponseCode);
	return res;
    }

    public Map<String, ResponseBuilder> getExceptionsResponseCodesMap() {
	return exceptionsResponseCodesMap;
    }

    public String getInternalErrorResponseCode() {
	return internalErrorResponseCode;
    }

    public void setExceptionsResponseCodesMap(
	    Map<String, ResponseBuilder> exceptionsResponseCodesMap) {
	this.exceptionsResponseCodesMap = exceptionsResponseCodesMap;
    }

    public void setInternalErrorResponseCode(String internalErrorResponseCode) {
	this.internalErrorResponseCode = internalErrorResponseCode;
    }
}
