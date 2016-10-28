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
package net.segoia.netcell.entities;

import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.config.EasyFactoryConfiguration;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.DisplayTool;

import net.segoia.util.data.GenericNameValueContext;

public class DynamicEvalComponent extends GenericEntity<GenericNameValueContext> {
    private static Logger logger = Logger.getLogger(DynamicEvalComponent.class.getName());
    private static VelocityEngine velocityEngine;

    private static ToolManager velocityToolManager;
    private String builderIdentifier = "DynamicEvalComponent";
    static {

	velocityEngine = new VelocityEngine();

	EasyFactoryConfiguration toolboxConfig = new EasyFactoryConfiguration();
	toolboxConfig.toolbox(Scope.APPLICATION).tool(DisplayTool.class);
	toolboxConfig.toolbox(Scope.APPLICATION).tool(DateTool.class);

	velocityToolManager = new ToolManager();
	velocityToolManager.configure(toolboxConfig);

	try {
	    velocityEngine.init();
	    logger.info("Velocity Engine for Script Dao   successfully initialized");
	} catch (Exception e) {
	    logger.error("Error initializing the velocity engine", e);
	}
    }

    @Override
    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	GenericNameValueContext inputContext = (GenericNameValueContext) input.getValue("argsContext");
	String evalString = (String) inputContext.getValue("evalString");
	
	GenericNameValueContext globalContext = (GenericNameValueContext) inputContext.getValue(DynamicEntityWrapper.GLOBAL_CONTEXT);

	StringWriter sw = new StringWriter();
	Context toolContext = velocityToolManager.createContext();
	
	VelocityContext vc = new VelocityContext(globalContext.getNameValuesAsMap(), toolContext);

	velocityEngine.evaluate(vc, sw, builderIdentifier, evalString);
	GenericNameValueContext returnContext = new GenericNameValueContext();
	returnContext.put("return", sw.toString());
	return returnContext;
    }

}
