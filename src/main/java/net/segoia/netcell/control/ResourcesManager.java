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
package net.segoia.netcell.control;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;
import net.segoia.util.resources.ResourcesUtil;

public class ResourcesManager {

    private String rootDir;
    private Map<String, String> resourcesRelativePaths;
    private Map<String, String> resourcesFullPaths;
    private Map<String, String> fullSystemPaths;
    private ClassLoader resourcesLoader = ClassLoader.getSystemClassLoader();

    public void init() {
	if (rootDir == null || resourcesRelativePaths == null || resourcesLoader == null) {
	    return;
	}
	computeFullPaths();
    }

    private void computeFullPaths() {
	resourcesFullPaths = new HashMap<String, String>();
	fullSystemPaths = new HashMap<String, String>();
//	String rootDirSystemPath = getFullSystemPathForRoot();

	for (Map.Entry<String, String> entry : resourcesRelativePaths.entrySet()) {
	    resourcesFullPaths.put(entry.getKey(), getFullPath(entry.getValue()));
	    fullSystemPaths.put(entry.getKey(), getSystemPath(entry.getValue()));
	}
    }

    private String getFullSystemPathForRoot() {
//	File f = new File(rootDir);
//	if (f.exists()) {
//	    return f.getAbsolutePath();
//	}
	return resourcesLoader.getResource(rootDir).getFile();
    }
    
    private String getSystemPath(String resName){
	URL url = resourcesLoader.getResource(getResourceRelativePath(rootDir, resName));
	if(url != null){
	    return url.getFile();
	}
	return null;
    }

    private String getFullPath(String relativePath) {
	return getResourceRelativePath(rootDir, relativePath);
    }

    public String getResourceRelativePath(String parent, String child) {
	return parent + File.separator + child;
    }

    public String getResourceFullPath(String resource) {
	return resourcesFullPaths.get(resource);
    }

    public String getFullSystemPath(String resource) {
	return fullSystemPaths.get(resource);
    }

    public String getFullSystemPath(String resource, String child){
	return getResourceRelativePath(getFullSystemPath(resource), child);
    }
    /**
     * The baseDir should already exist
     * @param baseDir
     * @param filePath
     * @param create
     * @return
     * @throws ContextAwareException 
     */
    public URL getUrl(String baseDir,String filePath,boolean create) throws ContextAwareException {
	return ResourcesUtil.getURL(baseDir, filePath, resourcesLoader, create);
    }
    
    public boolean removeResource(URL url) throws ContextAwareException {
	return ResourcesUtil.removeResource(url);
    }
    
    public String getRootDir() {
	return rootDir;
    }

    public void setRootDir(String rootDir) {
	this.rootDir = rootDir;
	init();
    }

    public Map<String, String> getResourcesRelativePaths() {
	return resourcesRelativePaths;
    }

    public void setResourcesRelativePaths(Map<String, String> entitiesRelativePaths) {
	this.resourcesRelativePaths = entitiesRelativePaths;
	init();
    }

    /**
     * @return the resourcesLoader
     */
    public ClassLoader getResourcesLoader() {
        return resourcesLoader;
    }

    /**
     * @param resourcesLoader the resourcesLoader to set
     */
    public void setResourcesLoader(ClassLoader resourcesLoader) {
        this.resourcesLoader = resourcesLoader;
        init();
    }

    /**
     * @return the fullSystemPaths
     */
    public Map<String, String> getFullSystemPaths() {
        return fullSystemPaths;
    }
    
}
