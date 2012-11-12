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

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import ro.zg.netcell.vo.definitions.ScheduledJobDefinition;
import ro.zg.util.data.NameValue;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

public class WorkflowExecutionJob implements Job{
    private static Logger logger = MasterLogManager.getLogger("SCHEDULER");
    public void execute(JobExecutionContext context) throws JobExecutionException {
	JobDetail jobDetail = context.getJobDetail();
	Command command = getCommandFromJobDetail(jobDetail);
	try {
	    logger.info("Job executing "+command);
	    CommandResponse response = NodeLoader.getNetCellInstance().execute(command);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    private Command getCommandFromJobDetail(JobDetail jd) {
	Command command = new Command();
	command.setName("execute");
	JobDataMap dataMap = jd.getJobDataMap();
	command.put("fid", dataMap.getString(ScheduledJobDefinition.FLOW_ID));
	command.putAll((List<NameValue<Object>>)dataMap.get(ScheduledJobDefinition.FLOW_INPUT_PARAMS));
	return command;
    }

}
