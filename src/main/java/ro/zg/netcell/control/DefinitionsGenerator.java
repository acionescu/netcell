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
package ro.zg.netcell.control;

import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.netcell.vo.definitions.EntityDefinition;
import ro.zg.util.data.GenericNameValue;

public class DefinitionsGenerator {
    private VelocityEngine velocityEngine;
    private boolean engineInitialized;
    private ResourceLoader resourcesLoader;
    
    
    private void initEngine() throws ContextAwareException{
	velocityEngine = new VelocityEngine();String s = VelocityEngine.SET_NULL_ALLOWED;
	Properties p = new Properties();
	p.setProperty("resource.loader", "instance");
	p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_CACHE, "true");
	p.setProperty(VelocityEngine.VM_LIBRARY, "root/config/util/macros/generic-macros.xml");
	try {
	    velocityEngine.setProperty("instance.resource.loader.instance", resourcesLoader);
	    velocityEngine.init(p);
	} catch (Exception e) {
	    throw new ContextAwareException("DEFINITIONS_GENERATOR_INIT_ERROR",e);
	}
	engineInitialized = true;
    }
    
    public void generateXmlDefinition(EntityDefinition definition, Writer writer,String template) throws ContextAwareException{
	generateXmlDefinition(definition, writer, template,null);
    }
    
    public void generateXmlDefinition(EntityDefinition definition, Writer writer,String template,Map additionalParams) throws ContextAwareException{
	String defType = definition.getType();
	if(!engineInitialized){
	    initEngine();
	}
	VelocityContext vc = new VelocityContext(additionalParams);
	vc.put("entityDefinition", definition);
	vc.put("escapeUtils", StringEscapeUtils.class);
	
	try {
//	    velocityEngine.mergeTemplate(template, vc, writer);
	    velocityEngine.mergeTemplate(template, "UTF8", vc, writer);
	} catch (Exception e) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("defType",defType));
	    ec.put(new GenericNameValue("template",template));
	    ec.put(new GenericNameValue("velocityContext",vc));
	    throw new ContextAwareException("ERROR_GENERATING_DEFINITION",e,ec);
	}
    }
    
    public void generateXmlDefinition(Writer writer,String template,Map<?,?> additionalParams) throws ContextAwareException{
	if(!engineInitialized){
	    initEngine();
	}
	VelocityContext vc = new VelocityContext(additionalParams);
	vc.put("escapeUtils", StringEscapeUtils.class);
	try {
//	    velocityEngine.mergeTemplate(template, vc, writer);
	    velocityEngine.mergeTemplate(template, "UTF8", vc, writer);
	} catch (Exception e) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("template",template));
	    ec.put(new GenericNameValue("velocityContext",additionalParams));
	    throw new ContextAwareException("ERROR_GENERATING_DEFINITION",e,ec);
	}
    }

    /**
     * @return the resourcesLoader
     */
    public ResourceLoader getResourcesLoader() {
        return resourcesLoader;
    }

    /**
     * @param resourcesLoader the resourcesLoader to set
     */
    public void setResourcesLoader(ResourceLoader resourcesLoader) {
        this.resourcesLoader = resourcesLoader;
    }

}
