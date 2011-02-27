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
package ro.zg.netcell.config;

import junit.framework.TestCase;
import ro.zg.cfgengine.core.configuration.ConfigurationManager;
import ro.zg.cfgengine.util.PackageCfgLoader;
import ro.zg.netcell.control.ExecutionEngineController;
import ro.zg.netcell.control.ExecutionEngineLoader;

public class BaseEngineConfigTestCase extends TestCase{
    protected ConfigurationManager cfgManager;
    protected ExecutionEngineController engineController;
    
    public void setUp() throws Exception{
	String dir = "root/config/engine/";
	String handlers = dir+"engine-handlers.xml";
	String config = dir+"engine-config.xml";
	cfgManager = PackageCfgLoader.getInstance().load(handlers, config);
	engineController = new ExecutionEngineLoader().load(handlers, config);
    }
}
