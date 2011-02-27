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
package ro.zg.netcell.engine.core.components;

import java.util.Map;

import ro.zg.util.data.GenericNameValue;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.execution.ExecutionEntity;

public class DynamicParameterMapper implements ExecutionEntity<GenericNameValueContext, GenericNameValueContext> {

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	GenericNameValueContext source = (GenericNameValueContext) input.getValue("source");
	GenericNameValueContext target = (GenericNameValueContext) input.getValue("target");
	Map<String, String> params = (Map<String, String>) input.getValue("params");

	for (Map.Entry<String, String> entry : params.entrySet()) {
	    String key = entry.getKey();
	    String value = entry.getValue();
	    GenericNameValue contextParam = (GenericNameValue)source.get(value);
	    if (contextParam != null) {
		target.put(key, contextParam.getValue());
	    }
	}
	
	return null;
    }

}
