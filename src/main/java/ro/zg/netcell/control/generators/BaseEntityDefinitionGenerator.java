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
package ro.zg.netcell.control.generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.netcell.control.DefinitionsGenerator;
import ro.zg.netcell.control.ResourcesManager;
import ro.zg.netcell.vo.definitions.EntityDefinition;
import ro.zg.util.data.GenericNameValue;

public abstract class BaseEntityDefinitionGenerator<E extends EntityDefinition> implements EntityDefinitionGenerator<E> {
    public static final String COMMAND_MANAGER_TEMPLATE = "command-manager-template.xml";
    public static final String CONNECTION_MANAGER_TEMPLATE = "connection-manager-template.xml";

    protected ResourcesManager resourcesManager;
    private DefinitionsGenerator definitionsGenerator;
    private String templatesDir;

    protected void generateDefinition(URL entityFileUrl, Object entity, String templatePath,
	    String definitionContextName) throws ContextAwareException {
	try {
	    // /* transform the package separators into file separators */
	    // entityFilePath = entityFilePath.replace(".",File.separator);
	    // /* add suffix */
	    // entityFilePath += ".xml";
	    // /* create file if not existent */
	    // File ff = new File(entityFilePath);
	    // if (!ff.exists()) {
	    // ff.getParentFile().mkdirs();
	    // ff.createNewFile();
	    // }
	    // FileWriter fw = new FileWriter(entityFilePath);
//	    BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(entityFileUrl.openConnection()
//		    .getOutputStream()));
	    Writer fw = getWriterForUrl(entityFileUrl);
	    Map<Object, Object> params = new HashMap<Object, Object>();
	    params.put(definitionContextName, entity);
	    getDefinitionsGenerator().generateXmlDefinition(fw, templatePath, params);
	    fw.close();
	} catch (IOException e) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("file", entityFileUrl.toString()));
	    throw new ContextAwareException("ERROR_CREATING_DEFINITION_FILE", e, ec);
	}
    }

    protected String getEntityFileForId(String id) {
	String entityFilePath = id.replace(".", File.separator);
	/* add suffix */
	entityFilePath += ".xml";
	return entityFilePath;
    }
    
    protected String getFlatFileForEntityId(String id) {
	String entityFilePath = id.replace(".", "_");
	return entityFilePath+".xml";
    }
    
    private Writer getWriterForUrl(URL url) throws IOException {
	String protocol = url.getProtocol();
	if(protocol.equals("ftp")) {
	    BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(url.openConnection()
		    .getOutputStream()));
	    return fw;
	}
	else if(protocol.equals("file")) {
	    return new FileWriter(url.getPath());
	}
	throw new IOException("Unknown protocol "+protocol);
    }

    /**
     * @return the resourcesManager
     */
    public ResourcesManager getResourcesManager() {
	return resourcesManager;
    }

    /**
     * @param resourcesManager
     *            the resourcesManager to set
     */
    public void setResourcesManager(ResourcesManager resourcesManager) {
	this.resourcesManager = resourcesManager;
    }

    /**
     * @return the templatesDir
     */
    public String getTemplatesDir() {
	return templatesDir;
    }

    /**
     * @param templatesDir
     *            the templatesDir to set
     */
    public void setTemplatesDir(String templatesDir) {
	this.templatesDir = templatesDir;
    }

    /**
     * @return the definitionsGenerator
     */
    public DefinitionsGenerator getDefinitionsGenerator() {
	return definitionsGenerator;
    }

    /**
     * @param definitionsGenerator
     *            the definitionsGenerator to set
     */
    public void setDefinitionsGenerator(DefinitionsGenerator definitionsGenerator) {
	this.definitionsGenerator = definitionsGenerator;
    }

}
