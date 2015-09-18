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

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import junit.framework.TestCase;
import net.segoia.commons.exceptions.ContextAwareException;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Ignore;
@Ignore
public class VelocityTest extends TestCase {
    private VelocityEngine velocityEngine;

    public void setUp() throws Exception {

	velocityEngine = new VelocityEngine();
	Properties p = new Properties();
	p.setProperty("resource.loader", "file");
	p.setProperty("class.resource.loader.class",
		"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
	p.setProperty("file.resource.loader.class",
			"org.apache.velocity.runtime.resource.loader.GenericResourceLoader");
	p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_CACHE, "true");
	try {
	    velocityEngine.init(p);
	} catch (Exception e) {
	    throw new ContextAwareException("DEFINITIONS_GENERATOR_INIT_ERROR", e);
	}

    }

    public void testThisShit() throws Exception {
	String path = "/media/disk/work/projects/netcell/src/test/resources/root/config/definitions/templates/workflow-definition-template.xml";
	VelocityContext vc = new VelocityContext();
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	PrintWriter writer = new PrintWriter(bos);
	velocityEngine.mergeTemplate(path, vc, writer);
	System.out.println(bos.size());
    }
}
