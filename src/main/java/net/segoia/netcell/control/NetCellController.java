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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;
import net.segoia.netcell.vo.DefinitionsRepository;
import net.segoia.netcell.vo.WorkflowContext;
import net.segoia.netcell.vo.definitions.EntitiesTypes;
import net.segoia.netcell.vo.definitions.EntityDefinition;
import net.segoia.netcell.vo.definitions.EntityDefinitionSummary;
import net.segoia.netcell.vo.definitions.EntityType;
import net.segoia.util.data.GenericNameValue;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.io.file.FileUtil;
import net.segoia.util.monitoring.MonitoringManager;

public class NetCellController implements NetCellControllerContract {
    private NetCellControllerConfiguration config;
    private boolean available = false;
    private boolean running = false;

    private ResourcesManager resourcesManager;

    public void start(String rootDir) throws ContextAwareException {
	if (running) {
	    return;
	}
	running = true;
	init();
    }

    private void init() throws ContextAwareException {
	available = false;
	initServices();
	available = true;
    }
    
    private void initServices() throws ContextAwareException{
	config.getServicesManager().init();
    }

    public NetCellControllerConfiguration getConfig() {
	return config;
    }

    public void setConfig(NetCellControllerConfiguration config) {
	this.config = config;
    }

    public boolean isAvailable() {
	return available;
    }

    public boolean isRunning() {
	return running;
    }

    /* contract implementation */

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	if (available) {
	    GenericNameValueContext response = config.getServicesManager().getExecutionEngine().execute(input);
	    return response;
	} else {
	    throw new ContextAwareException("SERVICE_NOT_AVAILABLE");
	}
    }
    
    public GenericNameValueContext execute(WorkflowContext wfContext) throws Exception {
	if (available) {
	    GenericNameValueContext response = config.getServicesManager().getExecutionEngine().execute(wfContext);
	    return response;
	} else {
	    throw new ContextAwareException("SERVICE_NOT_AVAILABLE");
	}
    }

    public DefinitionsRepository getDefinitions() throws ContextAwareException {
	return config.getServicesManager().getEntitiesManager().getEntitiesDefinitions();
    }


    public boolean containsEntityWithId(String entityId) throws ContextAwareException {
	return config.getServicesManager().getEntitiesManager().containsEntityWithId(entityId);
    }

    public boolean removeEntity(String entityId) throws ContextAwareException {
//	if (!executionEngineController.containsEntityWithId(entityId)) {
//	    ExceptionContext ec = new ExceptionContext();
//	    ec.put(new GenericNameValue("id", entityId));
//	    throw new ContextAwareException("UNKNOWN_ENTITY", ec);
//	}
//	String fileName = entityId + ".xml";
//	makeBackup(fileName);
//	String entityFilePath = getEntityFilePath(fileName);
//	return new File(entityFilePath).delete();
	return false;
    }

    public synchronized EntityDefinition createEntity(EntityDefinition ed) throws ContextAwareException {
	// ed.validate();
	// if (executionEngineController.containsEntityWithId(ed.getId())) {
	// ExceptionContext ec = new ExceptionContext();
	// ec.put(new GenericNameValue("id", ed.getId()));
	// throw new ContextAwareException("INVALID_ID", ec);
	// }
	// saveEntity(ed);
	// return true;
	return config.getServicesManager().getEntitiesManager().createEntity(ed);
    }
    
    public synchronized EntityDefinition createEntity(String name, EntityType entityType) throws ContextAwareException{
	return config.getServicesManager().getEntitiesManager().createEntityDirectoryStructure(name,entityType);
    }

    public synchronized EntityDefinition updateEntity(EntityDefinition ed) throws ContextAwareException {
	return config.getServicesManager().getEntitiesManager().updateEntity(ed);
    }
    
    public boolean refresh() throws ContextAwareException {
//	initDefinitionsController();
//	ExecutionEngineController execEngine = loadExecutionEngineController();
//	if (execEngine != null) {
//	    DestroyExecutionEngineThread oldEngineDestroyThread = new DestroyExecutionEngineThread(
//		    executionEngineController, config.getOldConfigDestroyTimeout());
//	    /* switch to the new configuration */
//	    executionEngineController = execEngine;
//	    oldEngineDestroyThread.start();
//	    return true;
//	}
	return false;
    }

    public boolean simulateRefresh() throws ContextAwareException {
//	ExecutionEngineController execEngine = loadExecutionEngineController();
//	ExecutableEntitiesManager defController = loadDefinitionsController();
//	return (execEngine != null && defController != null);
	return false;
    }

    // private void saveEntity(EntityDefinition ed) throws ContextAwareException {
    // String templatesPath = resourcesManager.getResourceFullPath("DEFINITIONS_TEMPLATES_DIR");
    // String templateName = config.getDefinitionsTemplates().get(ed.getType());
    // if (templateName == null) {
    // ExceptionContext ec = new ExceptionContext();
    // ec.put(new GenericNameValue("defType", ed.getType()));
    // throw new ContextAwareException("UNKNOWN_DEFINITION", ec);
    // }
    // String templatePath = templatesPath + File.separator + templateName;
    // String entityFilePath = getEntityFilePath(ed.getId() + ".xml");
    // try {
    // /* create file if not existent */
    // File ff = new File(entityFilePath);
    // if (!ff.exists()) {
    // ff.createNewFile();
    // }
    // FileWriter fw = new FileWriter(entityFilePath);
    // config.getDefinitionsGenerator().generateXmlDefinition(ed, fw, templatePath);
    // fw.close();
    // /* now refresh the definition controller */
    // initDefinitionsController();
    // } catch (IOException e) {
    // ExceptionContext ec = new ExceptionContext();
    // ec.put(new GenericNameValue("file", entityFilePath));
    // throw new ContextAwareException("ERROR_CREATING_DEFINITION_FILE", e, ec);
    // }
    // }

    public void makeBackup(String fileName) throws ContextAwareException {
	String entityFilePath = getEntityFilePath(fileName);
	String backupFilePath = getBackupFilePath(fileName);
	try {
	    FileUtil.copyFile(entityFilePath, backupFilePath);
	} catch (IOException e) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("file", fileName));
	    throw new ContextAwareException("SAVE_BACKUP_ERROR", e, ec);
	}
    }

    private String getEntityFilePath(String fileName) {
	// String entitiesDir = resourcesManager.getFullSystemPath("ENTITIES_DIR");
	// return entitiesDir + File.separator + fileName;
	return resourcesManager.getFullSystemPath("ENTITIES_DIR", fileName);
    }

    private String getBackupFilePath(String fileName) {
	// String entitiesDir = resourcesManager.getFullSystemPath("ENTITIES_BACKUP_DIR");
	// return entitiesDir + File.separator + fileName;
	return resourcesManager.getFullSystemPath("ENTITIES_BACKUP_DIR", fileName);
    }

    private void handleEngineDestoryError(Exception e) {

    }

    public String getResourceFullPath(String resourceType) {
	return resourcesManager.getFullSystemPath(resourceType);
    }

    public String getResourceFullPath(String resourceType, String resourceName) {
	return resourcesManager.getFullSystemPath(resourceType, resourceName);
    }

    public String getResourceRelativePath(String parent, String resourceName) {
	return resourcesManager.getResourceRelativePath(parent, resourceName);
    }

    /**
     * used to destroy previous running execution engines
     * 
     * @author adi
     * 
     */
    class DestroyExecutionEngineThread extends Thread {
	private ExecutionEngineController eec;
	private int timeout;

	public DestroyExecutionEngineThread(ExecutionEngineController eec, int timeout) {
	    this.eec = eec;
	    this.timeout = timeout;
	}

	public void run() {
	    if (eec.getPendingTasks() == 0) {
		destroyEngine();
	    } else {
		try {
		    Thread.sleep(timeout);
		} catch (InterruptedException e) {
		    handleEngineDestoryError(e);
		}
		destroyEngine();
	    }
	}

	private void destroyEngine() {
	    try {
		eec.destroy();
	    } catch (Exception e) {
		handleEngineDestoryError(e);
	    }
	}
    }

    public List<String> getDefinitionTypes() throws ContextAwareException {
	return config.getServicesManager().getEntitiesManager().getDefinitionTypes();
    }

    public List<String> getDefinitionTypes(String parentType) throws ContextAwareException {
	return config.getServicesManager().getEntitiesManager().getDefinitionTypes(parentType);
    }

    public boolean removeEntity(EntityDefinition ed) throws ContextAwareException {
	return config.getServicesManager().getEntitiesManager().removeEntity(ed);
    }

    public MonitoringManager getMonitoringManager() throws ContextAwareException {
	return config.getServicesManager().getMonitoringManager();
    }

    public List<String> getTemplatesIdsForEntity(String entityType) throws ContextAwareException {
	return config.getServicesManager().getEntitiesManager().getEntityDefinitionTemplatesIds(entityType);
    }
    
    public String getNodeId() {
	return config.getServicesManager().getProcessingNodeId();
    }
    
    public Map<String,EntityDefinitionSummary> getWorkflowDefinitionSummaries() throws ContextAwareException{
	return config.getServicesManager().getEntitiesManager().getEnitityDefinitionSummariesForType(EntitiesTypes.WORKFLOW);
    }
}
