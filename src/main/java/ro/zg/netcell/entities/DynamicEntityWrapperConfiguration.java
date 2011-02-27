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

import java.io.Serializable;
import java.util.Map;

import ro.zg.netcell.vo.configurations.ExceptionMapping;
import ro.zg.util.data.ConfigurationData;
import ro.zg.util.data.GenericNameValueContext;

public class DynamicEntityWrapperConfiguration<O> implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -3130778775072944944L;
    private Map<String, Object> staticParameters;
    private GenericEntity<O> executor;
    private GenericNameValueContext staticContext;
    private Map<String, String> dynamicParameters;
    private String originalInputName;
    private Map<String,ExceptionMapping> exceptionMappings;
    private ConfigurationData userConfig;
    
    private int entityType;

    public void init() {
	if (staticParameters != null) {
	    staticContext = new GenericNameValueContext();
	    staticContext.putMap(staticParameters);
	}
    }

    public Map<String, Object> getStaticParameters() {
	return staticParameters;
    }

    public GenericNameValueContext getStaticContext() {
	return staticContext;
    }

    public Map<String, String> getDynamicParameters() {
	return dynamicParameters;
    }

    public void setStaticParameters(Map<String, Object> staticParameters) {
	this.staticParameters = staticParameters;
    }

    public void setDynamicParameters(Map<String, String> dynamicParameters) {
	this.dynamicParameters = dynamicParameters;
    }

    public GenericEntity<O> getExecutor() {
	return executor;
    }

    public void setExecutor(GenericEntity<O> executor) {
	this.executor = executor;
    }

    public String getOriginalInputName() {
        return originalInputName;
    }

    public void setOriginalInputName(String originalInputName) {
        this.originalInputName = originalInputName;
    }

    /**
     * @return the exceptionMappings
     */
    public Map<String, ExceptionMapping> getExceptionMappings() {
        return exceptionMappings;
    }

    /**
     * @param exceptionMappings the exceptionMappings to set
     */
    public void setExceptionMappings(Map<String, ExceptionMapping> exceptionMappings) {
        this.exceptionMappings = exceptionMappings;
    }

    /**
     * @return the entityType
     */
    public int getEntityType() {
        return entityType;
    }

    /**
     * @param entityType the entityType to set
     */
    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    /**
     * @return the userConfig
     */
    public ConfigurationData getUserConfig() {
        return userConfig;
    }

    /**
     * @param userConfig the userConfig to set
     */
    public void setUserConfig(ConfigurationData userConfig) {
        this.userConfig = userConfig;
    }
    
}
