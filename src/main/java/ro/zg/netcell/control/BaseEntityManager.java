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

import java.io.File;
import java.net.URL;
import java.util.Map;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.netcell.control.generators.BaseEntityDefinitionGenerator;
import ro.zg.netcell.vo.definitions.EntityDefinition;
import ro.zg.util.data.GenericNameValue;

public abstract class BaseEntityManager<E extends EntityDefinition> extends BaseEntityDefinitionGenerator<E> implements EntityManager<E> {
    protected EntitiesManager entitiesManager;
//    protected ResourcesManager resourcesManager;
    protected ClassLoader resourcesLoader;
    /**
     * key - definition type
     * </br>
     * value - template name used to generate the xml 
     */
    private Map<String,String> definitionsTemplates;  
    
//    private DefinitionsGenerator definitionsGenerator;
    
    public E createEntity(E ed) throws ContextAwareException {
	validateCreate(ed);
	return saveEntity(ed);
    }
    
    protected void validateCreate(E ed) throws ContextAwareException{
	ed.validate();
	if (entitiesManager.containsEntityWithId(ed.getId())) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("id", ed.getId()));
	    throw new ContextAwareException("INVALID_ID", ec);
	}
    }
    
    protected void validateUpdate(E ed) throws ContextAwareException{
	ed.validate();
	if (!entitiesManager.containsEntityWithId(ed.getId())) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("id", ed.getId()));
	    throw new ContextAwareException("UNKNOWN_ENTITY", ec);
	}
    }
    
    public synchronized E updateEntity(E ed) throws ContextAwareException {
	validateUpdate(ed);
	makeBackup(ed);
	return saveEntity(ed);
    }
    
    protected boolean removeResource(URL url) throws ContextAwareException{
	return getResourcesManager().removeResource(url);
    }
    
    public void makeBackup(EntityDefinition ed) throws ContextAwareException {
//	String fileName = ed.getId() + ".xml";
//	String entityFilePath = controller.getResourceFullPath(ResourcesTypes.EXECUTABLE_ENTITIES_DIR, fileName);
//	String mainBackupPath = controller.getResourceFullPath(ResourcesTypes.BACKUP_DIR);
//	String backupDir = controller.getResourceRelativePath(mainBackupPath, ed.getType());
//	String backupFilePath = controller.getResourceRelativePath(backupDir,fileName);
//	try {
//	    new File(backupDir).mkdirs();
//	    FileUtil.copyFile(entityFilePath, backupFilePath);
//	} catch (IOException e) {
//	    ExceptionContext ec = new ExceptionContext();
//	    ec.put(new GenericNameValue("file", fileName));
//	    throw new ContextAwareException("SAVE_BACKUP_ERROR", e, ec);
//	}
    }
    
//    protected void generateDefinition(String entityFilePath, Object entity, String templatePath, String definitionContextName) throws ContextAwareException{
//	try {
//	    /* transform the package separators into file separators */
//	    entityFilePath = entityFilePath.replace(".",File.separator);
//	    /* add suffix */
//	    entityFilePath += ".xml";
//	    /* create file if not existent */
//	    File ff = new File(entityFilePath);
//	    if (!ff.exists()) {
//		ff.getParentFile().mkdirs();
//		ff.createNewFile();
//	    }
//	    FileWriter fw = new FileWriter(entityFilePath);
//	    Map<Object,Object> params = new HashMap<Object, Object>();
//	    params.put(definitionContextName, entity);
//	    getDefinitionsGenerator().generateXmlDefinition(fw, templatePath, params);
//	    fw.close();
//	} catch (IOException e) {
//	    ExceptionContext ec = new ExceptionContext();
//	    ec.put(new GenericNameValue("file", entityFilePath));
//	    throw new ContextAwareException("ERROR_CREATING_DEFINITION_FILE", e, ec);
//	}
//    }

    /**
     * @return the entitiesManager
     */
    public EntitiesManager getEntitiesManager() {
        return entitiesManager;
    }

    /**
     * @param entitiesManager the entitiesManager to set
     */
    public void setEntitiesManager(EntitiesManager entitiesManager) {
        this.entitiesManager = entitiesManager;
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
    }

    /**
     * @return the definitionsTemplates
     */
    public Map<String, String> getDefinitionsTemplates() {
        return definitionsTemplates;
    }

    /**
     * @param definitionsTemplates the definitionsTemplates to set
     */
    public void setDefinitionsTemplates(Map<String, String> definitionsTemplates) {
        this.definitionsTemplates = definitionsTemplates;
    }
    
}
