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
package ro.zg.netcell.control.commands;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.netcell.vo.WorkflowContext;
import ro.zg.util.data.GenericNameValue;
import ro.zg.util.data.GenericNameValueContext;

public class ExecuteEntity extends BaseCommandExecutor{

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
//	return getNetCellController().execute(input);
	
	GenericNameValue flow = (GenericNameValue) input.remove("fid");
	if (flow == null) {
	    throw new ContextAwareException("FLOW_ID_EXPECTED");
	}
	String flowId = (String) flow.getValue();
	WorkflowContext wfContext = new WorkflowContext();
	wfContext.setParametersContext(input);
	wfContext.setFlowId(flowId);
	return getNetCellController().execute(wfContext);
    }

}
