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

import java.util.List;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.netcell.control.CommandResponse;

public abstract class ResponseBuilder {
    private String responseCode;
    private List<String> parameterNames;
    
    public String getResponseCode() {
        return responseCode;
    }
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
    public List<String> getParameterNames() {
        return parameterNames;
    }
    public void setParameterNames(List<String> parameterNames) {
        this.parameterNames = parameterNames;
    }
    
    public abstract CommandResponse buildResponse(ContextAwareException e);
}
