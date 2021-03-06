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

import junit.framework.TestCase;
import net.segoia.cfgengine.core.configuration.ConfigurationManager;
import net.segoia.cfgengine.util.PackageCfgLoader;

import org.junit.Ignore;

@Ignore
public class BaseDefinitionsConfigTestCase extends TestCase{
protected ConfigurationManager cfgManager;
    
    public void setUp() throws Exception{
	String dir = "root/config/definitions/";
	String handlers = dir+"definitions-handlers.xml";
	String config = dir+"definitions-config.xml";
	cfgManager = PackageCfgLoader.getInstance().load(handlers, config);
    }
}
