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
package ro.zg.netcell.entities.util.collections;

import ro.zg.netcell.entities.GenericEntity;
import ro.zg.util.data.GenericNameValue;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;
import ro.zg.util.data.NameValue;
import ro.zg.util.data.ObjectsUtil;

public class ConcatenateTwoLists extends GenericEntity<GenericNameValueContext>{

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	GenericNameValueContext first = (GenericNameValueContext)input.getValue("first");
	GenericNameValueContext last = (GenericNameValueContext)input.getValue("last");
	GenericNameValueList resultList = new GenericNameValueList();
	first.copyTo(resultList);
	for(NameValue<Object> p : last.getParametersAsList()){
//	    GenericNameValue np = ((GenericNameValue)p).copy();
//	    np.setName(""+resultList.size());
//	    resultList.put(np);
	    resultList.addValue(ObjectsUtil.copy(p.getValue()));
	}
	GenericNameValueContext response = new GenericNameValueContext();
	response.put("result",resultList);
	return response;
    }

}
