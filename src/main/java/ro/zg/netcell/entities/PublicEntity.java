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
package ro.zg.netcell.entities;

import java.util.List;
import java.util.Map;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.netcell.vo.InputParameter;
import ro.zg.util.data.GenericNameValueContext;

public class PublicEntity extends GenericEntity<Object>{
    private GenericEntity<GenericNameValueContext> inputParamsProcessor;

    public Object execute(GenericNameValueContext input) throws Exception {
	List<InputParameter> inputParams = (List<InputParameter>)input.getValue("inputParameters");
	GenericEntity<Object> executor = (GenericEntity<Object>)input.getValue("executor");
	GenericNameValueContext inputContext = (GenericNameValueContext)input.getValue("inputContext");
	GenericNameValueContext newContext = inputParamsProcessor.execute(input);
	
	Object result = executor.execute(newContext);
	Map<Object,Object> simpleResponseMappings = (Map<Object,Object>)input.getValue("simpleResponse");
	if(simpleResponseMappings != null){
	    return processSimpleResponse(result, simpleResponseMappings);
	}
	return result;
    }
    
    public Object processSimpleResponse(Object rawResult,Map<Object,Object> respMappings) throws ContextAwareException{
	Object res = respMappings.get(rawResult.toString());
	if(res == null){
	    ExceptionContext ec = new ExceptionContext();
	    ec.put("result",rawResult);
	    throw new ContextAwareException("UNKNOWN_RESULT",ec);
	}
	GenericNameValueContext resContext = new GenericNameValueContext();
	resContext.put("exit", res);
	return resContext;
    }

    public GenericEntity<GenericNameValueContext> getInputParamsProcessor() {
        return inputParamsProcessor;
    }

    public void setInputParamsProcessor(GenericEntity<GenericNameValueContext> inputParamsProcessor) {
        this.inputParamsProcessor = inputParamsProcessor;
    }
    
}
