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
package ro.zg.netcell.datasources.translators;

import java.util.Map;

import ro.zg.netcell.datasources.executors.db.SqlCommandResponse;
import ro.zg.scriptdao.core.ResponseTranslator;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;

public class DatabaseResponseTranslator implements ResponseTranslator<SqlCommandResponse, GenericNameValueContext>{

    public GenericNameValueContext translate(SqlCommandResponse input) {
	GenericNameValueContext response = new GenericNameValueContext();
	response.put("count",input.getRowsCount());
	if(!input.isUpdate()){
	    GenericNameValueList result = new GenericNameValueList();
	    int count = 0;
	    for(Map<String, ?> row : input.getResults()){
		GenericNameValueContext currentContext = new GenericNameValueContext();
		currentContext.putMap(row);
		result.addValue(currentContext);
	    }
	    response.put("result",result);
	}
	return response;
    }

    public GenericNameValueContext translate(SqlCommandResponse[] inputs) {
	throw new UnsupportedOperationException("not implemented");
    }

}
