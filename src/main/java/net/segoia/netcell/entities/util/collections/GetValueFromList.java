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
package net.segoia.netcell.entities.util.collections;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;
import net.segoia.netcell.constants.ExceptionTypes;
import net.segoia.netcell.entities.GenericEntity;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.data.GenericNameValueList;

public class GetValueFromList extends GenericEntity<GenericNameValueContext> {

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	GenericNameValueList list = (GenericNameValueList) input.getValue("list");
	Number index = (Number) input.getValue("index");
	int i = index.intValue();

	if (i < 0 || i >= list.size()) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put("index", index);
	    ec.put("size", list.size());
	    throw new ContextAwareException(ExceptionTypes.INDEX_OUT_OF_BOUNDS,ec);
	}
	GenericNameValueContext response = new GenericNameValueContext();
	response.put("value", list.getValueForIndex(i));
	return response;
    }

}
