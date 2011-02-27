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
package ro.zg.netcell.control.exceptions;

import java.util.Map;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.netcell.control.CommandResponse;

public class CommandResponsesManager {
    /**
     * Holds the mappings between Exceptions and responses descriptions </br> key - full name of the exception </br>
     * value - the ResponseDescription - code, and parameter names
     */
    private Map<String, ResponseBuilder> exceptionsResponseCodesMap;

    private String internalErrorResponseCode;

    /**
     * If this is not a {@link ContextAwareException} assume it's an internal error
     */
    public CommandResponse getResponseForException(Exception e) {
	if( e instanceof ContextAwareException){
	    return getSpecificResponseForException((ContextAwareException)e);
	}
	return getInternalErrorResponse();
    }

    public CommandResponse getSpecificResponseForException(ContextAwareException e) {
	ResponseBuilder rd = exceptionsResponseCodesMap.get(e.getType());
	if (rd != null) {
	   return rd.buildResponse(e);
	} else {
	    return getInternalErrorResponse();
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

    public void setExceptionsResponseCodesMap(Map<String, ResponseBuilder> exceptionsResponseCodesMap) {
        this.exceptionsResponseCodesMap = exceptionsResponseCodesMap;
    }

    public void setInternalErrorResponseCode(String internalErrorResponseCode) {
        this.internalErrorResponseCode = internalErrorResponseCode;
    }
}
