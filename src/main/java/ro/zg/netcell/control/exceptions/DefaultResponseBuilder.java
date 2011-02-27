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

import java.util.List;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.netcell.control.CommandResponse;
import ro.zg.util.data.GenericNameValue;

public class DefaultResponseBuilder extends ResponseBuilder {

    @Override
    public CommandResponse buildResponse(ContextAwareException e) {
	CommandResponse res = new CommandResponse();
	res.setResponseCode(getResponseCode());
	List<String> paramNames = getParameterNames();
	ExceptionContext context = e.getExceptionContext();
	if (paramNames != null && context != null) {
	    for (int i = 0; i < paramNames.size(); i++) {
		String name = paramNames.get(i);
		res.put(new GenericNameValue(name,context.get(name).getValue().toString()));
	    }
	}
	return res;
    }

}
