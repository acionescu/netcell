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
package org.apache.velocity.runtime.resource.loader;

import java.io.InputStream;

import net.segoia.netcell.control.ResourcesManager;

import org.apache.velocity.exception.ResourceNotFoundException;
/**
 * This loader attempts to load the template from the classpath and if it fails it tries to
 * load it from the file system
 * @author adi
 *
 */
public class GenericResourceLoader extends ClasspathResourceLoader{
    private ResourcesManager resourcesManager;
    
    @Override
    public InputStream getResourceStream(String source) throws ResourceNotFoundException {
	InputStream is = resourcesManager.getResourcesLoader().getResourceAsStream(source);
	return is;
    }

    /**
     * @return the resourcesManager
     */
    public ResourcesManager getResourcesManager() {
        return resourcesManager;
    }

    /**
     * @param resourcesManager the resourcesManager to set
     */
    public void setResourcesManager(ResourcesManager resourcesManager) {
        this.resourcesManager = resourcesManager;
    }

}
