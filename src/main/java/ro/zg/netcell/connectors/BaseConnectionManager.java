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
package ro.zg.netcell.connectors;

import ro.zg.netcell.vo.definitions.DataSourceDefinition;
import ro.zg.scriptdao.core.ConnectionManager;

public abstract class BaseConnectionManager<C> implements ConnectionManager<C>{
    protected DataSourceDefinition dataSourceDefinition;

    /**
     * @return the dataSourceDefinition
     */
    public DataSourceDefinition getDataSourceDefinition() {
        return dataSourceDefinition;
    }

    /**
     * @param dataSourceDefinition the dataSourceDefinition to set
     */
    public void setDataSourceDefinition(DataSourceDefinition dataSourceDefinition) {
        this.dataSourceDefinition = dataSourceDefinition;
    }
    
    
}
