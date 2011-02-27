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

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import ro.zg.cfgengine.core.configuration.ConfigurationManager;
import ro.zg.cfgengine.core.exceptions.ConfigurationException;
import ro.zg.cfgengine.util.PackageCfgLoader;
import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.netcell.constants.ResourcesTypes;
import ro.zg.netcell.scheduler.ExtendedCronTrigger;
import ro.zg.netcell.scheduler.ExtendedJobExecutionController;
import ro.zg.netcell.vo.definitions.EntityType;
import ro.zg.netcell.vo.definitions.ScheduledJobDefinition;
import ro.zg.util.logging.MasterLogManager;

public class ScheduledJobsManager extends BaseEntityManager<ScheduledJobDefinition> implements JobSchedulerContract {
    private ClassLoader resourcesLoader;
    private ResourcesManager resourcesManager;
    private Scheduler scheduler;
    private ExtendedJobExecutionController jobsController;
    private int startDelay=10;
    
    private Map<String,ScheduledJobDefinition> scheduledJobs=new LinkedHashMap<String, ScheduledJobDefinition>();

    public void init() throws ContextAwareException {
	try {
	    loadJobs();
	    initScheduler();
	    scheduleJobs();
//	    scheduler.startDelayed(startDelay);
	} catch (Exception e) {
	    throw new ContextAwareException("SCHEDULER_INIT_ERROR",e);
	} 
	MasterLogManager.getLogger("SCHEDULER").info("Scheduler successfuly initialized");
    }

    private void initScheduler() throws IOException, SchedulerException {
	String propertiesFile = resourcesManager.getResourceFullPath(ResourcesTypes.SCHEDULER_PROPERTIES_FILE);
	Properties prop = new Properties();
	prop.load(resourcesLoader.getResourceAsStream(propertiesFile));
	scheduler = new StdSchedulerFactory(prop).getScheduler();
	jobsController=new ExtendedJobExecutionController("Netcell-Job-Controller");
	scheduler.addTriggerListener(jobsController);
	scheduler.addJobListener(jobsController);
    }

    private void loadJobs() throws ContextAwareException {
	String handlersFile = resourcesManager.getResourceFullPath(ResourcesTypes.SCHEDULED_JOBS_HANDLERS_FILE);
	String configFile = resourcesManager.getResourceFullPath(ResourcesTypes.SCHEDULED_JOBS_CONFIG_FILE);
	ConfigurationManager cfgManager;
	try {
	    cfgManager = PackageCfgLoader.getInstance().load(handlersFile, configFile, getResourcesLoader());
	} catch (ConfigurationException e) {
	    throw new ContextAwareException("ERROR_LOADING_SCHEDULED_JOBS",e);
	}
	
	for(Map.Entry<String, Object> entry :  cfgManager.getAllObjects().entrySet()) {
	    if(entry.getValue() instanceof ScheduledJobDefinition) {
		scheduledJobs.put(entry.getKey(), (ScheduledJobDefinition)entry.getValue());
	    }
	}
    }
    
    private void scheduleJobs() throws SchedulerException {
	for(ScheduledJobDefinition sjd : scheduledJobs.values()) {
	    scheduleJob(sjd);
	}
    }
    
    private void scheduleJob(ScheduledJobDefinition sjd) throws SchedulerException {
	JobDetail jd = getJobDetailFromDefinition(sjd);
	Trigger trigger;
	try {
	    trigger = getTriggerFromDefinition(sjd);
	} catch (ParseException e) {
	    throw new SchedulerException(e);
	}
	
	String jobsControllerName = jobsController.getName(); 
	jd.addJobListener(jobsControllerName);
	trigger.addTriggerListener(jobsControllerName);
	scheduler.scheduleJob(jd, trigger);
    }
    
    private JobDetail getJobDetailFromDefinition(ScheduledJobDefinition sjd) {
	JobDetail jobDetail = new JobDetail();
	jobDetail.setName(sjd.getId());
	/* the job class is hardcoded for now */
	jobDetail.setJobClass(WorkflowExecutionJob.class);
	JobDataMap dataMap = new JobDataMap();
	dataMap.put(ScheduledJobDefinition.FLOW_ID, sjd.getConfigParamValue(ScheduledJobDefinition.FLOW_ID));
	dataMap.put(ScheduledJobDefinition.FLOW_INPUT_PARAMS, sjd.getConfigParamValue(ScheduledJobDefinition.FLOW_INPUT_PARAMS));
	
	jobDetail.setJobDataMap(dataMap);
	return jobDetail;
    }
    
    private Trigger getTriggerFromDefinition(ScheduledJobDefinition sjd) throws ParseException {
	ExtendedCronTrigger trigger = new ExtendedCronTrigger();
	trigger.setName(sjd.getId()+"-trigger");
	trigger.setCronExpression((String)sjd.getConfigParamValue(ScheduledJobDefinition.CRON_TRIGGER));
	trigger.setAllowedConcurentJobsCount((Integer)sjd.getConfigParamValue(ScheduledJobDefinition.ALLOWED_CONCURENT_JOBS));
	trigger.setMisfireInstruction((Integer)sjd.getConfigParamValue(ScheduledJobDefinition.MISSFIRE_VALUE));
	
	return trigger;
    }
    
    

    /**
     * @return the startDelay
     */
    public int getStartDelay() {
        return startDelay;
    }

    /**
     * @param startDelay the startDelay to set
     */
    public void setStartDelay(int startDelay) {
        this.startDelay = startDelay;
    }

    public boolean reload() throws Exception {
	// TODO Auto-generated method stub
	return false;
    }

    /**
     * @return the resourcesLoader
     */
    public ClassLoader getResourcesLoader() {
	return resourcesLoader;
    }

    /**
     * @return the resourcesManager
     */
    public ResourcesManager getResourcesManager() {
	return resourcesManager;
    }

    /**
     * @param resourcesLoader
     *            the resourcesLoader to set
     */
    public void setResourcesLoader(ClassLoader resourcesLoader) {
	this.resourcesLoader = resourcesLoader;
    }

    /**
     * @param resourcesManager
     *            the resourcesManager to set
     */
    public void setResourcesManager(ResourcesManager resourcesManager) {
	this.resourcesManager = resourcesManager;
    }

    public boolean containsEntityWithId(String entityId) throws ContextAwareException {
	// TODO Auto-generated method stub
	return false;
    }

    public ScheduledJobDefinition createEntityDirectoryStructure(String name, EntityType type)
	    throws ContextAwareException {
	// TODO Auto-generated method stub
	return null;
    }

    public List<String> getDefinitionTypes() throws ContextAwareException {
	// TODO Auto-generated method stub
	return null;
    }

    public List<String> getDefinitionTypes(String parentType) throws ContextAwareException {
	// TODO Auto-generated method stub
	return null;
    }

    public Map<String, List<String>> getDependencies() throws ContextAwareException {
	// TODO Auto-generated method stub
	return null;
    }

    public Map<String, List<ScheduledJobDefinition>> getEntities() throws ContextAwareException {
	// TODO Auto-generated method stub
	return null;
    }

    public Map<String, ScheduledJobDefinition> getEntitiesAsMap() throws ContextAwareException {
	return scheduledJobs;
    }

    public List<String> getTemplatesIds() throws ContextAwareException {
	// TODO Auto-generated method stub
	return null;
    }

    public boolean removeEntity(ScheduledJobDefinition entity) throws ContextAwareException {
	// TODO Auto-generated method stub
	return false;
    }

    public ScheduledJobDefinition saveEntity(ScheduledJobDefinition entity) throws ContextAwareException {
	// TODO Auto-generated method stub
	return null;
    }
}
