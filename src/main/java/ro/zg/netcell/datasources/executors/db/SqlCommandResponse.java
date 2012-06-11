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
/**
 * $Id: SqlCommandResponse.java,v 1.1 2008/07/02 07:43:31 aionescu Exp $
 */
package ro.zg.netcell.datasources.executors.db;

import java.util.List;
import java.util.Map;

public class SqlCommandResponse {
	private boolean isUpdate = false;
	private int rowsCount;
	private List<Map<String,?>> results;
	
	public SqlCommandResponse(List<Map<String,?>> results) {
		this.results = results;
		isUpdate = false;
		rowsCount = results.size();
	}
	
	public SqlCommandResponse(int updateCount) {
		rowsCount = updateCount;
		isUpdate = true;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public int getRowsCount() {
		return rowsCount;
	}
	
	public Map<String,?> getRowAtIndex(int index){
		if(isUpdate) {
			throw new IllegalArgumentException("An update command does not return a resultSet.Nothing to retrieve.");
		}
		if(results == null) {
			return null;
		}
		if(index >= results.size()) {
			throw new IllegalArgumentException("The specified index("+index+") is greater then the results size("+results.size()+")");
		}
		return results.get(index);
	}

	public List<Map<String, ?>> getResults() {
	    return results;
	}
}
