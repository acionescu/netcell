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
package ro.zg.netcell.scheduler;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

public class ExtendedJobExecutionController implements TriggerListener, JobListener {

    /**
     * 
     */
    private static final long serialVersionUID = 5516558554501159477L;
    private String name;
    
    private Map<JobDetail, JobExecutionContext> pendingJobs = new Hashtable<JobDetail, JobExecutionContext>();
    private Map<JobDetail,Object> jobLocks = new Hashtable<JobDetail, Object>();
    
    public ExtendedJobExecutionController() {
	
    }
    
    public ExtendedJobExecutionController(String name) {
	this.name=name;
    }
    
    
    public void triggerComplete(Trigger trigger, JobExecutionContext context, int triggerInstructionCode) {

    }

    public void triggerFired(Trigger trigger, JobExecutionContext context) {
	// TODO Auto-generated method stub

    }

    public void triggerMisfired(Trigger trigger) {
	// TODO Auto-generated method stub

    }

    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
	if (context.getTrigger() instanceof ExtendedTrigger) {
	    ExtendedTrigger extendedTrigger = (ExtendedTrigger) trigger;
	    int allowedConcurentJobs = extendedTrigger.getAllowedConcurentJobsCount();

	    if (allowedConcurentJobs > 0) {
		JobDetail jd = context.getJobDetail();
		Scheduler scheduler = context.getScheduler();
		try {
		    synchronized (getJobLock(jd)) {
//			System.out.println("check veto "+Thread.currentThread()+" "+jd.hashCode());
			List<JobExecutionContext> runningJobs = (List<JobExecutionContext>) scheduler
				.getCurrentlyExecutingJobs();
			int sameJobRunningCount = getRunningJobCount(jd, runningJobs);

			// System.out.println("allowed: " + allowedConcurentJobs + " running: " + sameJobRunningCount);
			if (sameJobRunningCount >= allowedConcurentJobs) {
			    addPendingJob(context);
			    return true;
			}
		    }
		} catch (SchedulerException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
	return false;
    }

    private synchronized Object getJobLock(JobDetail jd) {
	Object lock = jobLocks.get(jd);
	if(lock==null) {
	    lock=new Object();
	    jobLocks.put(jd, lock);
	}
	return lock;
    }
    
    private int getRunningJobCount(JobDetail jd, List<JobExecutionContext> list) {
	int count = 0;
	for (JobExecutionContext jec : list) {
	    if (jec.getJobDetail().equals(jd)) {
		count++;
	    }
	}
	return count;
    }

    private void addPendingJob(JobExecutionContext context) {
	pendingJobs.put(context.getJobDetail(), context);
    }

    private void removePendingJob(JobDetail jd) {
	pendingJobs.remove(jd);
    }

    public void jobExecutionVetoed(JobExecutionContext context) {

    }

    public void jobToBeExecuted(JobExecutionContext context) {
	// TODO Auto-generated method stub

    }

    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
	JobDetail jobDetail = context.getJobDetail();
	JobExecutionContext pendingContext = pendingJobs.get(jobDetail);
	if (pendingContext == null) {
	    return;
	}
	Trigger trigger = pendingContext.getTrigger();
	if (trigger instanceof ExtendedTrigger) {
	    if (!vetoJobExecution(trigger, pendingContext)) {
		synchronized (getJobLock(jobDetail)) {
//		    System.out.println("submit pending "+Thread.currentThread() +" "+jobDetail.hashCode());
		    removePendingJob(jobDetail);
		    ((ExtendedTrigger) trigger).handleMissfiredJob(pendingContext);
//		    System.out.println("end submission");
		}
	    }
	}
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
	if(name != null) {
	    return name;
	}
	return this.toString();
    }

}
