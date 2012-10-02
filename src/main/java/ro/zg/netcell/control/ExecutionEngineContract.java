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
package ro.zg.netcell.control;

import ro.zg.netcell.vo.WorkflowContext;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.execution.ExecutionEntity;

public interface ExecutionEngineContract extends ExecutionEntity<GenericNameValueContext, GenericNameValueContext>{

    boolean reload() throws Exception;
    GenericNameValueContext execute(WorkflowContext wfContext) throws Exception;
}
