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
package net.segoia.netcell.control.generators;

import java.io.File;
import java.net.URL;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.netcell.constants.ResourcesTypes;
import net.segoia.netcell.vo.definitions.DataSourceDefinition;

public class DatasourceEntityDefinitionGenerator extends BaseEntityDefinitionGenerator<DataSourceDefinition>{

    public DataSourceDefinition saveEntity(DataSourceDefinition entity) throws ContextAwareException {
	String rootTemplatesDir = getResourcesManager().getResourceFullPath(ResourcesTypes.DATASOURCES_TEMPLATES_DIR);
	String dsTemplatesDir = getResourcesManager().getResourceRelativePath(rootTemplatesDir, getTemplatesDir());
	
	String connectionManagerTemplatePath = getResourcesManager().getResourceRelativePath(dsTemplatesDir, CONNECTION_MANAGER_TEMPLATE);
	String commandManagerTemplatePath = getResourcesManager().getResourceRelativePath(dsTemplatesDir, COMMAND_MANAGER_TEMPLATE);
	
	String currentDsDir = getDatasourceDirForName(entity.getId());
//	String commandManagerFileName = getResourcesManager().getResourceRelativePath(currentDsDir,"command-manager-config");
//	String connectionManagerFileName = getResourcesManager().getResourceRelativePath(currentDsDir,"connection-manager-config");
	
	URL commandManagerUrl = getResourcesManager().getUrl(currentDsDir, getEntityFileForId("command-manager-config"), true);
	URL connectionManagerUrl = getResourcesManager().getUrl(currentDsDir, getEntityFileForId("connection-manager-config"), true);
	
	generateDefinition(connectionManagerUrl,entity,connectionManagerTemplatePath,"datasourceDefinition");
	generateDefinition(commandManagerUrl,entity,commandManagerTemplatePath,"datasourceDefinition");
	
	return entity;
    }
    
    protected String getDatasourceDirForName(String name) throws ContextAwareException{
//	String datasourcesDir = getResourcesManager().getFullSystemPath(ResourcesTypes.DATASOURCES_DIR);
	String datasourcesDir = getResourcesManager().getResourceFullPath(ResourcesTypes.DATASOURCES_DIR);
//	return getResourcesManager().getUrl(datasourcesDir, name+File.separator, true).toString();
	getResourcesManager().getUrl(datasourcesDir, name+File.separator, true);
	return datasourcesDir+name+File.separator;
    }

}
