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
package net.segoia.netcell.definitions;

import java.io.StringWriter;
import java.util.Properties;

import net.segoia.netcell.vo.configurations.WorkFlowConfiguration;
import net.segoia.netcell.vo.definitions.ExecutableEntityDefinition;
import net.segoia.netcell.vo.definitions.WorkFlowDefinition;
import net.segoia.util.data.reflection.ReflectionUtility;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class DefinitionsTestCase extends BaseDefinitionsConfigTestCase {
    VelocityEngine velEngine;
    
    public void setUp() throws Exception{
	super.setUp();
	
	velEngine = new VelocityEngine();
	Properties p = new Properties();
	p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, "root/config/definitions/templates");
	p.setProperty("resource.loader", "class");
	p.setProperty("class.resource.loader.class",
		"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
	p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_CACHE, "true");
	velEngine.init(p);
    }
    public void testLoadDef() throws Exception {
	WorkFlowConfiguration wfc = (WorkFlowConfiguration) cfgManager.getObjectById("firstFlow");
	assertNotNull(wfc);
	System.out.println("damn!");
	System.out.println(wfc.getComponents().get("comp1").getComponentConfig().getComponent());

	
	VelocityContext vc = new VelocityContext();
	vc.put("componentConfig", wfc.getComponents().get("comp2").getComponentConfig());
	StringWriter pw = new StringWriter();
	velEngine.mergeTemplate("root/config/definitions/templates/component-template.xml", vc, pw);
	System.out.println(pw);
    }

    public void testFlowLogicMapping() throws Exception{
	WorkFlowConfiguration wfc = (WorkFlowConfiguration) cfgManager.getObjectById("firstFlow");
	assertNotNull(wfc);
	
	VelocityContext vc = new VelocityContext();
	vc.put("compMapping", wfc.getComponents().get("comp1").getComponentMapping());
	StringWriter pw = new StringWriter();
	velEngine.mergeTemplate("root/config/definitions/templates/flow-logic-template.xml", vc, pw);
	System.out.println(pw);
    }
    
    public void testWorkflowComponent() throws Exception{
	WorkFlowConfiguration wfc = (WorkFlowConfiguration) cfgManager.getObjectById("firstFlow");
	assertNotNull(wfc);
	
	VelocityContext vc = new VelocityContext();
	vc.put("workflowComponent", wfc.getComponents().get("comp2"));
	StringWriter pw = new StringWriter();
	velEngine.mergeTemplate("root/config/definitions/templates/workflow-component-template.xml", vc, pw);
	System.out.println(pw);
    }
    
    public void testWorkflow() throws Exception{
	WorkFlowConfiguration wfc = (WorkFlowConfiguration) cfgManager.getObjectById("firstFlow");
	assertNotNull(wfc);
	
	VelocityContext vc = new VelocityContext();
	vc.put("workflowConfig", wfc);
	StringWriter pw = new StringWriter();
	velEngine.mergeTemplate("root/config/definitions/templates/workflow-template.xml", vc, pw);
	System.out.println("---------------------------------------------------------------------");
	System.out.println(pw);
    }
    
    public void testWorkFlowDefinition() throws Exception{
	WorkFlowDefinition wfd = (WorkFlowDefinition) cfgManager.getObjectById("wrappedFirstFlow");
	assertNotNull(wfd);
	assertNotNull(wfd.getType());
	VelocityContext vc = new VelocityContext();
	vc.put("workflowDefinition", wfd);
	StringWriter pw = new StringWriter();
	velEngine.mergeTemplate("root/config/definitions/templates/workflow-definition-template.xml", vc, pw);
	System.out.println("---------------------------------------------------------------------");
	System.out.println(pw);
    }
    
    public void testCompDefinition() throws Exception{
	ExecutableEntityDefinition ed = (ExecutableEntityDefinition) cfgManager.getObjectById("comparator");
	assertNotNull(ed);
	assertNotNull(ed.getType());
	System.out.println(ReflectionUtility.objectToString(ed));
    }
    
    public void testObjToMap() throws Exception{
	WorkFlowDefinition wfd = (WorkFlowDefinition) cfgManager.getObjectById("generatedFlow2");
	assertNotNull(wfd);
//	assertNotNull(wfd.getType());
	System.out.println(ReflectionUtility.objectToString(wfd));
    }
}
