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

import net.segoia.netcell.entities.GenericEntity;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.data.GenericNameValueList;

public class AppendToList extends GenericEntity<GenericNameValueContext>{

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	GenericNameValueList list = (GenericNameValueList)input.getValue("list");
	Object value = input.getValue("value");
	list.addValue(value);
	return new GenericNameValueContext();
    }

}
