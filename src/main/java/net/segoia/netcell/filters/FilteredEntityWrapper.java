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
package net.segoia.netcell.filters;

import net.segoia.util.execution.ExecutionEntity;
import net.segoia.util.execution.ExecutionEntityWrapper;

public class FilteredEntityWrapper<E extends ExecutionEntity<I, O>, I, O> extends  ExecutionEntityWrapper<E,I,O>{

    /* (non-Javadoc)
     * @see net.segoia.util.execution.ExecutionEntityWrapper#execute(java.lang.Object)
     */
    @Override
    public O execute(I input) throws Exception {
	// TODO Auto-generated method stub
	return super.execute(input);
    }

}
